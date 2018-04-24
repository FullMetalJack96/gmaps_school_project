// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyConnection, Settings

public final class SpdyStream
{
    private final class SpdyDataInputStream extends InputStream
    {

        static final boolean $assertionsDisabled;
        private final byte buffer[];
        private boolean closed;
        private boolean finished;
        private int limit;
        private int pos;
        final SpdyStream this$0;
        private int unacknowledgedBytes;

        private void checkNotClosed()
            throws IOException
        {
            if (closed)
            {
                throw new IOException("stream closed");
            }
            if (rstStatusCode != -1)
            {
                throw new IOException((new StringBuilder()).append("stream was reset: ").append(rstStatusString()).toString());
            } else
            {
                return;
            }
        }

        private void waitUntilReadable()
            throws IOException
        {
            long l;
            long l1;
            l = 0L;
            l1 = 0L;
            if (readTimeoutMillis != 0L)
            {
                l = System.nanoTime() / 0xf4240L;
                l1 = readTimeoutMillis;
            }
            do
            {
                if (pos != -1 || finished || closed || rstStatusCode != -1)
                {
                    break MISSING_BLOCK_LABEL_140;
                }
                InterruptedException interruptedexception;
                if (readTimeoutMillis == 0L)
                {
                    wait();
                    continue;
                }
                if (l1 <= 0L)
                {
                    break;
                }
                try
                {
                    wait(l1);
                    l1 = (l + readTimeoutMillis) - System.nanoTime() / 0xf4240L;
                }
                // Misplaced declaration of an exception variable
                catch (InterruptedException interruptedexception)
                {
                    throw new InterruptedIOException();
                }
            } while (true);
            throw new SocketTimeoutException();
        }

        public int available()
            throws IOException
        {
label0:
            {
                synchronized (SpdyStream.this)
                {
                    checkNotClosed();
                    if (pos != -1)
                    {
                        break label0;
                    }
                }
                return 0;
            }
            int j;
            if (limit <= pos)
            {
                break MISSING_BLOCK_LABEL_55;
            }
            j = limit - pos;
            spdystream;
            JVM INSTR monitorexit ;
            return j;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
            int i = limit + (buffer.length - pos);
            spdystream;
            JVM INSTR monitorexit ;
            return i;
        }

        public void close()
            throws IOException
        {
            synchronized (SpdyStream.this)
            {
                closed = true;
                notifyAll();
            }
            cancelStreamIfNecessary();
            return;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public int read()
            throws IOException
        {
            return Util.readSingleByte(this);
        }

        public int read(byte abyte0[], int i, int j)
            throws IOException
        {
label0:
            {
                synchronized (SpdyStream.this)
                {
                    Util.checkOffsetAndCount(abyte0.length, i, j);
                    waitUntilReadable();
                    checkNotClosed();
                    if (pos != -1)
                    {
                        break label0;
                    }
                }
                return -1;
            }
            int k;
            int l;
            k = limit;
            l = pos;
            int i1;
            i1 = 0;
            if (k > l)
            {
                break MISSING_BLOCK_LABEL_124;
            }
            int j1;
            j1 = Math.min(j, buffer.length - pos);
            System.arraycopy(buffer, pos, abyte0, i, j1);
            pos = j1 + pos;
            i1 = 0 + j1;
            if (pos == buffer.length)
            {
                pos = 0;
            }
            if (i1 >= j)
            {
                break MISSING_BLOCK_LABEL_184;
            }
            int k1;
            k1 = Math.min(limit - pos, j - i1);
            System.arraycopy(buffer, pos, abyte0, i + i1, k1);
            pos = k1 + pos;
            i1 += k1;
            unacknowledgedBytes = i1 + unacknowledgedBytes;
            if (unacknowledgedBytes >= 32768)
            {
                connection.writeWindowUpdateLater(id, unacknowledgedBytes);
                unacknowledgedBytes = 0;
            }
            if (pos == limit)
            {
                pos = -1;
                limit = 0;
            }
            spdystream;
            JVM INSTR monitorexit ;
            return i1;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
        }

        void receive(InputStream inputstream, int i)
            throws IOException
        {
            if (!$assertionsDisabled && Thread.holdsLock(SpdyStream.this))
            {
                throw new AssertionError();
            }
            if (i == 0)
            {
                return;
            }
            SpdyStream spdystream = SpdyStream.this;
            spdystream;
            JVM INSTR monitorenter ;
            boolean flag;
            int j;
            int k;
            int l;
            flag = finished;
            j = pos;
            k = limit;
            l = limit;
            boolean flag1;
            if (i > buffer.length - available())
            {
                flag1 = true;
            } else
            {
                flag1 = false;
            }
            spdystream;
            JVM INSTR monitorexit ;
            if (flag1)
            {
                Util.skipByReading(inputstream, i);
                closeLater(7);
                return;
            }
            break MISSING_BLOCK_LABEL_114;
            Exception exception;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
            if (flag)
            {
                Util.skipByReading(inputstream, i);
                return;
            }
            if (j < l)
            {
                int i1 = Math.min(i, buffer.length - l);
                Util.readFully(inputstream, buffer, l, i1);
                l += i1;
                i -= i1;
                if (l == buffer.length)
                {
                    l = 0;
                }
            }
            if (i > 0)
            {
                Util.readFully(inputstream, buffer, l, i);
                l += i;
            }
            synchronized (SpdyStream.this)
            {
                limit = l;
                if (pos == -1)
                {
                    pos = k;
                    notifyAll();
                }
            }
            return;
            exception1;
            spdystream1;
            JVM INSTR monitorexit ;
            throw exception1;
        }

        static 
        {
            boolean flag;
            if (!com/squareup/okhttp/internal/spdy/SpdyStream.desiredAssertionStatus())
            {
                flag = true;
            } else
            {
                flag = false;
            }
            $assertionsDisabled = flag;
        }



/*
        static boolean access$202(SpdyDataInputStream spdydatainputstream, boolean flag)
        {
            spdydatainputstream.finished = flag;
            return flag;
        }

*/


        private SpdyDataInputStream()
        {
            this$0 = SpdyStream.this;
            super();
            buffer = new byte[0x10000];
            pos = -1;
            unacknowledgedBytes = 0;
        }

    }

    private final class SpdyDataOutputStream extends OutputStream
    {

        static final boolean $assertionsDisabled;
        private final byte buffer[];
        private boolean closed;
        private boolean finished;
        private int pos;
        final SpdyStream this$0;
        private int unacknowledgedBytes;

        private void checkNotClosed()
            throws IOException
        {
            SpdyStream spdystream = SpdyStream.this;
            spdystream;
            JVM INSTR monitorenter ;
            if (closed)
            {
                throw new IOException("stream closed");
            }
            break MISSING_BLOCK_LABEL_29;
            Exception exception;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
            if (finished)
            {
                throw new IOException("stream finished");
            }
            if (rstStatusCode != -1)
            {
                throw new IOException((new StringBuilder()).append("stream was reset: ").append(rstStatusString()).toString());
            }
            spdystream;
            JVM INSTR monitorexit ;
        }

        private void waitUntilWritable(int i, boolean flag)
            throws IOException
        {
_L1:
            if (i + unacknowledgedBytes < writeWindowSize)
            {
                break MISSING_BLOCK_LABEL_114;
            }
            wait();
            if (!flag)
            {
                try
                {
                    if (closed)
                    {
                        throw new IOException("stream closed");
                    }
                }
                catch (InterruptedException interruptedexception)
                {
                    throw new InterruptedIOException();
                }
            }
            if (finished)
            {
                throw new IOException("stream finished");
            }
            if (rstStatusCode != -1)
            {
                throw new IOException((new StringBuilder()).append("stream was reset: ").append(rstStatusString()).toString());
            }
              goto _L1
        }

        private void writeFrame(boolean flag)
            throws IOException
        {
            if (!$assertionsDisabled && Thread.holdsLock(SpdyStream.this))
            {
                throw new AssertionError();
            }
            int i = -8 + pos;
            synchronized (SpdyStream.this)
            {
                waitUntilWritable(i, flag);
                unacknowledgedBytes = i + unacknowledgedBytes;
            }
            int j = 0;
            if (flag)
            {
                j = false | true;
            }
            Util.pokeInt(buffer, 0, 0x7fffffff & id, ByteOrder.BIG_ENDIAN);
            Util.pokeInt(buffer, 4, (j & 0xff) << 24 | 0xffffff & i, ByteOrder.BIG_ENDIAN);
            connection.writeFrame(buffer, 0, pos);
            pos = 8;
            return;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public void close()
            throws IOException
        {
label0:
            {
                if (!$assertionsDisabled && Thread.holdsLock(SpdyStream.this))
                {
                    throw new AssertionError();
                }
                synchronized (SpdyStream.this)
                {
                    if (!closed)
                    {
                        break label0;
                    }
                }
                return;
            }
            closed = true;
            spdystream;
            JVM INSTR monitorexit ;
            writeFrame(true);
            connection.flush();
            cancelStreamIfNecessary();
            return;
            exception;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public void flush()
            throws IOException
        {
            if (!$assertionsDisabled && Thread.holdsLock(SpdyStream.this))
            {
                throw new AssertionError();
            }
            checkNotClosed();
            if (pos > 8)
            {
                writeFrame(false);
                connection.flush();
            }
        }

        public void write(int i)
            throws IOException
        {
            Util.writeSingleByte(this, i);
        }

        public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            if (!$assertionsDisabled && Thread.holdsLock(SpdyStream.this))
            {
                throw new AssertionError();
            }
            Util.checkOffsetAndCount(abyte0.length, i, j);
            checkNotClosed();
            int k;
            for (; j > 0; j -= k)
            {
                if (pos == buffer.length)
                {
                    writeFrame(false);
                }
                k = Math.min(j, buffer.length - pos);
                System.arraycopy(abyte0, i, buffer, pos, k);
                pos = k + pos;
                i += k;
            }

        }

        static 
        {
            boolean flag;
            if (!com/squareup/okhttp/internal/spdy/SpdyStream.desiredAssertionStatus())
            {
                flag = true;
            } else
            {
                flag = false;
            }
            $assertionsDisabled = flag;
        }



/*
        static boolean access$302(SpdyDataOutputStream spdydataoutputstream, boolean flag)
        {
            spdydataoutputstream.finished = flag;
            return flag;
        }

*/



/*
        static int access$620(SpdyDataOutputStream spdydataoutputstream, int i)
        {
            int j = spdydataoutputstream.unacknowledgedBytes - i;
            spdydataoutputstream.unacknowledgedBytes = j;
            return j;
        }

*/

        private SpdyDataOutputStream()
        {
            this$0 = SpdyStream.this;
            super();
            buffer = new byte[8192];
            pos = 8;
            unacknowledgedBytes = 0;
        }

    }


    static final boolean $assertionsDisabled = false;
    private static final int DATA_FRAME_HEADER_LENGTH = 8;
    public static final int RST_CANCEL = 5;
    public static final int RST_FLOW_CONTROL_ERROR = 7;
    public static final int RST_FRAME_TOO_LARGE = 11;
    public static final int RST_INTERNAL_ERROR = 6;
    public static final int RST_INVALID_CREDENTIALS = 10;
    public static final int RST_INVALID_STREAM = 2;
    public static final int RST_PROTOCOL_ERROR = 1;
    public static final int RST_REFUSED_STREAM = 3;
    public static final int RST_STREAM_ALREADY_CLOSED = 9;
    public static final int RST_STREAM_IN_USE = 8;
    public static final int RST_UNSUPPORTED_VERSION = 4;
    private static final String STATUS_CODE_NAMES[] = {
        null, "PROTOCOL_ERROR", "INVALID_STREAM", "REFUSED_STREAM", "UNSUPPORTED_VERSION", "CANCEL", "INTERNAL_ERROR", "FLOW_CONTROL_ERROR", "STREAM_IN_USE", "STREAM_ALREADY_CLOSED", 
        "INVALID_CREDENTIALS", "FRAME_TOO_LARGE"
    };
    public static final int WINDOW_UPDATE_THRESHOLD = 32768;
    private final SpdyConnection connection;
    private final int id;
    private final SpdyDataInputStream in = new SpdyDataInputStream();
    private final SpdyDataOutputStream out = new SpdyDataOutputStream();
    private final int priority;
    private long readTimeoutMillis;
    private final List requestHeaders;
    private List responseHeaders;
    private int rstStatusCode;
    private final int slot;
    private int writeWindowSize;

    SpdyStream(int i, SpdyConnection spdyconnection, int j, int k, int l, List list, Settings settings)
    {
        boolean flag = true;
        super();
        readTimeoutMillis = 0L;
        rstStatusCode = -1;
        if (spdyconnection == null)
        {
            throw new NullPointerException("connection == null");
        }
        if (list == null)
        {
            throw new NullPointerException("requestHeaders == null");
        }
        id = i;
        connection = spdyconnection;
        priority = k;
        slot = l;
        requestHeaders = list;
        if (isLocallyInitiated())
        {
            SpdyDataInputStream spdydatainputstream1 = in;
            boolean flag2;
            SpdyDataOutputStream spdydataoutputstream1;
            if ((j & 2) != 0)
            {
                flag2 = flag;
            } else
            {
                flag2 = false;
            }
            spdydatainputstream1.finished = flag2;
            spdydataoutputstream1 = out;
            if ((j & 1) == 0)
            {
                flag = false;
            }
            spdydataoutputstream1.finished = flag;
        } else
        {
            SpdyDataInputStream spdydatainputstream = in;
            boolean flag1;
            SpdyDataOutputStream spdydataoutputstream;
            if ((j & 1) != 0)
            {
                flag1 = flag;
            } else
            {
                flag1 = false;
            }
            spdydatainputstream.finished = flag1;
            spdydataoutputstream = out;
            if ((j & 2) == 0)
            {
                flag = false;
            }
            spdydataoutputstream.finished = flag;
        }
        setSettings(settings);
    }

    private void cancelStreamIfNecessary()
        throws IOException
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        this;
        JVM INSTR monitorenter ;
        boolean flag;
        boolean flag1;
        if (in.finished || !in.closed || !out.finished && !out.closed)
        {
            flag = false;
        } else
        {
            flag = true;
        }
        flag1 = isOpen();
        this;
        JVM INSTR monitorexit ;
        Exception exception;
        if (flag)
        {
            close(5);
        } else
        if (!flag1)
        {
            connection.removeStream(id);
            return;
        }
        return;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private boolean closeInternal(int i)
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        this;
        JVM INSTR monitorenter ;
        if (rstStatusCode == -1)
        {
            break MISSING_BLOCK_LABEL_35;
        }
        this;
        JVM INSTR monitorexit ;
        return false;
        if (!in.finished || !out.finished)
        {
            break MISSING_BLOCK_LABEL_64;
        }
        this;
        JVM INSTR monitorexit ;
        return false;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        rstStatusCode = i;
        notifyAll();
        this;
        JVM INSTR monitorexit ;
        connection.removeStream(id);
        return true;
    }

    private String rstStatusString()
    {
        if (rstStatusCode > 0 && rstStatusCode < STATUS_CODE_NAMES.length)
        {
            return STATUS_CODE_NAMES[rstStatusCode];
        } else
        {
            return Integer.toString(rstStatusCode);
        }
    }

    private void setSettings(Settings settings)
    {
        int i = 0x10000;
        if (!$assertionsDisabled && !Thread.holdsLock(connection))
        {
            throw new AssertionError();
        }
        if (settings != null)
        {
            i = settings.getInitialWindowSize(i);
        }
        writeWindowSize = i;
    }

    public void close(int i)
        throws IOException
    {
        if (!closeInternal(i))
        {
            return;
        } else
        {
            connection.writeSynReset(id, i);
            return;
        }
    }

    public void closeLater(int i)
    {
        if (!closeInternal(i))
        {
            return;
        } else
        {
            connection.writeSynResetLater(id, i);
            return;
        }
    }

    public SpdyConnection getConnection()
    {
        return connection;
    }

    public InputStream getInputStream()
    {
        return in;
    }

    public OutputStream getOutputStream()
    {
        this;
        JVM INSTR monitorenter ;
        if (responseHeaders == null && !isLocallyInitiated())
        {
            throw new IllegalStateException("reply before requesting the output stream");
        }
        break MISSING_BLOCK_LABEL_31;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        this;
        JVM INSTR monitorexit ;
        return out;
    }

    int getPriority()
    {
        return priority;
    }

    public long getReadTimeoutMillis()
    {
        return readTimeoutMillis;
    }

    public List getRequestHeaders()
    {
        return requestHeaders;
    }

    public List getResponseHeaders()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        while (responseHeaders == null && rstStatusCode == -1) 
        {
            wait();
        }
        break MISSING_BLOCK_LABEL_46;
        InterruptedException interruptedexception;
        interruptedexception;
        InterruptedIOException interruptedioexception = new InterruptedIOException();
        interruptedioexception.initCause(interruptedexception);
        throw interruptedioexception;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        List list;
        if (responseHeaders == null)
        {
            break MISSING_BLOCK_LABEL_64;
        }
        list = responseHeaders;
        this;
        JVM INSTR monitorexit ;
        return list;
        throw new IOException((new StringBuilder()).append("stream was reset: ").append(rstStatusString()).toString());
    }

    public int getRstStatusCode()
    {
        this;
        JVM INSTR monitorenter ;
        int i = rstStatusCode;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    int getSlot()
    {
        return slot;
    }

    public boolean isLocallyInitiated()
    {
        boolean flag;
        if (id % 2 == 1)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        return connection.client == flag;
    }

    public boolean isOpen()
    {
        this;
        JVM INSTR monitorenter ;
        int i = rstStatusCode;
        boolean flag = false;
        if (i == -1) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorexit ;
        return flag;
_L2:
        List list;
        if (!in.finished && !in.closed || !out.finished && !out.closed)
        {
            break MISSING_BLOCK_LABEL_71;
        }
        list = responseHeaders;
        flag = false;
        if (list != null)
        {
            continue; /* Loop/switch isn't completed */
        }
        flag = true;
        if (true) goto _L1; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }

    void receiveData(InputStream inputstream, int i)
        throws IOException
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        } else
        {
            in.receive(inputstream, i);
            return;
        }
    }

    void receiveFin()
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        this;
        JVM INSTR monitorenter ;
        boolean flag;
        in.finished = true;
        flag = isOpen();
        notifyAll();
        this;
        JVM INSTR monitorexit ;
        if (!flag)
        {
            connection.removeStream(id);
        }
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    void receiveHeaders(List list)
        throws IOException
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        boolean flag = false;
        this;
        JVM INSTR monitorenter ;
        if (responseHeaders == null)
        {
            break MISSING_BLOCK_LABEL_80;
        }
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(responseHeaders);
        arraylist.addAll(list);
        responseHeaders = arraylist;
_L1:
        this;
        JVM INSTR monitorexit ;
        if (flag)
        {
            closeLater(1);
        }
        return;
        flag = true;
          goto _L1
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    void receiveReply(List list)
        throws IOException
    {
        boolean flag;
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        flag = false;
        boolean flag1 = true;
        this;
        JVM INSTR monitorenter ;
        if (!isLocallyInitiated() || responseHeaders != null) goto _L2; else goto _L1
_L1:
        responseHeaders = list;
        flag1 = isOpen();
        notifyAll();
_L5:
        this;
        JVM INSTR monitorexit ;
        if (!flag) goto _L4; else goto _L3
_L3:
        closeLater(8);
_L7:
        return;
_L2:
        flag = true;
          goto _L5
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
_L4:
        if (flag1) goto _L7; else goto _L6
_L6:
        connection.removeStream(id);
        return;
          goto _L5
    }

    void receiveRstStream(int i)
    {
        this;
        JVM INSTR monitorenter ;
        if (rstStatusCode == -1)
        {
            rstStatusCode = i;
            notifyAll();
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    void receiveSettings(Settings settings)
    {
        if (!$assertionsDisabled && !Thread.holdsLock(this))
        {
            throw new AssertionError();
        } else
        {
            setSettings(settings);
            notifyAll();
            return;
        }
    }

    void receiveWindowUpdate(int i)
    {
        this;
        JVM INSTR monitorenter ;
        int j = 
// JavaClassFileOutputException: get_constant: invalid tag

    public void reply(List list, boolean flag)
        throws IOException
    {
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        this;
        JVM INSTR monitorenter ;
        if (list != null)
        {
            break MISSING_BLOCK_LABEL_45;
        }
        throw new NullPointerException("responseHeaders == null");
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        if (isLocallyInitiated())
        {
            throw new IllegalStateException("cannot reply to a locally initiated stream");
        }
        if (responseHeaders != null)
        {
            throw new IllegalStateException("reply already sent");
        }
        responseHeaders = list;
        int i;
        i = 0;
        if (flag)
        {
            break MISSING_BLOCK_LABEL_105;
        }
        out.finished = true;
        i = false | true;
        this;
        JVM INSTR monitorexit ;
        connection.writeSynReply(id, i, list);
        return;
    }

    public void setReadTimeout(long l)
    {
        readTimeoutMillis = l;
    }

    static 
    {
        boolean flag;
        if (!com/squareup/okhttp/internal/spdy/SpdyStream.desiredAssertionStatus())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        $assertionsDisabled = flag;
    }







}
