// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.AbstractOutputStream;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpTransport

private static final class <init> extends AbstractOutputStream
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

    private (OutputStream outputstream, int i)
    {
        socketOut = outputstream;
        bytesRemaining = i;
    }

    bytesRemaining(OutputStream outputstream, int i, bytesRemaining bytesremaining)
    {
        this(outputstream, i);
    }
}
