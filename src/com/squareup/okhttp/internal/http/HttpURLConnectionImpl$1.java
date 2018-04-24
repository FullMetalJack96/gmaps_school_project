// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.AbstractOutputStream;
import com.squareup.okhttp.internal.FaultRecoveringOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpURLConnectionImpl, HttpEngine

class init> extends FaultRecoveringOutputStream
{

    final HttpURLConnectionImpl this$0;

    protected OutputStream replacementStream(IOException ioexception)
        throws IOException
    {
        if ((httpEngine.getRequestBody() instanceof AbstractOutputStream) && ((AbstractOutputStream)httpEngine.getRequestBody()).isClosed())
        {
            return null;
        }
        if (HttpURLConnectionImpl.access$000(HttpURLConnectionImpl.this, ioexception))
        {
            return httpEngine.getRequestBody();
        } else
        {
            return null;
        }
    }

    (int i, OutputStream outputstream)
    {
        this$0 = HttpURLConnectionImpl.this;
        super(i, outputstream);
    }
}
