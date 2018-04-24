// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

public final class Util
{

    public static final byte EMPTY_BYTE_ARRAY[] = new byte[0];
    public static final String EMPTY_STRING_ARRAY[] = new String[0];
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static AtomicReference skipBuffer = new AtomicReference();

    private Util()
    {
    }

    public static void checkOffsetAndCount(int i, int j, int k)
    {
        if ((j | k) < 0 || j > i || i - j < k)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            return;
        }
    }

    public static void closeAll(Closeable closeable, Closeable closeable1)
        throws IOException
    {
        Throwable throwable;
        throwable = null;
        try
        {
            closeable.close();
        }
        catch (Throwable throwable1)
        {
            throwable = throwable1;
        }
        closeable1.close();
_L2:
        if (throwable == null)
        {
            return;
        }
        break; /* Loop/switch isn't completed */
        Throwable throwable2;
        throwable2;
        if (throwable == null)
        {
            throwable = throwable2;
        }
        if (true) goto _L2; else goto _L1
_L1:
        if (throwable instanceof IOException)
        {
            throw (IOException)throwable;
        }
        if (throwable instanceof RuntimeException)
        {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error)
        {
            throw (Error)throwable;
        } else
        {
            throw new AssertionError(throwable);
        }
    }

    public static void closeQuietly(Closeable closeable)
    {
        if (closeable == null)
        {
            break MISSING_BLOCK_LABEL_10;
        }
        closeable.close();
        return;
        RuntimeException runtimeexception;
        runtimeexception;
        throw runtimeexception;
        Exception exception;
        exception;
    }

    public static void closeQuietly(Socket socket)
    {
        if (socket == null)
        {
            break MISSING_BLOCK_LABEL_8;
        }
        socket.close();
        return;
        RuntimeException runtimeexception;
        runtimeexception;
        throw runtimeexception;
        Exception exception;
        exception;
    }

    public static int copy(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        int i = 0;
        byte abyte0[] = new byte[8192];
        do
        {
            int j = inputstream.read(abyte0);
            if (j != -1)
            {
                i += j;
                outputstream.write(abyte0, 0, j);
            } else
            {
                return i;
            }
        } while (true);
    }

    public static void deleteContents(File file)
        throws IOException
    {
        File afile[] = file.listFiles();
        if (afile == null)
        {
            throw new IOException((new StringBuilder()).append("not a readable directory: ").append(file).toString());
        }
        int i = afile.length;
        for (int j = 0; j < i; j++)
        {
            File file1 = afile[j];
            if (file1.isDirectory())
            {
                deleteContents(file1);
            }
            if (!file1.delete())
            {
                throw new IOException((new StringBuilder()).append("failed to delete file: ").append(file1).toString());
            }
        }

    }

    public static boolean equal(Object obj, Object obj1)
    {
        return obj == obj1 || obj != null && obj.equals(obj1);
    }

    public static int getDefaultPort(String s)
    {
        if ("http".equalsIgnoreCase(s))
        {
            return 80;
        }
        return !"https".equalsIgnoreCase(s) ? -1 : 443;
    }

    private static int getEffectivePort(String s, int i)
    {
        if (i != -1)
        {
            return i;
        } else
        {
            return getDefaultPort(s);
        }
    }

    public static int getEffectivePort(URI uri)
    {
        return getEffectivePort(uri.getScheme(), uri.getPort());
    }

    public static int getEffectivePort(URL url)
    {
        return getEffectivePort(url.getProtocol(), url.getPort());
    }

    public static void pokeInt(byte abyte0[], int i, int j, ByteOrder byteorder)
    {
        if (byteorder == ByteOrder.BIG_ENDIAN)
        {
            int j1 = i + 1;
            abyte0[i] = (byte)(0xff & j >> 24);
            int k1 = j1 + 1;
            abyte0[j1] = (byte)(0xff & j >> 16);
            int l1 = k1 + 1;
            abyte0[k1] = (byte)(0xff & j >> 8);
            abyte0[l1] = (byte)(0xff & j >> 0);
            return;
        } else
        {
            int k = i + 1;
            abyte0[i] = (byte)(0xff & j >> 0);
            int l = k + 1;
            abyte0[k] = (byte)(0xff & j >> 8);
            int i1 = l + 1;
            abyte0[l] = (byte)(0xff & j >> 16);
            abyte0[i1] = (byte)(0xff & j >> 24);
            return;
        }
    }

    public static String readAsciiLine(InputStream inputstream)
        throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder(80);
        do
        {
            int i = inputstream.read();
            if (i == -1)
            {
                throw new EOFException();
            }
            if (i == 10)
            {
                int j = stringbuilder.length();
                if (j > 0 && stringbuilder.charAt(j - 1) == '\r')
                {
                    stringbuilder.setLength(j - 1);
                }
                return stringbuilder.toString();
            }
            stringbuilder.append((char)i);
        } while (true);
    }

    public static String readFully(Reader reader)
        throws IOException
    {
        StringWriter stringwriter;
        char ac[];
        stringwriter = new StringWriter();
        ac = new char[1024];
_L1:
        int i = reader.read(ac);
        if (i == -1)
        {
            break MISSING_BLOCK_LABEL_45;
        }
        stringwriter.write(ac, 0, i);
          goto _L1
        Exception exception;
        exception;
        reader.close();
        throw exception;
        String s = stringwriter.toString();
        reader.close();
        return s;
    }

    public static void readFully(InputStream inputstream, byte abyte0[])
        throws IOException
    {
        readFully(inputstream, abyte0, 0, abyte0.length);
    }

    public static void readFully(InputStream inputstream, byte abyte0[], int i, int j)
        throws IOException
    {
        if (j != 0)
        {
            if (inputstream == null)
            {
                throw new NullPointerException("in == null");
            }
            if (abyte0 == null)
            {
                throw new NullPointerException("dst == null");
            }
            checkOffsetAndCount(abyte0.length, i, j);
            while (j > 0) 
            {
                int k = inputstream.read(abyte0, i, j);
                if (k < 0)
                {
                    throw new EOFException();
                }
                i += k;
                j -= k;
            }
        }
    }

    public static int readSingleByte(InputStream inputstream)
        throws IOException
    {
        int i = -1;
        byte abyte0[] = new byte[1];
        if (inputstream.read(abyte0, 0, 1) != i)
        {
            i = 0xff & abyte0[0];
        }
        return i;
    }

    public static void skipAll(InputStream inputstream)
        throws IOException
    {
        do
        {
            inputstream.skip(0x7fffffffffffffffL);
        } while (inputstream.read() != -1);
    }

    public static long skipByReading(InputStream inputstream, long l)
        throws IOException
    {
        byte abyte0[];
        long l1;
        abyte0 = (byte[])skipBuffer.getAndSet(null);
        if (abyte0 == null)
        {
            abyte0 = new byte[4096];
        }
        l1 = 0L;
_L4:
        if (l1 >= l) goto _L2; else goto _L1
_L1:
        int i;
        int j;
        i = (int)Math.min(l - l1, abyte0.length);
        j = inputstream.read(abyte0, 0, i);
        if (j != -1) goto _L3; else goto _L2
_L2:
        skipBuffer.set(abyte0);
        return l1;
_L3:
        l1 += j;
        if (j >= i) goto _L4; else goto _L2
    }

    public static void writeSingleByte(OutputStream outputstream, int i)
        throws IOException
    {
        byte abyte0[] = new byte[1];
        abyte0[0] = (byte)(i & 0xff);
        outputstream.write(abyte0);
    }

}
