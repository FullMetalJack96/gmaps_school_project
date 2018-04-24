// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp;

import com.squareup.okhttp.internal.http.HttpURLConnectionImpl;
import com.squareup.okhttp.internal.http.HttpsURLConnectionImpl;
import com.squareup.okhttp.internal.http.OkResponseCache;
import com.squareup.okhttp.internal.http.OkResponseCacheAdapter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.ResponseCache;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

// Referenced classes of package com.squareup.okhttp:
//            ConnectionPool, HttpResponseCache

public final class OkHttpClient
{

    private ConnectionPool connectionPool;
    private CookieHandler cookieHandler;
    private Set failedRoutes;
    private boolean followProtocolRedirects;
    private HostnameVerifier hostnameVerifier;
    private Proxy proxy;
    private ProxySelector proxySelector;
    private ResponseCache responseCache;
    private SSLSocketFactory sslSocketFactory;

    public OkHttpClient()
    {
        failedRoutes = Collections.synchronizedSet(new LinkedHashSet());
        followProtocolRedirects = true;
    }

    private OkHttpClient copyWithDefaults()
    {
        OkHttpClient okhttpclient = new OkHttpClient();
        okhttpclient.proxy = proxy;
        okhttpclient.failedRoutes = failedRoutes;
        ProxySelector proxyselector;
        CookieHandler cookiehandler;
        ResponseCache responsecache;
        SSLSocketFactory sslsocketfactory;
        HostnameVerifier hostnameverifier;
        ConnectionPool connectionpool;
        if (proxySelector != null)
        {
            proxyselector = proxySelector;
        } else
        {
            proxyselector = ProxySelector.getDefault();
        }
        okhttpclient.proxySelector = proxyselector;
        if (cookieHandler != null)
        {
            cookiehandler = cookieHandler;
        } else
        {
            cookiehandler = CookieHandler.getDefault();
        }
        okhttpclient.cookieHandler = cookiehandler;
        if (responseCache != null)
        {
            responsecache = responseCache;
        } else
        {
            responsecache = ResponseCache.getDefault();
        }
        okhttpclient.responseCache = responsecache;
        if (sslSocketFactory != null)
        {
            sslsocketfactory = sslSocketFactory;
        } else
        {
            sslsocketfactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        }
        okhttpclient.sslSocketFactory = sslsocketfactory;
        if (hostnameVerifier != null)
        {
            hostnameverifier = hostnameVerifier;
        } else
        {
            hostnameverifier = HttpsURLConnection.getDefaultHostnameVerifier();
        }
        okhttpclient.hostnameVerifier = hostnameverifier;
        if (connectionPool != null)
        {
            connectionpool = connectionPool;
        } else
        {
            connectionpool = ConnectionPool.getDefault();
        }
        okhttpclient.connectionPool = connectionpool;
        okhttpclient.followProtocolRedirects = followProtocolRedirects;
        return okhttpclient;
    }

    private OkResponseCache okResponseCache()
    {
        if (responseCache instanceof HttpResponseCache)
        {
            return ((HttpResponseCache)responseCache).okResponseCache;
        }
        if (responseCache != null)
        {
            return new OkResponseCacheAdapter(responseCache);
        } else
        {
            return null;
        }
    }

    public ConnectionPool getConnectionPool()
    {
        return connectionPool;
    }

    public CookieHandler getCookieHandler()
    {
        return cookieHandler;
    }

    public boolean getFollowProtocolRedirects()
    {
        return followProtocolRedirects;
    }

    public HostnameVerifier getHostnameVerifier()
    {
        return hostnameVerifier;
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public ProxySelector getProxySelector()
    {
        return proxySelector;
    }

    public ResponseCache getResponseCache()
    {
        return responseCache;
    }

    public SSLSocketFactory getSslSocketFactory()
    {
        return sslSocketFactory;
    }

    public HttpURLConnection open(URL url)
    {
        String s = url.getProtocol();
        OkHttpClient okhttpclient = copyWithDefaults();
        if (s.equals("http"))
        {
            return new HttpURLConnectionImpl(url, okhttpclient, okhttpclient.okResponseCache(), okhttpclient.failedRoutes);
        }
        if (s.equals("https"))
        {
            return new HttpsURLConnectionImpl(url, okhttpclient, okhttpclient.okResponseCache(), okhttpclient.failedRoutes);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unexpected protocol: ").append(s).toString());
        }
    }

    public OkHttpClient setConnectionPool(ConnectionPool connectionpool)
    {
        connectionPool = connectionpool;
        return this;
    }

    public OkHttpClient setCookieHandler(CookieHandler cookiehandler)
    {
        cookieHandler = cookiehandler;
        return this;
    }

    public OkHttpClient setFollowProtocolRedirects(boolean flag)
    {
        followProtocolRedirects = flag;
        return this;
    }

    public OkHttpClient setHostnameVerifier(HostnameVerifier hostnameverifier)
    {
        hostnameVerifier = hostnameverifier;
        return this;
    }

    public OkHttpClient setProxy(Proxy proxy1)
    {
        proxy = proxy1;
        return this;
    }

    public OkHttpClient setProxySelector(ProxySelector proxyselector)
    {
        proxySelector = proxyselector;
        return this;
    }

    public OkHttpClient setResponseCache(ResponseCache responsecache)
    {
        responseCache = responsecache;
        return this;
    }

    public OkHttpClient setSSLSocketFactory(SSLSocketFactory sslsocketfactory)
    {
        sslSocketFactory = sslsocketfactory;
        return this;
    }
}
