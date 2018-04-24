// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import java.io.IOException;
import java.io.OutputStream;

abstract class AbstractHttpOutputStream extends OutputStream
{

    protected boolean closed;

    AbstractHttpOutputStream()
    {
    }

    protected final void checkNotClosed()
        throws IOException
    {
        if (closed)
        {
            throw new IOException("stream closed");
        } else
        {
            return;
        }
    }

    public final void write(int i)
        throws IOException
    {
        byte abyte0[] = new byte[1];
        abyte0[0] = (byte)i;
        write(abyte0);
    }
}
