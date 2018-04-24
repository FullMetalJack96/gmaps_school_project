// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyStream, SpdyConnection

private final class <init> extends InputStream
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
        if (SpdyStream.access$1000(SpdyStream.this) != -1)
        {
            throw new IOException((new StringBuilder()).append("stream was reset: ").append(SpdyStream.access$1200(SpdyStream.this)).toString());
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
        if (SpdyStream.access$900(SpdyStream.this) != 0L)
        {
            l = System.nanoTime() / 0xf4240L;
            l1 = SpdyStream.access$900(SpdyStream.this);
        }
        do
        {
            if (pos != -1 || finished || closed || SpdyStream.access$1000(SpdyStream.this) != -1)
            {
                break MISSING_BLOCK_LABEL_140;
            }
            InterruptedException interruptedexception;
            if (SpdyStream.access$900(SpdyStream.this) == 0L)
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
                l1 = (l + SpdyStream.access$900(SpdyStream.this)) - System.nanoTime() / 0xf4240L;
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
        SpdyStream.access$1100(SpdyStream.this);
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
            SpdyStream.access$800(SpdyStream.this).writeWindowUpdateLater(SpdyStream.access$700(SpdyStream.this), unacknowledgedBytes);
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
    static boolean access$202( , boolean flag)
    {
        .finished = flag;
        return flag;
    }

*/


    private finished()
    {
        this$0 = SpdyStream.this;
        super();
        buffer = new byte[0x10000];
        pos = -1;
        unacknowledgedBytes = 0;
    }

    unacknowledgedBytes(unacknowledgedBytes unacknowledgedbytes)
    {
        this();
    }
}
