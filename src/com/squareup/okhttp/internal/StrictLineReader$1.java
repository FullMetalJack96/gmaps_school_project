// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

// Referenced classes of package com.squareup.okhttp.internal:
//            StrictLineReader

class this._cls0 extends ByteArrayOutputStream
{

    final StrictLineReader this$0;

    public String toString()
    {
        int i;
        String s;
        if (count > 0 && buf[-1 + count] == 13)
        {
            i = -1 + count;
        } else
        {
            i = count;
        }
        try
        {
            s = new String(buf, 0, i, StrictLineReader.access$000(StrictLineReader.this).name());
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new AssertionError(unsupportedencodingexception);
        }
        return s;
    }

    (int i)
    {
        this$0 = StrictLineReader.this;
        super(i);
    }
}
