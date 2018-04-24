// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyReader

class this._cls0 extends Inflater
{

    final SpdyReader this$0;

    public int inflate(byte abyte0[], int i, int j)
        throws DataFormatException
    {
        int k = super.inflate(abyte0, i, j);
        if (k == 0 && needsDictionary())
        {
            setDictionary(SpdyReader.DICTIONARY);
            k = super.inflate(abyte0, i, j);
        }
        return k;
    }

    ()
    {
        this$0 = SpdyReader.this;
        super();
    }
}
