// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.OkHttpClient;
import java.net.HttpURLConnection;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.util.Set;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpURLConnectionImpl, HttpEngine, HttpsURLConnectionImpl, OkResponseCache

private final class <init> extends HttpURLConnectionImpl
{

    final HttpsURLConnectionImpl this$0;

    protected HttpURLConnection getHttpConnectionToCache()
    {
        return HttpsURLConnectionImpl.this;
    }

    public SecureCacheResponse getSecureCacheResponse()
    {
        if (httpEngine instanceof httpEngine)
        {
            return (SecureCacheResponse)httpEngine.getCacheResponse();
        } else
        {
            return null;
        }
    }

    private (URL url, OkHttpClient okhttpclient, OkResponseCache okresponsecache, Set set)
    {
        this$0 = HttpsURLConnectionImpl.this;
        super(url, okhttpclient, okresponsecache, set);
    }

    this._cls0(URL url, OkHttpClient okhttpclient, OkResponseCache okresponsecache, Set set, this._cls0 _pcls0)
    {
        this(url, okhttpclient, okresponsecache, set);
    }
}
