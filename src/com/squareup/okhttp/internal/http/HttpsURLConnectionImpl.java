// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.TunnelRequest;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpEngine, OkResponseCache, HttpURLConnectionImpl, RequestHeaders, 
//            RawHeaders, RetryableOutputStream

public final class HttpsURLConnectionImpl extends HttpsURLConnection
{
    private final class HttpUrlConnectionDelegate extends HttpURLConnectionImpl
    {

        final HttpsURLConnectionImpl this$0;

        protected HttpURLConnection getHttpConnectionToCache()
        {
            return HttpsURLConnectionImpl.this;
        }

        public SecureCacheResponse getSecureCacheResponse()
        {
            if (httpEngine instanceof HttpsEngine)
            {
                return (SecureCacheResponse)httpEngine.getCacheResponse();
            } else
            {
                return null;
            }
        }

        private HttpUrlConnectionDelegate(URL url, OkHttpClient okhttpclient, OkResponseCache okresponsecache, Set set)
        {
            this$0 = HttpsURLConnectionImpl.this;
            super(url, okhttpclient, okresponsecache, set);
        }

    }

    public static final class HttpsEngine extends HttpEngine
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


        public HttpsEngine(HttpURLConnectionImpl httpurlconnectionimpl, String s, RawHeaders rawheaders, Connection connection, RetryableOutputStream retryableoutputstream)
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


    private final HttpUrlConnectionDelegate _flddelegate;

    public HttpsURLConnectionImpl(URL url, OkHttpClient okhttpclient, OkResponseCache okresponsecache, Set set)
    {
        super(url);
        _flddelegate = new HttpUrlConnectionDelegate(url, okhttpclient, okresponsecache, set);
    }

    private SSLSocket getSslSocket()
    {
        if (_flddelegate.httpEngine == null || _flddelegate.httpEngine.sentRequestMillis == -1L)
        {
            throw new IllegalStateException("Connection has not yet been established");
        }
        if (_flddelegate.httpEngine instanceof HttpsEngine)
        {
            return ((HttpsEngine)_flddelegate.httpEngine).sslSocket;
        } else
        {
            return null;
        }
    }

    public void addRequestProperty(String s, String s1)
    {
        _flddelegate.addRequestProperty(s, s1);
    }

    public void connect()
        throws IOException
    {
        connected = true;
        _flddelegate.connect();
    }

    public void disconnect()
    {
        _flddelegate.disconnect();
    }

    public boolean getAllowUserInteraction()
    {
        return _flddelegate.getAllowUserInteraction();
    }

    public String getCipherSuite()
    {
        SecureCacheResponse securecacheresponse = _flddelegate.getSecureCacheResponse();
        if (securecacheresponse != null)
        {
            return securecacheresponse.getCipherSuite();
        }
        SSLSocket sslsocket = getSslSocket();
        if (sslsocket != null)
        {
            return sslsocket.getSession().getCipherSuite();
        } else
        {
            return null;
        }
    }

    public int getConnectTimeout()
    {
        return _flddelegate.getConnectTimeout();
    }

    public Object getContent()
        throws IOException
    {
        return _flddelegate.getContent();
    }

    public Object getContent(Class aclass[])
        throws IOException
    {
        return _flddelegate.getContent(aclass);
    }

    public String getContentEncoding()
    {
        return _flddelegate.getContentEncoding();
    }

    public int getContentLength()
    {
        return _flddelegate.getContentLength();
    }

    public String getContentType()
    {
        return _flddelegate.getContentType();
    }

    public long getDate()
    {
        return _flddelegate.getDate();
    }

    public boolean getDefaultUseCaches()
    {
        return _flddelegate.getDefaultUseCaches();
    }

    public boolean getDoInput()
    {
        return _flddelegate.getDoInput();
    }

    public boolean getDoOutput()
    {
        return _flddelegate.getDoOutput();
    }

    public InputStream getErrorStream()
    {
        return _flddelegate.getErrorStream();
    }

    public long getExpiration()
    {
        return _flddelegate.getExpiration();
    }

    public String getHeaderField(int i)
    {
        return _flddelegate.getHeaderField(i);
    }

    public String getHeaderField(String s)
    {
        return _flddelegate.getHeaderField(s);
    }

    public long getHeaderFieldDate(String s, long l)
    {
        return _flddelegate.getHeaderFieldDate(s, l);
    }

    public int getHeaderFieldInt(String s, int i)
    {
        return _flddelegate.getHeaderFieldInt(s, i);
    }

    public String getHeaderFieldKey(int i)
    {
        return _flddelegate.getHeaderFieldKey(i);
    }

    public Map getHeaderFields()
    {
        return _flddelegate.getHeaderFields();
    }

    public HostnameVerifier getHostnameVerifier()
    {
        return _flddelegate.hostnameVerifier;
    }

    public HttpEngine getHttpEngine()
    {
        return _flddelegate.getHttpEngine();
    }

    public long getIfModifiedSince()
    {
        return _flddelegate.getIfModifiedSince();
    }

    public InputStream getInputStream()
        throws IOException
    {
        return _flddelegate.getInputStream();
    }

    public boolean getInstanceFollowRedirects()
    {
        return _flddelegate.getInstanceFollowRedirects();
    }

    public long getLastModified()
    {
        return _flddelegate.getLastModified();
    }

    public Certificate[] getLocalCertificates()
    {
        SecureCacheResponse securecacheresponse = _flddelegate.getSecureCacheResponse();
        Certificate acertificate[];
        if (securecacheresponse != null)
        {
            List list = securecacheresponse.getLocalCertificateChain();
            acertificate = null;
            if (list != null)
            {
                acertificate = (Certificate[])list.toArray(new Certificate[list.size()]);
            }
        } else
        {
            SSLSocket sslsocket = getSslSocket();
            acertificate = null;
            if (sslsocket != null)
            {
                return sslsocket.getSession().getLocalCertificates();
            }
        }
        return acertificate;
    }

    public Principal getLocalPrincipal()
    {
        SecureCacheResponse securecacheresponse = _flddelegate.getSecureCacheResponse();
        if (securecacheresponse != null)
        {
            return securecacheresponse.getLocalPrincipal();
        }
        SSLSocket sslsocket = getSslSocket();
        if (sslsocket != null)
        {
            return sslsocket.getSession().getLocalPrincipal();
        } else
        {
            return null;
        }
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        return _flddelegate.getOutputStream();
    }

    public Principal getPeerPrincipal()
        throws SSLPeerUnverifiedException
    {
        SecureCacheResponse securecacheresponse = _flddelegate.getSecureCacheResponse();
        if (securecacheresponse != null)
        {
            return securecacheresponse.getPeerPrincipal();
        }
        SSLSocket sslsocket = getSslSocket();
        if (sslsocket != null)
        {
            return sslsocket.getSession().getPeerPrincipal();
        } else
        {
            return null;
        }
    }

    public Permission getPermission()
        throws IOException
    {
        return _flddelegate.getPermission();
    }

    public int getReadTimeout()
    {
        return _flddelegate.getReadTimeout();
    }

    public String getRequestMethod()
    {
        return _flddelegate.getRequestMethod();
    }

    public Map getRequestProperties()
    {
        return _flddelegate.getRequestProperties();
    }

    public String getRequestProperty(String s)
    {
        return _flddelegate.getRequestProperty(s);
    }

    public int getResponseCode()
        throws IOException
    {
        return _flddelegate.getResponseCode();
    }

    public String getResponseMessage()
        throws IOException
    {
        return _flddelegate.getResponseMessage();
    }

    public SSLSocketFactory getSSLSocketFactory()
    {
        return _flddelegate.sslSocketFactory;
    }

    public Certificate[] getServerCertificates()
        throws SSLPeerUnverifiedException
    {
        SecureCacheResponse securecacheresponse = _flddelegate.getSecureCacheResponse();
        Certificate acertificate[];
        if (securecacheresponse != null)
        {
            List list = securecacheresponse.getServerCertificateChain();
            acertificate = null;
            if (list != null)
            {
                acertificate = (Certificate[])list.toArray(new Certificate[list.size()]);
            }
        } else
        {
            SSLSocket sslsocket = getSslSocket();
            acertificate = null;
            if (sslsocket != null)
            {
                return sslsocket.getSession().getPeerCertificates();
            }
        }
        return acertificate;
    }

    public URL getURL()
    {
        return _flddelegate.getURL();
    }

    public boolean getUseCaches()
    {
        return _flddelegate.getUseCaches();
    }

    public void setAllowUserInteraction(boolean flag)
    {
        _flddelegate.setAllowUserInteraction(flag);
    }

    public void setChunkedStreamingMode(int i)
    {
        _flddelegate.setChunkedStreamingMode(i);
    }

    public void setConnectTimeout(int i)
    {
        _flddelegate.setConnectTimeout(i);
    }

    public void setDefaultUseCaches(boolean flag)
    {
        _flddelegate.setDefaultUseCaches(flag);
    }

    public void setDoInput(boolean flag)
    {
        _flddelegate.setDoInput(flag);
    }

    public void setDoOutput(boolean flag)
    {
        _flddelegate.setDoOutput(flag);
    }

    public void setFixedLengthStreamingMode(int i)
    {
        _flddelegate.setFixedLengthStreamingMode(i);
    }

    public void setHostnameVerifier(HostnameVerifier hostnameverifier)
    {
        _flddelegate.hostnameVerifier = hostnameverifier;
    }

    public void setIfModifiedSince(long l)
    {
        _flddelegate.setIfModifiedSince(l);
    }

    public void setInstanceFollowRedirects(boolean flag)
    {
        _flddelegate.setInstanceFollowRedirects(flag);
    }

    public void setReadTimeout(int i)
    {
        _flddelegate.setReadTimeout(i);
    }

    public void setRequestMethod(String s)
        throws ProtocolException
    {
        _flddelegate.setRequestMethod(s);
    }

    public void setRequestProperty(String s, String s1)
    {
        _flddelegate.setRequestProperty(s, s1);
    }

    public void setSSLSocketFactory(SSLSocketFactory sslsocketfactory)
    {
        _flddelegate.sslSocketFactory = sslsocketfactory;
    }

    public void setUseCaches(boolean flag)
    {
        _flddelegate.setUseCaches(flag);
    }

    public String toString()
    {
        return _flddelegate.toString();
    }

    public boolean usingProxy()
    {
        return _flddelegate.usingProxy();
    }
}
