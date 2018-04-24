// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.internal.AbstractOutputStream;
import com.squareup.okhttp.internal.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.ProtocolException;
import java.net.Socket;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            Transport, HttpEngine, RequestHeaders, HttpURLConnectionImpl, 
//            RetryableOutputStream, ResponseHeaders, UnknownLengthHttpInputStream, RawHeaders, 
//            AbstractHttpInputStream

public final class HttpTransport
    implements Transport
{
    private static class ChunkedInputStream extends AbstractHttpInputStream
    {

        private static final int NO_CHUNK_YET = -1;
        private int bytesRemainingInChunk;
        private boolean hasMoreChunks;
        private final HttpTransport transport;

        private void readChunkSize()
            throws IOException
        {
            if (bytesRemainingInChunk != -1)
            {
                Util.readAsciiLine(in);
            }
            String s = Util.readAsciiLine(in);
            int i = s.indexOf(";");
            if (i != -1)
            {
                s = s.substring(0, i);
            }
            try
            {
                bytesRemainingInChunk = Integer.parseInt(s.trim(), 16);
            }
            catch (NumberFormatException numberformatexception)
            {
                throw new ProtocolException((new StringBuilder()).append("Expected a hex chunk size but was ").append(s).toString());
            }
            if (bytesRemainingInChunk == 0)
            {
                hasMoreChunks = false;
                RawHeaders rawheaders = httpEngine.responseHeaders.getHeaders();
                RawHeaders.readHeaders(transport.socketIn, rawheaders);
                httpEngine.receiveHeaders(rawheaders);
                endOfInput(false);
            }
        }

        public int available()
            throws IOException
        {
            checkNotClosed();
            if (!hasMoreChunks || bytesRemainingInChunk == -1)
            {
                return 0;
            } else
            {
                return Math.min(in.available(), bytesRemainingInChunk);
            }
        }

        public void close()
            throws IOException
        {
            if (closed)
            {
                return;
            }
            if (hasMoreChunks && !HttpTransport.discardStream(httpEngine, this))
            {
                unexpectedEndOfInput();
            }
            closed = true;
        }

        public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            Util.checkOffsetAndCount(abyte0.length, i, j);
            checkNotClosed();
            if (!hasMoreChunks)
            {
                return -1;
            }
            if (bytesRemainingInChunk == 0 || bytesRemainingInChunk == -1)
            {
                readChunkSize();
                if (!hasMoreChunks)
                {
                    return -1;
                }
            }
            int k = in.read(abyte0, i, Math.min(j, bytesRemainingInChunk));
            if (k == -1)
            {
                unexpectedEndOfInput();
                throw new IOException("unexpected end of stream");
            } else
            {
                bytesRemainingInChunk = bytesRemainingInChunk - k;
                cacheWrite(abyte0, i, k);
                return k;
            }
        }

        ChunkedInputStream(InputStream inputstream, CacheRequest cacherequest, HttpTransport httptransport)
            throws IOException
        {
            super(inputstream, httptransport.httpEngine, cacherequest);
            bytesRemainingInChunk = -1;
            hasMoreChunks = true;
            transport = httptransport;
        }
    }

    private static final class ChunkedOutputStream extends AbstractOutputStream
    {

        private static final byte CRLF[] = {
            13, 10
        };
        private static final byte FINAL_CHUNK[] = {
            48, 13, 10, 13, 10
        };
        private static final byte HEX_DIGITS[] = {
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
            97, 98, 99, 100, 101, 102
        };
        private final ByteArrayOutputStream bufferedChunk;
        private final byte hex[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 13, 10
        };
        private final int maxChunkLength;
        private final OutputStream socketOut;

        private int dataLength(int i)
        {
            int j = 4;
            for (int k = i - j; k > 0; k >>= 4)
            {
                j++;
            }

            return i - j;
        }

        private void writeBufferedChunkToSocket()
            throws IOException
        {
            int i = bufferedChunk.size();
            if (i <= 0)
            {
                return;
            } else
            {
                writeHex(i);
                bufferedChunk.writeTo(socketOut);
                bufferedChunk.reset();
                socketOut.write(CRLF);
                return;
            }
        }

        private void writeHex(int i)
            throws IOException
        {
            int j = 8;
            do
            {
                byte abyte0[] = hex;
                j--;
                abyte0[j] = HEX_DIGITS[i & 0xf];
                i >>>= 4;
            } while (i != 0);
            socketOut.write(hex, j, hex.length - j);
        }

        public void close()
            throws IOException
        {
            this;
            JVM INSTR monitorenter ;
            boolean flag = closed;
            if (!flag) goto _L2; else goto _L1
_L1:
            this;
            JVM INSTR monitorexit ;
            return;
_L2:
            closed = true;
            writeBufferedChunkToSocket();
            socketOut.write(FINAL_CHUNK);
            if (true) goto _L1; else goto _L3
_L3:
            Exception exception;
            exception;
            throw exception;
        }

        public void flush()
            throws IOException
        {
            this;
            JVM INSTR monitorenter ;
            boolean flag = closed;
            if (!flag) goto _L2; else goto _L1
_L1:
            this;
            JVM INSTR monitorexit ;
            return;
_L2:
            writeBufferedChunkToSocket();
            socketOut.flush();
            if (true) goto _L1; else goto _L3
_L3:
            Exception exception;
            exception;
            throw exception;
        }

        public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            this;
            JVM INSTR monitorenter ;
            checkNotClosed();
            Util.checkOffsetAndCount(abyte0.length, i, j);
_L2:
            if (j <= 0)
            {
                break MISSING_BLOCK_LABEL_128;
            }
            int k;
            if (bufferedChunk.size() > 0 || j < maxChunkLength)
            {
                k = Math.min(j, maxChunkLength - bufferedChunk.size());
                bufferedChunk.write(abyte0, i, k);
                if (bufferedChunk.size() == maxChunkLength)
                {
                    writeBufferedChunkToSocket();
                }
                break MISSING_BLOCK_LABEL_131;
            }
            k = maxChunkLength;
            writeHex(k);
            socketOut.write(abyte0, i, k);
            socketOut.write(CRLF);
            break MISSING_BLOCK_LABEL_131;
            Exception exception;
            exception;
            throw exception;
            this;
            JVM INSTR monitorexit ;
            return;
            i += k;
            j -= k;
            if (true) goto _L2; else goto _L1
_L1:
        }


        private ChunkedOutputStream(OutputStream outputstream, int i)
        {
            socketOut = outputstream;
            maxChunkLength = Math.max(1, dataLength(i));
            bufferedChunk = new ByteArrayOutputStream(i);
        }

    }

    private static class FixedLengthInputStream extends AbstractHttpInputStream
    {

        private int bytesRemaining;

        public int available()
            throws IOException
        {
            checkNotClosed();
            if (bytesRemaining == 0)
            {
                return 0;
            } else
            {
                return Math.min(in.available(), bytesRemaining);
            }
        }

        public void close()
            throws IOException
        {
            if (closed)
            {
                return;
            }
            if (bytesRemaining != 0 && !HttpTransport.discardStream(httpEngine, this))
            {
                unexpectedEndOfInput();
            }
            closed = true;
        }

        public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            Util.checkOffsetAndCount(abyte0.length, i, j);
            checkNotClosed();
            int k;
            if (bytesRemaining == 0)
            {
                k = -1;
            } else
            {
                k = in.read(abyte0, i, Math.min(j, bytesRemaining));
                if (k == -1)
                {
                    unexpectedEndOfInput();
                    throw new ProtocolException("unexpected end of stream");
                }
                bytesRemaining = bytesRemaining - k;
                cacheWrite(abyte0, i, k);
                if (bytesRemaining == 0)
                {
                    endOfInput(false);
                    return k;
                }
            }
            return k;
        }

        public FixedLengthInputStream(InputStream inputstream, CacheRequest cacherequest, HttpEngine httpengine, int i)
            throws IOException
        {
            super(inputstream, httpengine, cacherequest);
            bytesRemaining = i;
            if (bytesRemaining == 0)
            {
                endOfInput(false);
            }
        }
    }

    private static final class FixedLengthOutputStream extends AbstractOutputStream
    {

        private int bytesRemaining;
        private final OutputStream socketOut;

        public void close()
            throws IOException
        {
            if (!closed)
            {
                closed = true;
                if (bytesRemaining > 0)
                {
                    throw new ProtocolException("unexpected end of stream");
                }
            }
        }

        public void flush()
            throws IOException
        {
            if (closed)
            {
                return;
            } else
            {
                socketOut.flush();
                return;
            }
        }

        public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            checkNotClosed();
            Util.checkOffsetAndCount(abyte0.length, i, j);
            if (j > bytesRemaining)
            {
                throw new ProtocolException((new StringBuilder()).append("expected ").append(bytesRemaining).append(" bytes but received ").append(j).toString());
            } else
            {
                socketOut.write(abyte0, i, j);
                bytesRemaining = bytesRemaining - j;
                return;
            }
        }

        private FixedLengthOutputStream(OutputStream outputstream, int i)
        {
            socketOut = outputstream;
            bytesRemaining = i;
        }

    }


    public static final int DEFAULT_CHUNK_LENGTH = 1024;
    private static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;
    private final HttpEngine httpEngine;
    private OutputStream requestOut;
    private final InputStream socketIn;
    private final OutputStream socketOut;

    public HttpTransport(HttpEngine httpengine, OutputStream outputstream, InputStream inputstream)
    {
        httpEngine = httpengine;
        socketOut = outputstream;
        requestOut = outputstream;
        socketIn = inputstream;
    }

    private static boolean discardStream(HttpEngine httpengine, InputStream inputstream)
    {
        Connection connection = httpengine.connection;
        if (connection != null) goto _L2; else goto _L1
_L1:
        Socket socket;
        return false;
_L2:
        if ((socket = connection.getSocket()) == null) goto _L1; else goto _L3
_L3:
        int i;
        Exception exception;
        try
        {
            i = socket.getSoTimeout();
            socket.setSoTimeout(100);
        }
        catch (IOException ioexception)
        {
            return false;
        }
        Util.skipAll(inputstream);
        socket.setSoTimeout(i);
        return true;
        exception;
        socket.setSoTimeout(i);
        throw exception;
    }

    public OutputStream createRequestBody()
        throws IOException
    {
        boolean flag = httpEngine.requestHeaders.isChunked();
        if (!flag && httpEngine.policy.getChunkLength() > 0 && httpEngine.connection.getHttpMinorVersion() != 0)
        {
            httpEngine.requestHeaders.setChunked();
            flag = true;
        }
        if (flag)
        {
            int k = httpEngine.policy.getChunkLength();
            if (k == -1)
            {
                k = 1024;
            }
            writeRequestHeaders();
            return new ChunkedOutputStream(requestOut, k);
        }
        int i = httpEngine.policy.getFixedContentLength();
        if (i != -1)
        {
            httpEngine.requestHeaders.setContentLength(i);
            writeRequestHeaders();
            return new FixedLengthOutputStream(requestOut, i);
        }
        int j = httpEngine.requestHeaders.getContentLength();
        if (j != -1)
        {
            writeRequestHeaders();
            return new RetryableOutputStream(j);
        } else
        {
            return new RetryableOutputStream();
        }
    }

    public void flushRequest()
        throws IOException
    {
        requestOut.flush();
        requestOut = socketOut;
    }

    public InputStream getTransferStream(CacheRequest cacherequest)
        throws IOException
    {
        if (!httpEngine.hasResponseBody())
        {
            return new FixedLengthInputStream(socketIn, cacherequest, httpEngine, 0);
        }
        if (httpEngine.responseHeaders.isChunked())
        {
            return new ChunkedInputStream(socketIn, cacherequest, this);
        }
        if (httpEngine.responseHeaders.getContentLength() != -1)
        {
            return new FixedLengthInputStream(socketIn, cacherequest, httpEngine, httpEngine.responseHeaders.getContentLength());
        } else
        {
            return new UnknownLengthHttpInputStream(socketIn, cacherequest, httpEngine);
        }
    }

    public boolean makeReusable(boolean flag, OutputStream outputstream, InputStream inputstream)
    {
        while (flag || outputstream != null && !((AbstractOutputStream)outputstream).isClosed() || httpEngine.requestHeaders.hasConnectionClose() || httpEngine.responseHeaders != null && httpEngine.responseHeaders.hasConnectionClose() || (inputstream instanceof UnknownLengthHttpInputStream)) 
        {
            return false;
        }
        if (inputstream != null)
        {
            return discardStream(httpEngine, inputstream);
        } else
        {
            return true;
        }
    }

    public ResponseHeaders readResponseHeaders()
        throws IOException
    {
        RawHeaders rawheaders = RawHeaders.fromBytes(socketIn);
        httpEngine.connection.setHttpMinorVersion(rawheaders.getHttpMinorVersion());
        httpEngine.receiveHeaders(rawheaders);
        return new ResponseHeaders(httpEngine.uri, rawheaders);
    }

    public void writeRequestBody(RetryableOutputStream retryableoutputstream)
        throws IOException
    {
        retryableoutputstream.writeToSocket(requestOut);
    }

    public void writeRequestHeaders()
        throws IOException
    {
        httpEngine.writingRequestHeaders();
        byte abyte0[] = httpEngine.requestHeaders.getHeaders().toBytes();
        requestOut.write(abyte0);
    }



}
