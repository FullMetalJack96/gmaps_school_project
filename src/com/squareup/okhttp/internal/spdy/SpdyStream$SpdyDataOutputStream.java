// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyStream, SpdyConnection

private final class <init> extends OutputStream
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
        if (SpdyStream.access$1000(SpdyStream.this) != -1)
        {
            throw new IOException((new StringBuilder()).append("stream was reset: ").append(SpdyStream.access$1200(SpdyStream.this)).toString());
        }
        spdystream;
        JVM INSTR monitorexit ;
    }

    private void waitUntilWritable(int i, boolean flag)
        throws IOException
    {
_L1:
        if (i + unacknowledgedBytes < SpdyStream.access$1300(SpdyStream.this))
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
        if (SpdyStream.access$1000(SpdyStream.this) != -1)
        {
            throw new IOException((new StringBuilder()).append("stream was reset: ").append(SpdyStream.access$1200(SpdyStream.this)).toString());
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
        Util.pokeInt(buffer, 0, 0x7fffffff & SpdyStream.access$700(SpdyStream.this), ByteOrder.BIG_ENDIAN);
        Util.pokeInt(buffer, 4, (j & 0xff) << 24 | 0xffffff & i, ByteOrder.BIG_ENDIAN);
        SpdyStream.access$800(SpdyStream.this).writeFrame(buffer, 0, pos);
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
        SpdyStream.access$800(SpdyStream.this).flush();
        SpdyStream.access$1100(SpdyStream.this);
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
            SpdyStream.access$800(SpdyStream.this).flush();
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
    static boolean access$302( , boolean flag)
    {
        .finished = flag;
        return flag;
    }

*/



/*
    static int access$620(finished finished1, int i)
    {
        int j = finished1.unacknowledgedBytes - i;
        finished1.unacknowledgedBytes = j;
        return j;
    }

*/

    private unacknowledgedBytes()
    {
        this$0 = SpdyStream.this;
        super();
        buffer = new byte[8192];
        pos = 8;
        unacknowledgedBytes = 0;
    }

    unacknowledgedBytes(unacknowledgedBytes unacknowledgedbytes)
    {
        this();
    }
}
