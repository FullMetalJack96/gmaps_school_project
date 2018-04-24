// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.AbstractOutputStream;
import com.squareup.okhttp.internal.FaultRecoveringOutputStream;
import com.squareup.okhttp.internal.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketPermission;
import java.net.URL;
import java.security.Permission;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            RawHeaders, HttpEngine, RetryableOutputStream, RouteSelector, 
//            ResponseHeaders, HttpAuthenticator, OkResponseCache

public class HttpURLConnectionImpl extends HttpURLConnection
{
    static final class Retry extends Enum
    {

        private static final Retry $VALUES[];
        public static final Retry DIFFERENT_CONNECTION;
        public static final Retry NONE;
        public static final Retry SAME_CONNECTION;

        public static Retry valueOf(String s)
        {
            return (Retry)Enum.valueOf(com/squareup/okhttp/internal/http/HttpURLConnectionImpl$Retry, s);
        }

        public static Retry[] values()
        {
            return (Retry[])$VALUES.clone();
        }

        static 
        {
            NONE = new Retry("NONE", 0);
            SAME_CONNECTION = new Retry("SAME_CONNECTION", 1);
            DIFFERENT_CONNECTION = new Retry("DIFFERENT_CONNECTION", 2);
            Retry aretry[] = new Retry[3];
            aretry[0] = NONE;
            aretry[1] = SAME_CONNECTION;
            aretry[2] = DIFFERENT_CONNECTION;
            $VALUES = aretry;
        }

        private Retry(String s, int i)
        {
            super(s, i);
        }
    }


    static final int HTTP_TEMP_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final int MAX_REPLAY_BUFFER_LENGTH = 8192;
    final ConnectionPool connectionPool;
    final CookieHandler cookieHandler;
    final Set failedRoutes;
    private FaultRecoveringOutputStream faultRecoveringRequestBody;
    private final boolean followProtocolRedirects;
    HostnameVerifier hostnameVerifier;
    protected HttpEngine httpEngine;
    protected IOException httpEngineFailure;
    final ProxySelector proxySelector;
    private final RawHeaders rawRequestHeaders = new RawHeaders();
    private int redirectionCount;
    final Proxy requestedProxy;
    final OkResponseCache responseCache;
    SSLSocketFactory sslSocketFactory;

    public HttpURLConnectionImpl(URL url, OkHttpClient okhttpclient, OkResponseCache okresponsecache, Set set)
    {
        super(url);
        followProtocolRedirects = okhttpclient.getFollowProtocolRedirects();
        failedRoutes = set;
        requestedProxy = okhttpclient.getProxy();
        proxySelector = okhttpclient.getProxySelector();
        cookieHandler = okhttpclient.getCookieHandler();
        connectionPool = okhttpclient.getConnectionPool();
        sslSocketFactory = okhttpclient.getSslSocketFactory();
        hostnameVerifier = okhttpclient.getHostnameVerifier();
        responseCache = okresponsecache;
    }

    private boolean execute(boolean flag)
        throws IOException
    {
        try
        {
            httpEngine.sendRequest();
        }
        catch (IOException ioexception)
        {
            if (handleFailure(ioexception))
            {
                return false;
            } else
            {
                throw ioexception;
            }
        }
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_18;
        }
        httpEngine.readResponse();
        return true;
    }

    private HttpEngine getResponse()
        throws IOException
    {
        initHttpEngine();
        if (httpEngine.hasResponse())
        {
            return httpEngine;
        }
        do
        {
            while (!execute(true)) ;
            Retry retry = processResponseHeaders();
            if (retry == Retry.NONE)
            {
                httpEngine.automaticallyReleaseConnectionToPool();
                return httpEngine;
            }
            String s = method;
            OutputStream outputstream = httpEngine.getRequestBody();
            int i = getResponseCode();
            if (i == 300 || i == 301 || i == 302 || i == 303)
            {
                s = "GET";
                outputstream = null;
            }
            if (outputstream != null && !(outputstream instanceof RetryableOutputStream))
            {
                throw new HttpRetryException("Cannot retry streamed HTTP body", httpEngine.getResponseCode());
            }
            if (retry == Retry.DIFFERENT_CONNECTION)
            {
                httpEngine.automaticallyReleaseConnectionToPool();
            }
            httpEngine.release(false);
            httpEngine = newHttpEngine(s, rawRequestHeaders, httpEngine.getConnection(), (RetryableOutputStream)outputstream);
        } while (true);
    }

    private boolean handleFailure(IOException ioexception)
        throws IOException
    {
        RouteSelector routeselector = httpEngine.routeSelector;
        if (routeselector != null && httpEngine.connection != null)
        {
            routeselector.connectFailed(httpEngine.connection, ioexception);
        }
        OutputStream outputstream = httpEngine.getRequestBody();
        boolean flag;
        if (outputstream == null || (outputstream instanceof RetryableOutputStream) || faultRecoveringRequestBody != null && faultRecoveringRequestBody.isRecoverable())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        if (routeselector == null && httpEngine.connection == null || routeselector != null && !routeselector.hasNext() || !isRecoverable(ioexception) || !flag)
        {
            httpEngineFailure = ioexception;
            return false;
        }
        httpEngine.release(true);
        RetryableOutputStream retryableoutputstream;
        if (outputstream instanceof RetryableOutputStream)
        {
            retryableoutputstream = (RetryableOutputStream)outputstream;
        } else
        {
            retryableoutputstream = null;
        }
        httpEngine = newHttpEngine(method, rawRequestHeaders, null, retryableoutputstream);
        httpEngine.routeSelector = routeselector;
        if (faultRecoveringRequestBody != null && faultRecoveringRequestBody.isRecoverable())
        {
            httpEngine.sendRequest();
            faultRecoveringRequestBody.replaceStream(httpEngine.getRequestBody());
        }
        return true;
    }

    private void initHttpEngine()
        throws IOException
    {
        if (httpEngineFailure != null)
        {
            throw httpEngineFailure;
        }
        if (httpEngine != null)
        {
            return;
        }
        connected = true;
        if (!doOutput) goto _L2; else goto _L1
_L1:
        if (!method.equals("GET")) goto _L4; else goto _L3
_L3:
        method = "POST";
_L2:
        httpEngine = newHttpEngine(method, rawRequestHeaders, null, null);
        return;
        IOException ioexception;
        ioexception;
        httpEngineFailure = ioexception;
        throw ioexception;
_L4:
        if (method.equals("POST") || method.equals("PUT")) goto _L2; else goto _L5
_L5:
        throw new ProtocolException((new StringBuilder()).append(method).append(" does not support writing").toString());
    }

    private boolean isRecoverable(IOException ioexception)
    {
        boolean flag;
        boolean flag1;
        if ((ioexception instanceof SSLHandshakeException) && (ioexception.getCause() instanceof CertificateException))
        {
            flag = true;
        } else
        {
            flag = false;
        }
        flag1 = ioexception instanceof ProtocolException;
        return !flag && !flag1;
    }

    private HttpEngine newHttpEngine(String s, RawHeaders rawheaders, Connection connection, RetryableOutputStream retryableoutputstream)
        throws IOException
    {
        if (url.getProtocol().equals("http"))
        {
            return new HttpEngine(this, s, rawheaders, connection, retryableoutputstream);
        }
        if (url.getProtocol().equals("https"))
        {
            return new HttpsURLConnectionImpl.HttpsEngine(this, s, rawheaders, connection, retryableoutputstream);
        } else
        {
            throw new AssertionError();
        }
    }

    private Retry processResponseHeaders()
        throws IOException
    {
        Proxy proxy;
        int i;
        if (httpEngine.connection != null)
        {
            proxy = httpEngine.connection.getRoute().getProxy();
        } else
        {
            proxy = requestedProxy;
        }
        i = getResponseCode();
        switch (i)
        {
        default:
            return Retry.NONE;

        case 407: 
            if (proxy.type() != java.net.Proxy.Type.HTTP)
            {
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }
            // fall through

        case 401: 
            if (HttpAuthenticator.processAuthHeader(getResponseCode(), httpEngine.getResponseHeaders().getHeaders(), rawRequestHeaders, proxy, this.url))
            {
                return Retry.SAME_CONNECTION;
            } else
            {
                return Retry.NONE;
            }

        case 300: 
        case 301: 
        case 302: 
        case 303: 
        case 307: 
            break;
        }
        if (!getInstanceFollowRedirects())
        {
            return Retry.NONE;
        }
        int j = 1 + redirectionCount;
        redirectionCount = j;
        if (j > 20)
        {
            throw new ProtocolException((new StringBuilder()).append("Too many redirects: ").append(redirectionCount).toString());
        }
        if (i == 307 && !method.equals("GET") && !method.equals("HEAD"))
        {
            return Retry.NONE;
        }
        String s = getHeaderField("Location");
        if (s == null)
        {
            return Retry.NONE;
        }
        URL url = this.url;
        this.url = new URL(url, s);
        if (!this.url.getProtocol().equals("https") && !this.url.getProtocol().equals("http"))
        {
            return Retry.NONE;
        }
        boolean flag = url.getProtocol().equals(this.url.getProtocol());
        if (!flag && !followProtocolRedirects)
        {
            return Retry.NONE;
        }
        boolean flag1 = url.getHost().equals(this.url.getHost());
        boolean flag2;
        if (Util.getEffectivePort(url) == Util.getEffectivePort(this.url))
        {
            flag2 = true;
        } else
        {
            flag2 = false;
        }
        if (flag1 && flag2 && flag)
        {
            return Retry.SAME_CONNECTION;
        } else
        {
            return Retry.DIFFERENT_CONNECTION;
        }
    }

    public final void addRequestProperty(String s, String s1)
    {
        if (connected)
        {
            throw new IllegalStateException("Cannot add request property after connection is made");
        }
        if (s == null)
        {
            throw new NullPointerException("field == null");
        } else
        {
            rawRequestHeaders.add(s, s1);
            return;
        }
    }

    public final void connect()
        throws IOException
    {
        initHttpEngine();
        while (!execute(false)) ;
    }

    public final void disconnect()
    {
        if (httpEngine != null)
        {
            if (httpEngine.hasResponse())
            {
                Util.closeQuietly(httpEngine.getResponseBody());
            }
            httpEngine.release(true);
        }
    }

    final int getChunkLength()
    {
        return chunkLength;
    }

    public final InputStream getErrorStream()
    {
        HttpEngine httpengine;
        boolean flag;
        InputStream inputstream;
        int i;
        InputStream inputstream1;
        try
        {
            httpengine = getResponse();
            flag = httpengine.hasResponseBody();
        }
        catch (IOException ioexception)
        {
            return null;
        }
        inputstream = null;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_44;
        }
        i = httpengine.getResponseCode();
        inputstream = null;
        if (i < 400)
        {
            break MISSING_BLOCK_LABEL_44;
        }
        inputstream1 = httpengine.getResponseBody();
        inputstream = inputstream1;
        return inputstream;
    }

    Set getFailedRoutes()
    {
        return failedRoutes;
    }

    final int getFixedContentLength()
    {
        return fixedContentLength;
    }

    public final String getHeaderField(int i)
    {
        String s;
        try
        {
            s = getResponse().getResponseHeaders().getHeaders().getValue(i);
        }
        catch (IOException ioexception)
        {
            return null;
        }
        return s;
    }

    public final String getHeaderField(String s)
    {
        RawHeaders rawheaders;
        String s1;
        try
        {
            rawheaders = getResponse().getResponseHeaders().getHeaders();
        }
        catch (IOException ioexception)
        {
            return null;
        }
        if (s != null)
        {
            break MISSING_BLOCK_LABEL_20;
        }
        return rawheaders.getStatusLine();
        s1 = rawheaders.get(s);
        return s1;
    }

    public final String getHeaderFieldKey(int i)
    {
        String s;
        try
        {
            s = getResponse().getResponseHeaders().getHeaders().getFieldName(i);
        }
        catch (IOException ioexception)
        {
            return null;
        }
        return s;
    }

    public final Map getHeaderFields()
    {
        Map map;
        try
        {
            map = getResponse().getResponseHeaders().getHeaders().toMultimap(true);
        }
        catch (IOException ioexception)
        {
            return null;
        }
        return map;
    }

    protected HttpURLConnection getHttpConnectionToCache()
    {
        return this;
    }

    public HttpEngine getHttpEngine()
    {
        return httpEngine;
    }

    public final InputStream getInputStream()
        throws IOException
    {
        if (!doInput)
        {
            throw new ProtocolException("This protocol does not support input");
        }
        HttpEngine httpengine = getResponse();
        if (getResponseCode() >= 400)
        {
            throw new FileNotFoundException(url.toString());
        }
        InputStream inputstream = httpengine.getResponseBody();
        if (inputstream == null)
        {
            throw new ProtocolException((new StringBuilder()).append("No response body exists; responseCode=").append(getResponseCode()).toString());
        } else
        {
            return inputstream;
        }
    }

    public final OutputStream getOutputStream()
        throws IOException
    {
        connect();
        OutputStream outputstream = httpEngine.getRequestBody();
        if (outputstream == null)
        {
            throw new ProtocolException((new StringBuilder()).append("method does not support a request body: ").append(method).toString());
        }
        if (httpEngine.hasResponse())
        {
            throw new ProtocolException("cannot write request body after response has been read");
        }
        if (faultRecoveringRequestBody == null)
        {
            faultRecoveringRequestBody = new FaultRecoveringOutputStream(8192, outputstream) {

                final HttpURLConnectionImpl this$0;

                protected OutputStream replacementStream(IOException ioexception)
                    throws IOException
                {
                    if ((httpEngine.getRequestBody() instanceof AbstractOutputStream) && ((AbstractOutputStream)httpEngine.getRequestBody()).isClosed())
                    {
                        return null;
                    }
                    if (handleFailure(ioexception))
                    {
                        return httpEngine.getRequestBody();
                    } else
                    {
                        return null;
                    }
                }

            
            {
                this$0 = HttpURLConnectionImpl.this;
                super(i, outputstream);
            }
            };
        }
        return faultRecoveringRequestBody;
    }

    public final Permission getPermission()
        throws IOException
    {
        String s = getURL().getHost();
        int i = Util.getEffectivePort(getURL());
        if (usingProxy())
        {
            InetSocketAddress inetsocketaddress = (InetSocketAddress)requestedProxy.address();
            s = inetsocketaddress.getHostName();
            i = inetsocketaddress.getPort();
        }
        return new SocketPermission((new StringBuilder()).append(s).append(":").append(i).toString(), "connect, resolve");
    }

    public final Map getRequestProperties()
    {
        if (connected)
        {
            throw new IllegalStateException("Cannot access request header fields after connection is set");
        } else
        {
            return rawRequestHeaders.toMultimap(false);
        }
    }

    public final String getRequestProperty(String s)
    {
        if (s == null)
        {
            return null;
        } else
        {
            return rawRequestHeaders.get(s);
        }
    }

    public final int getResponseCode()
        throws IOException
    {
        return getResponse().getResponseCode();
    }

    public String getResponseMessage()
        throws IOException
    {
        return getResponse().getResponseHeaders().getHeaders().getResponseMessage();
    }

    public final void setRequestProperty(String s, String s1)
    {
        if (connected)
        {
            throw new IllegalStateException("Cannot set request property after connection is made");
        }
        if (s == null)
        {
            throw new NullPointerException("field == null");
        } else
        {
            rawRequestHeaders.set(s, s1);
            return;
        }
    }

    public final boolean usingProxy()
    {
        return requestedProxy != null && requestedProxy.type() != java.net.Proxy.Type.DIRECT;
    }

}
