// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.spdy.SpdyStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.URI;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            Transport, UnknownLengthHttpInputStream, RawHeaders, HttpEngine, 
//            ResponseHeaders, RequestHeaders, HttpURLConnectionImpl, RetryableOutputStream

public final class SpdyTransport
    implements Transport
{

    private final HttpEngine httpEngine;
    private final SpdyConnection spdyConnection;
    private SpdyStream stream;

    public SpdyTransport(HttpEngine httpengine, SpdyConnection spdyconnection)
    {
        httpEngine = httpengine;
        spdyConnection = spdyconnection;
    }

    public OutputStream createRequestBody()
        throws IOException
    {
        writeRequestHeaders();
        return stream.getOutputStream();
    }

    public void flushRequest()
        throws IOException
    {
        stream.getOutputStream().close();
    }

    public InputStream getTransferStream(CacheRequest cacherequest)
        throws IOException
    {
        return new UnknownLengthHttpInputStream(stream.getInputStream(), cacherequest, httpEngine);
    }

    public boolean makeReusable(boolean flag, OutputStream outputstream, InputStream inputstream)
    {
label0:
        {
            if (flag)
            {
                if (stream == null)
                {
                    break label0;
                }
                stream.closeLater(5);
            }
            return true;
        }
        return false;
    }

    public ResponseHeaders readResponseHeaders()
        throws IOException
    {
        RawHeaders rawheaders = RawHeaders.fromNameValueBlock(stream.getResponseHeaders());
        rawheaders.computeResponseStatusLineFromSpdyHeaders();
        httpEngine.receiveHeaders(rawheaders);
        return new ResponseHeaders(httpEngine.uri, rawheaders);
    }

    public void writeRequestBody(RetryableOutputStream retryableoutputstream)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void writeRequestHeaders()
        throws IOException
    {
        if (stream != null)
        {
            return;
        }
        httpEngine.writingRequestHeaders();
        RawHeaders rawheaders = httpEngine.requestHeaders.getHeaders();
        String s;
        java.net.URL url;
        boolean flag;
        if (httpEngine.connection.getHttpMinorVersion() == 1)
        {
            s = "HTTP/1.1";
        } else
        {
            s = "HTTP/1.0";
        }
        url = httpEngine.policy.getURL();
        rawheaders.addSpdyRequestHeaders(httpEngine.method, HttpEngine.requestPath(url), s, HttpEngine.getOriginAddress(url), httpEngine.uri.getScheme());
        flag = httpEngine.hasRequestBody();
        stream = spdyConnection.newStream(rawheaders.toNameValueBlock(), flag, true);
        stream.setReadTimeout(httpEngine.policy.getReadTimeout());
    }
}
