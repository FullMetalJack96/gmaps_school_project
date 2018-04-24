// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.TunnelRequest;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.CacheResponse;
import java.net.SecureCacheResponse;
import java.net.URL;
import javax.net.ssl.SSLSocket;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpEngine, RequestHeaders, HttpURLConnectionImpl, HttpsURLConnectionImpl, 
//            RawHeaders, RetryableOutputStream

public static final class sslSocket extends HttpEngine
{

    private SSLSocket sslSocket;

    protected boolean acceptCacheResponseType(CacheResponse cacheresponse)
    {
        return cacheresponse instanceof SecureCacheResponse;
    }

    protected void connected(Connection connection)
    {
        sslSocket = (SSLSocket)connection.getSocket();
    }

    protected TunnelRequest getTunnelConfig()
    {
        String s = requestHeaders.getUserAgent();
        if (s == null)
        {
            s = getDefaultUserAgent();
        }
        URL url = policy.getURL();
        return new TunnelRequest(url.getHost(), Util.getEffectivePort(url), s, requestHeaders.getProxyAuthorization());
    }

    protected boolean includeAuthorityInRequestLine()
    {
        return false;
    }


    public (HttpURLConnectionImpl httpurlconnectionimpl, String s, RawHeaders rawheaders, Connection connection, RetryableOutputStream retryableoutputstream)
        throws IOException
    {
        super(httpurlconnectionimpl, s, rawheaders, connection, retryableoutputstream);
        SSLSocket sslsocket;
        if (connection != null)
        {
            sslsocket = (SSLSocket)connection.getSocket();
        } else
        {
            sslsocket = null;
        }
        sslSocket = sslsocket;
    }
}
