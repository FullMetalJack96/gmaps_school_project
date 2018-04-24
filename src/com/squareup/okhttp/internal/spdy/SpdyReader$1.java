// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Util;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyReader

class this._cls0 extends InputStream
{

    final SpdyReader this$0;

    public void close()
        throws IOException
    {
        SpdyReader.access$100(SpdyReader.this).close();
    }

    public int read()
        throws IOException
    {
        return Util.readSingleByte(this);
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        int k = Math.min(j, SpdyReader.access$000(SpdyReader.this));
        int l = SpdyReader.access$100(SpdyReader.this).read(abyte0, i, k);
        SpdyReader.access$020(SpdyReader.this, l);
        return l;
    }

    ()
    {
        this$0 = SpdyReader.this;
        super();
    }
}
