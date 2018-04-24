// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheRequest;
import java.net.ProtocolException;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            AbstractHttpInputStream, HttpTransport, HttpEngine

private static class endOfInput extends AbstractHttpInputStream
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
        if (bytesRemaining != 0 && !HttpTransport.access$200(httpEngine, this))
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

    public (InputStream inputstream, CacheRequest cacherequest, HttpEngine httpengine, int i)
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
