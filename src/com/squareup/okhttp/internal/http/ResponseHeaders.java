// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.ResponseSource;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            RawHeaders, HeaderParser, HttpDate, RequestHeaders

public final class ResponseHeaders
{

    private static final String RECEIVED_MILLIS = "X-Android-Received-Millis";
    static final String RESPONSE_SOURCE = "X-Android-Response-Source";
    private static final String SENT_MILLIS = "X-Android-Sent-Millis";
    private int ageSeconds;
    private String connection;
    private String contentEncoding;
    private int contentLength;
    private String etag;
    private Date expires;
    private final RawHeaders headers;
    private boolean isPublic;
    private Date lastModified;
    private int maxAgeSeconds;
    private boolean mustRevalidate;
    private boolean noCache;
    private boolean noStore;
    private long receivedResponseMillis;
    private int sMaxAgeSeconds;
    private long sentRequestMillis;
    private Date servedDate;
    private String transferEncoding;
    private final URI uri;
    private Set varyFields;

    public ResponseHeaders(URI uri1, RawHeaders rawheaders)
    {
        maxAgeSeconds = -1;
        sMaxAgeSeconds = -1;
        ageSeconds = -1;
        varyFields = Collections.emptySet();
        contentLength = -1;
        uri = uri1;
        headers = rawheaders;
        HeaderParser.CacheControlHandler cachecontrolhandler = new HeaderParser.CacheControlHandler() {

            final ResponseHeaders this$0;

            public void handle(String s3, String s4)
            {
                if ("no-cache".equalsIgnoreCase(s3))
                {
                    noCache = true;
                } else
                {
                    if ("no-store".equalsIgnoreCase(s3))
                    {
                        noStore = true;
                        return;
                    }
                    if ("max-age".equalsIgnoreCase(s3))
                    {
                        maxAgeSeconds = HeaderParser.parseSeconds(s4);
                        return;
                    }
                    if ("s-maxage".equalsIgnoreCase(s3))
                    {
                        sMaxAgeSeconds = HeaderParser.parseSeconds(s4);
                        return;
                    }
                    if ("public".equalsIgnoreCase(s3))
                    {
                        isPublic = true;
                        return;
                    }
                    if ("must-revalidate".equalsIgnoreCase(s3))
                    {
                        mustRevalidate = true;
                        return;
                    }
                }
            }

            
            {
                this$0 = ResponseHeaders.this;
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
            if ("Date".equalsIgnoreCase(s))
            {
                servedDate = HttpDate.parse(s1);
            } else
            if ("Expires".equalsIgnoreCase(s))
            {
                expires = HttpDate.parse(s1);
            } else
            if ("Last-Modified".equalsIgnoreCase(s))
            {
                lastModified = HttpDate.parse(s1);
            } else
            if ("ETag".equalsIgnoreCase(s))
            {
                etag = s1;
            } else
            if ("Pragma".equalsIgnoreCase(s))
            {
                if ("no-cache".equalsIgnoreCase(s1))
                {
                    noCache = true;
                }
            } else
            if ("Age".equalsIgnoreCase(s))
            {
                ageSeconds = HeaderParser.parseSeconds(s1);
            } else
            if ("Vary".equalsIgnoreCase(s))
            {
                if (varyFields.isEmpty())
                {
                    varyFields = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                }
                String as[] = s1.split(",");
                int j = as.length;
                int k = 0;
                while (k < j) 
                {
                    String s2 = as[k];
                    varyFields.add(s2.trim());
                    k++;
                }
            } else
            if ("Content-Encoding".equalsIgnoreCase(s))
            {
                contentEncoding = s1;
            } else
            if ("Transfer-Encoding".equalsIgnoreCase(s))
            {
                transferEncoding = s1;
            } else
            if ("Content-Length".equalsIgnoreCase(s))
            {
                try
                {
                    contentLength = Integer.parseInt(s1);
                }
                catch (NumberFormatException numberformatexception) { }
            } else
            if ("Connection".equalsIgnoreCase(s))
            {
                connection = s1;
            } else
            if ("X-Android-Sent-Millis".equalsIgnoreCase(s))
            {
                sentRequestMillis = Long.parseLong(s1);
            } else
            if ("X-Android-Received-Millis".equalsIgnoreCase(s))
            {
                receivedResponseMillis = Long.parseLong(s1);
            }
            i++;
        }
    }

    private long computeAge(long l)
    {
        long l1 = 0L;
        if (servedDate != null)
        {
            l1 = Math.max(l1, receivedResponseMillis - servedDate.getTime());
        }
        long l2;
        long l3;
        if (ageSeconds != -1)
        {
            l2 = Math.max(l1, TimeUnit.SECONDS.toMillis(ageSeconds));
        } else
        {
            l2 = l1;
        }
        l3 = receivedResponseMillis - sentRequestMillis;
        return (l - receivedResponseMillis) + (l2 + l3);
    }

    private long computeFreshnessLifetime()
    {
        long l = 0L;
        if (maxAgeSeconds != -1)
        {
            l = TimeUnit.SECONDS.toMillis(maxAgeSeconds);
        } else
        {
            if (expires != null)
            {
                long l3;
                long l4;
                if (servedDate != null)
                {
                    l3 = servedDate.getTime();
                } else
                {
                    l3 = receivedResponseMillis;
                }
                l4 = expires.getTime() - l3;
                if (l4 <= l)
                {
                    l4 = l;
                }
                return l4;
            }
            if (lastModified != null && uri.getRawQuery() == null)
            {
                long l1;
                long l2;
                if (servedDate != null)
                {
                    l1 = servedDate.getTime();
                } else
                {
                    l1 = sentRequestMillis;
                }
                l2 = l1 - lastModified.getTime();
                if (l2 > l)
                {
                    return l2 / 10L;
                }
            }
        }
        return l;
    }

    private static boolean isEndToEnd(String s)
    {
        return !"Connection".equalsIgnoreCase(s) && !"Keep-Alive".equalsIgnoreCase(s) && !"Proxy-Authenticate".equalsIgnoreCase(s) && !"Proxy-Authorization".equalsIgnoreCase(s) && !"TE".equalsIgnoreCase(s) && !"Trailers".equalsIgnoreCase(s) && !"Transfer-Encoding".equalsIgnoreCase(s) && !"Upgrade".equalsIgnoreCase(s);
    }

    private boolean isFreshnessLifetimeHeuristic()
    {
        return maxAgeSeconds == -1 && expires == null;
    }

    public ResponseSource chooseResponseSource(long l, RequestHeaders requestheaders)
    {
        if (!isCacheable(requestheaders))
        {
            return ResponseSource.NETWORK;
        }
        if (requestheaders.isNoCache() || requestheaders.hasConditions())
        {
            return ResponseSource.NETWORK;
        }
        long l1 = computeAge(l);
        long l2 = computeFreshnessLifetime();
        if (requestheaders.getMaxAgeSeconds() != -1)
        {
            l2 = Math.min(l2, TimeUnit.SECONDS.toMillis(requestheaders.getMaxAgeSeconds()));
        }
        long l3 = 0L;
        if (requestheaders.getMinFreshSeconds() != -1)
        {
            l3 = TimeUnit.SECONDS.toMillis(requestheaders.getMinFreshSeconds());
        }
        long l4 = 0L;
        if (!mustRevalidate && requestheaders.getMaxStaleSeconds() != -1)
        {
            l4 = TimeUnit.SECONDS.toMillis(requestheaders.getMaxStaleSeconds());
        }
        if (!noCache && l1 + l3 < l2 + l4)
        {
            if (l1 + l3 >= l2)
            {
                headers.add("Warning", "110 HttpURLConnection \"Response is stale\"");
            }
            if (l1 > 0x5265c00L && isFreshnessLifetimeHeuristic())
            {
                headers.add("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
            }
            return ResponseSource.CACHE;
        }
        if (lastModified != null)
        {
            requestheaders.setIfModifiedSince(lastModified);
        } else
        if (servedDate != null)
        {
            requestheaders.setIfModifiedSince(servedDate);
        }
        if (etag != null)
        {
            requestheaders.setIfNoneMatch(etag);
        }
        if (requestheaders.hasConditions())
        {
            return ResponseSource.CONDITIONAL_CACHE;
        } else
        {
            return ResponseSource.NETWORK;
        }
    }

    public ResponseHeaders combine(ResponseHeaders responseheaders)
        throws IOException
    {
        RawHeaders rawheaders = new RawHeaders();
        rawheaders.setStatusLine(headers.getStatusLine());
        int i = 0;
        do
        {
            if (i >= headers.length())
            {
                break;
            }
            String s1 = headers.getFieldName(i);
            String s2 = headers.getValue(i);
            if ((!"Warning".equals(s1) || !s2.startsWith("1")) && (!isEndToEnd(s1) || responseheaders.headers.get(s1) == null))
            {
                rawheaders.add(s1, s2);
            }
            i++;
        } while (true);
        for (int j = 0; j < responseheaders.headers.length(); j++)
        {
            String s = responseheaders.headers.getFieldName(j);
            if (isEndToEnd(s))
            {
                rawheaders.add(s, responseheaders.headers.getValue(j));
            }
        }

        return new ResponseHeaders(uri, rawheaders);
    }

    public String getConnection()
    {
        return connection;
    }

    public String getContentEncoding()
    {
        return contentEncoding;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public String getEtag()
    {
        return etag;
    }

    public Date getExpires()
    {
        return expires;
    }

    public RawHeaders getHeaders()
    {
        return headers;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public int getMaxAgeSeconds()
    {
        return maxAgeSeconds;
    }

    public int getSMaxAgeSeconds()
    {
        return sMaxAgeSeconds;
    }

    public Date getServedDate()
    {
        return servedDate;
    }

    public URI getUri()
    {
        return uri;
    }

    public Set getVaryFields()
    {
        return varyFields;
    }

    public boolean hasConnectionClose()
    {
        return "close".equalsIgnoreCase(connection);
    }

    public boolean hasVaryAll()
    {
        return varyFields.contains("*");
    }

    public boolean isCacheable(RequestHeaders requestheaders)
    {
        for (int i = headers.getResponseCode(); i != 200 && i != 203 && i != 300 && i != 301 && i != 410 || requestheaders.hasAuthorization() && !isPublic && !mustRevalidate && sMaxAgeSeconds == -1 || noStore;)
        {
            return false;
        }

        return true;
    }

    public boolean isChunked()
    {
        return "chunked".equalsIgnoreCase(transferEncoding);
    }

    public boolean isContentEncodingGzip()
    {
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    public boolean isMustRevalidate()
    {
        return mustRevalidate;
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public boolean isNoStore()
    {
        return noStore;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setLocalTimestamps(long l, long l1)
    {
        sentRequestMillis = l;
        headers.add("X-Android-Sent-Millis", Long.toString(l));
        receivedResponseMillis = l1;
        headers.add("X-Android-Received-Millis", Long.toString(l1));
    }

    public void setResponseSource(ResponseSource responsesource)
    {
        headers.set("X-Android-Response-Source", (new StringBuilder()).append(responsesource.toString()).append(" ").append(headers.getResponseCode()).toString());
    }

    public void stripContentEncoding()
    {
        contentEncoding = null;
        headers.removeAll("Content-Encoding");
    }

    public void stripContentLength()
    {
        contentLength = -1;
        headers.removeAll("Content-Length");
    }

    public boolean validate(ResponseHeaders responseheaders)
    {
        while (responseheaders.headers.getResponseCode() == 304 || lastModified != null && responseheaders.lastModified != null && responseheaders.lastModified.getTime() < lastModified.getTime()) 
        {
            return true;
        }
        return false;
    }

    public boolean varyMatches(Map map, Map map1)
    {
        for (Iterator iterator = varyFields.iterator(); iterator.hasNext();)
        {
            String s = (String)iterator.next();
            if (!Util.equal(map.get(s), map1.get(s)))
            {
                return false;
            }
        }

        return true;
    }


/*
    static boolean access$002(ResponseHeaders responseheaders, boolean flag)
    {
        responseheaders.noCache = flag;
        return flag;
    }

*/


/*
    static boolean access$102(ResponseHeaders responseheaders, boolean flag)
    {
        responseheaders.noStore = flag;
        return flag;
    }

*/


/*
    static int access$202(ResponseHeaders responseheaders, int i)
    {
        responseheaders.maxAgeSeconds = i;
        return i;
    }

*/


/*
    static int access$302(ResponseHeaders responseheaders, int i)
    {
        responseheaders.sMaxAgeSeconds = i;
        return i;
    }

*/


/*
    static boolean access$402(ResponseHeaders responseheaders, boolean flag)
    {
        responseheaders.isPublic = flag;
        return flag;
    }

*/


/*
    static boolean access$502(ResponseHeaders responseheaders, boolean flag)
    {
        responseheaders.mustRevalidate = flag;
        return flag;
    }

*/
}
