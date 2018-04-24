// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            RawHeaders, HeaderParser, HttpDate

public final class RequestHeaders
{

    private String acceptEncoding;
    private String connection;
    private int contentLength;
    private String contentType;
    private boolean hasAuthorization;
    private final RawHeaders headers;
    private String host;
    private String ifModifiedSince;
    private String ifNoneMatch;
    private int maxAgeSeconds;
    private int maxStaleSeconds;
    private int minFreshSeconds;
    private boolean noCache;
    private boolean onlyIfCached;
    private String proxyAuthorization;
    private String transferEncoding;
    private final URI uri;
    private String userAgent;

    public RequestHeaders(URI uri1, RawHeaders rawheaders)
    {
        maxAgeSeconds = -1;
        maxStaleSeconds = -1;
        minFreshSeconds = -1;
        contentLength = -1;
        uri = uri1;
        headers = rawheaders;
        HeaderParser.CacheControlHandler cachecontrolhandler = new HeaderParser.CacheControlHandler() {

            final RequestHeaders this$0;

            public void handle(String s2, String s3)
            {
                if ("no-cache".equalsIgnoreCase(s2))
                {
                    noCache = true;
                } else
                {
                    if ("max-age".equalsIgnoreCase(s2))
                    {
                        maxAgeSeconds = HeaderParser.parseSeconds(s3);
                        return;
                    }
                    if ("max-stale".equalsIgnoreCase(s2))
                    {
                        maxStaleSeconds = HeaderParser.parseSeconds(s3);
                        return;
                    }
                    if ("min-fresh".equalsIgnoreCase(s2))
                    {
                        minFreshSeconds = HeaderParser.parseSeconds(s3);
                        return;
                    }
                    if ("only-if-cached".equalsIgnoreCase(s2))
                    {
                        onlyIfCached = true;
                        return;
                    }
                }
            }

            
            {
                this$0 = RequestHeaders.this;
                super();
            }
        };
        int i = 0;
        while (i < rawheaders.length()) 
        {
            String s = rawheaders.getFieldName(i);
            String s1 = rawheaders.getValue(i);
            if ("Cache-Control".equalsIgnoreCase(s))
            {
                HeaderParser.parseCacheControl(s1, cachecontrolhandler);
            } else
            if ("Pragma".equalsIgnoreCase(s))
            {
                if ("no-cache".equalsIgnoreCase(s1))
                {
                    noCache = true;
                }
            } else
            if ("If-None-Match".equalsIgnoreCase(s))
            {
                ifNoneMatch = s1;
            } else
            if ("If-Modified-Since".equalsIgnoreCase(s))
            {
                ifModifiedSince = s1;
            } else
            if ("Authorization".equalsIgnoreCase(s))
            {
                hasAuthorization = true;
            } else
            if ("Content-Length".equalsIgnoreCase(s))
            {
                try
                {
                    contentLength = Integer.parseInt(s1);
                }
                catch (NumberFormatException numberformatexception) { }
            } else
            if ("Transfer-Encoding".equalsIgnoreCase(s))
            {
                transferEncoding = s1;
            } else
            if ("User-Agent".equalsIgnoreCase(s))
            {
                userAgent = s1;
            } else
            if ("Host".equalsIgnoreCase(s))
            {
                host = s1;
            } else
            if ("Connection".equalsIgnoreCase(s))
            {
                connection = s1;
            } else
            if ("Accept-Encoding".equalsIgnoreCase(s))
            {
                acceptEncoding = s1;
            } else
            if ("Content-Type".equalsIgnoreCase(s))
            {
                contentType = s1;
            } else
            if ("Proxy-Authorization".equalsIgnoreCase(s))
            {
                proxyAuthorization = s1;
            }
            i++;
        }
    }

    public void addCookies(Map map)
    {
        Iterator iterator = map.entrySet().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String s = (String)entry.getKey();
            if ("Cookie".equalsIgnoreCase(s) || "Cookie2".equalsIgnoreCase(s))
            {
                headers.addAll(s, (List)entry.getValue());
            }
        } while (true);
    }

    public String getAcceptEncoding()
    {
        return acceptEncoding;
    }

    public String getConnection()
    {
        return connection;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public String getContentType()
    {
        return contentType;
    }

    public RawHeaders getHeaders()
    {
        return headers;
    }

    public String getHost()
    {
        return host;
    }

    public String getIfModifiedSince()
    {
        return ifModifiedSince;
    }

    public String getIfNoneMatch()
    {
        return ifNoneMatch;
    }

    public int getMaxAgeSeconds()
    {
        return maxAgeSeconds;
    }

    public int getMaxStaleSeconds()
    {
        return maxStaleSeconds;
    }

    public int getMinFreshSeconds()
    {
        return minFreshSeconds;
    }

    public String getProxyAuthorization()
    {
        return proxyAuthorization;
    }

    public String getTransferEncoding()
    {
        return transferEncoding;
    }

    public URI getUri()
    {
        return uri;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public boolean hasAuthorization()
    {
        return hasAuthorization;
    }

    public boolean hasConditions()
    {
        return ifModifiedSince != null || ifNoneMatch != null;
    }

    public boolean hasConnectionClose()
    {
        return "close".equalsIgnoreCase(connection);
    }

    public boolean isChunked()
    {
        return "chunked".equalsIgnoreCase(transferEncoding);
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public boolean isOnlyIfCached()
    {
        return onlyIfCached;
    }

    public void setAcceptEncoding(String s)
    {
        if (acceptEncoding != null)
        {
            headers.removeAll("Accept-Encoding");
        }
        headers.add("Accept-Encoding", s);
        acceptEncoding = s;
    }

    public void setChunked()
    {
        if (transferEncoding != null)
        {
            headers.removeAll("Transfer-Encoding");
        }
        headers.add("Transfer-Encoding", "chunked");
        transferEncoding = "chunked";
    }

    public void setConnection(String s)
    {
        if (connection != null)
        {
            headers.removeAll("Connection");
        }
        headers.add("Connection", s);
        connection = s;
    }

    public void setContentLength(int i)
    {
        if (contentLength != -1)
        {
            headers.removeAll("Content-Length");
        }
        headers.add("Content-Length", Integer.toString(i));
        contentLength = i;
    }

    public void setContentType(String s)
    {
        if (contentType != null)
        {
            headers.removeAll("Content-Type");
        }
        headers.add("Content-Type", s);
        contentType = s;
    }

    public void setHost(String s)
    {
        if (host != null)
        {
            headers.removeAll("Host");
        }
        headers.add("Host", s);
        host = s;
    }

    public void setIfModifiedSince(Date date)
    {
        if (ifModifiedSince != null)
        {
            headers.removeAll("If-Modified-Since");
        }
        String s = HttpDate.format(date);
        headers.add("If-Modified-Since", s);
        ifModifiedSince = s;
    }

    public void setIfNoneMatch(String s)
    {
        if (ifNoneMatch != null)
        {
            headers.removeAll("If-None-Match");
        }
        headers.add("If-None-Match", s);
        ifNoneMatch = s;
    }

    public void setUserAgent(String s)
    {
        if (userAgent != null)
        {
            headers.removeAll("User-Agent");
        }
        headers.add("User-Agent", s);
        userAgent = s;
    }


/*
    static boolean access$002(RequestHeaders requestheaders, boolean flag)
    {
        requestheaders.noCache = flag;
        return flag;
    }

*/


/*
    static int access$102(RequestHeaders requestheaders, int i)
    {
        requestheaders.maxAgeSeconds = i;
        return i;
    }

*/


/*
    static int access$202(RequestHeaders requestheaders, int i)
    {
        requestheaders.maxStaleSeconds = i;
        return i;
    }

*/


/*
    static int access$302(RequestHeaders requestheaders, int i)
    {
        requestheaders.minFreshSeconds = i;
        return i;
    }

*/


/*
    static boolean access$402(RequestHeaders requestheaders, boolean flag)
    {
        requestheaders.onlyIfCached = flag;
        return flag;
    }

*/
}
