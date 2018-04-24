// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp;

import com.squareup.okhttp.internal.Base64;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.StrictLineReader;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.HttpURLConnectionImpl;
import com.squareup.okhttp.internal.http.HttpsURLConnectionImpl;
import com.squareup.okhttp.internal.http.OkResponseCache;
import com.squareup.okhttp.internal.http.RawHeaders;
import com.squareup.okhttp.internal.http.RequestHeaders;
import com.squareup.okhttp.internal.http.ResponseHeaders;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.SecureCacheResponse;
import java.net.URI;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

// Referenced classes of package com.squareup.okhttp:
//            ResponseSource

public final class HttpResponseCache extends ResponseCache
{
    private static final class Entry
    {

        private final String cipherSuite;
        private final Certificate localCertificates[];
        private final Certificate peerCertificates[];
        private final String requestMethod;
        private final RawHeaders responseHeaders;
        private final String uri;
        private final RawHeaders varyHeaders;

        private boolean isHttps()
        {
            return uri.startsWith("https://");
        }

        private Certificate[] readCertArray(StrictLineReader strictlinereader)
            throws IOException
        {
            int i = strictlinereader.readInt();
            if (i != -1) goto _L2; else goto _L1
_L1:
            Certificate acertificate[] = null;
_L6:
            return acertificate;
_L2:
            CertificateFactory certificatefactory;
            int j;
            try
            {
                certificatefactory = CertificateFactory.getInstance("X.509");
                acertificate = new Certificate[i];
            }
            catch (CertificateException certificateexception)
            {
                throw new IOException(certificateexception.getMessage());
            }
            j = 0;
            if (j >= acertificate.length)
            {
                continue; /* Loop/switch isn't completed */
            }
            acertificate[j] = certificatefactory.generateCertificate(new ByteArrayInputStream(Base64.decode(strictlinereader.readLine().getBytes("US-ASCII"))));
            j++;
            if (true) goto _L4; else goto _L3
_L3:
            break MISSING_BLOCK_LABEL_71;
_L4:
            break MISSING_BLOCK_LABEL_32;
            if (true) goto _L6; else goto _L5
_L5:
        }

        private void writeCertArray(Writer writer, Certificate acertificate[])
            throws IOException
        {
            if (acertificate != null) goto _L2; else goto _L1
_L1:
            writer.write("-1\n");
_L6:
            return;
_L2:
            int i;
            int j;
            String s;
            try
            {
                writer.write((new StringBuilder()).append(Integer.toString(acertificate.length)).append('\n').toString());
                i = acertificate.length;
            }
            catch (CertificateEncodingException certificateencodingexception)
            {
                throw new IOException(certificateencodingexception.getMessage());
            }
            j = 0;
            if (j >= i)
            {
                continue; /* Loop/switch isn't completed */
            }
            s = Base64.encode(acertificate[j].getEncoded());
            writer.write((new StringBuilder()).append(s).append('\n').toString());
            j++;
            if (true) goto _L4; else goto _L3
_L3:
            break MISSING_BLOCK_LABEL_90;
_L4:
            break MISSING_BLOCK_LABEL_45;
            if (true) goto _L6; else goto _L5
_L5:
        }

        public boolean matches(URI uri1, String s, Map map)
        {
            boolean flag = uri.equals(uri1.toString());
            boolean flag1 = false;
            if (flag)
            {
                boolean flag2 = requestMethod.equals(s);
                flag1 = false;
                if (flag2)
                {
                    boolean flag3 = (new ResponseHeaders(uri1, responseHeaders)).varyMatches(varyHeaders.toMultimap(false), map);
                    flag1 = false;
                    if (flag3)
                    {
                        flag1 = true;
                    }
                }
            }
            return flag1;
        }

        public void writeTo(com.squareup.okhttp.internal.DiskLruCache.Editor editor)
            throws IOException
        {
            BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(editor.newOutputStream(0), Util.UTF_8));
            bufferedwriter.write((new StringBuilder()).append(uri).append('\n').toString());
            bufferedwriter.write((new StringBuilder()).append(requestMethod).append('\n').toString());
            bufferedwriter.write((new StringBuilder()).append(Integer.toString(varyHeaders.length())).append('\n').toString());
            for (int i = 0; i < varyHeaders.length(); i++)
            {
                bufferedwriter.write((new StringBuilder()).append(varyHeaders.getFieldName(i)).append(": ").append(varyHeaders.getValue(i)).append('\n').toString());
            }

            bufferedwriter.write((new StringBuilder()).append(responseHeaders.getStatusLine()).append('\n').toString());
            bufferedwriter.write((new StringBuilder()).append(Integer.toString(responseHeaders.length())).append('\n').toString());
            for (int j = 0; j < responseHeaders.length(); j++)
            {
                bufferedwriter.write((new StringBuilder()).append(responseHeaders.getFieldName(j)).append(": ").append(responseHeaders.getValue(j)).append('\n').toString());
            }

            if (isHttps())
            {
                bufferedwriter.write(10);
                bufferedwriter.write((new StringBuilder()).append(cipherSuite).append('\n').toString());
                writeCertArray(bufferedwriter, peerCertificates);
                writeCertArray(bufferedwriter, localCertificates);
            }
            bufferedwriter.close();
        }






        public Entry(InputStream inputstream)
            throws IOException
        {
            StrictLineReader strictlinereader;
            int i;
            strictlinereader = new StrictLineReader(inputstream, Util.US_ASCII);
            uri = strictlinereader.readLine();
            requestMethod = strictlinereader.readLine();
            varyHeaders = new RawHeaders();
            i = strictlinereader.readInt();
            int j = 0;
_L2:
            if (j >= i)
            {
                break; /* Loop/switch isn't completed */
            }
            varyHeaders.addLine(strictlinereader.readLine());
            j++;
            if (true) goto _L2; else goto _L1
_L1:
            int k;
            responseHeaders = new RawHeaders();
            responseHeaders.setStatusLine(strictlinereader.readLine());
            k = strictlinereader.readInt();
            int l = 0;
_L4:
            if (l >= k)
            {
                break; /* Loop/switch isn't completed */
            }
            responseHeaders.addLine(strictlinereader.readLine());
            l++;
            if (true) goto _L4; else goto _L3
_L3:
            if (!isHttps())
            {
                break MISSING_BLOCK_LABEL_223;
            }
            String s = strictlinereader.readLine();
            if (s.length() > 0)
            {
                throw new IOException((new StringBuilder()).append("expected \"\" but was \"").append(s).append("\"").toString());
            }
            break MISSING_BLOCK_LABEL_192;
            Exception exception;
            exception;
            inputstream.close();
            throw exception;
            cipherSuite = strictlinereader.readLine();
            peerCertificates = readCertArray(strictlinereader);
            localCertificates = readCertArray(strictlinereader);
_L5:
            inputstream.close();
            return;
            cipherSuite = null;
            peerCertificates = null;
            localCertificates = null;
              goto _L5
        }

        public Entry(URI uri1, RawHeaders rawheaders, HttpURLConnection httpurlconnection)
            throws IOException
        {
            uri = uri1.toString();
            varyHeaders = rawheaders;
            requestMethod = httpurlconnection.getRequestMethod();
            responseHeaders = RawHeaders.fromMultimap(httpurlconnection.getHeaderFields(), true);
            if (!isHttps()) goto _L2; else goto _L1
_L1:
            HttpsURLConnection httpsurlconnection;
            httpsurlconnection = (HttpsURLConnection)httpurlconnection;
            cipherSuite = httpsurlconnection.getCipherSuite();
            Certificate acertificate1[] = httpsurlconnection.getServerCertificates();
            Certificate acertificate[] = acertificate1;
_L4:
            peerCertificates = acertificate;
            localCertificates = httpsurlconnection.getLocalCertificates();
            return;
_L2:
            cipherSuite = null;
            peerCertificates = null;
            localCertificates = null;
            return;
            SSLPeerUnverifiedException sslpeerunverifiedexception;
            sslpeerunverifiedexception;
            acertificate = null;
            if (true) goto _L4; else goto _L3
_L3:
        }
    }

    static class EntryCacheResponse extends CacheResponse
    {

        private final Entry entry;
        private final InputStream in;
        private final com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot;

        public InputStream getBody()
        {
            return in;
        }

        public Map getHeaders()
        {
            return entry.responseHeaders.toMultimap(true);
        }


        public EntryCacheResponse(Entry entry1, com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot1)
        {
            entry = entry1;
            snapshot = snapshot1;
            in = HttpResponseCache.newBodyInputStream(snapshot1);
        }
    }

    static class EntrySecureCacheResponse extends SecureCacheResponse
    {

        private final Entry entry;
        private final InputStream in;
        private final com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot;

        public InputStream getBody()
        {
            return in;
        }

        public String getCipherSuite()
        {
            return entry.cipherSuite;
        }

        public Map getHeaders()
        {
            return entry.responseHeaders.toMultimap(true);
        }

        public List getLocalCertificateChain()
        {
            if (entry.localCertificates == null || entry.localCertificates.length == 0)
            {
                return null;
            } else
            {
                return Arrays.asList((Object[])entry.localCertificates.clone());
            }
        }

        public Principal getLocalPrincipal()
        {
            if (entry.localCertificates == null || entry.localCertificates.length == 0)
            {
                return null;
            } else
            {
                return ((X509Certificate)entry.localCertificates[0]).getSubjectX500Principal();
            }
        }

        public Principal getPeerPrincipal()
            throws SSLPeerUnverifiedException
        {
            if (entry.peerCertificates == null || entry.peerCertificates.length == 0)
            {
                throw new SSLPeerUnverifiedException(null);
            } else
            {
                return ((X509Certificate)entry.peerCertificates[0]).getSubjectX500Principal();
            }
        }

        public List getServerCertificateChain()
            throws SSLPeerUnverifiedException
        {
            if (entry.peerCertificates == null || entry.peerCertificates.length == 0)
            {
                throw new SSLPeerUnverifiedException(null);
            } else
            {
                return Arrays.asList((Object[])entry.peerCertificates.clone());
            }
        }


        public EntrySecureCacheResponse(Entry entry1, com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot1)
        {
            entry = entry1;
            snapshot = snapshot1;
            in = HttpResponseCache.newBodyInputStream(snapshot1);
        }
    }


    private static final char DIGITS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 0x31191;
    private final DiskLruCache cache;
    private int hitCount;
    private int networkCount;
    final OkResponseCache okResponseCache = new OkResponseCache() {

        final HttpResponseCache this$0;

        public CacheResponse get(URI uri, String s, Map map)
            throws IOException
        {
            return HttpResponseCache.this.get(uri, s, map);
        }

        public CacheRequest put(URI uri, URLConnection urlconnection)
            throws IOException
        {
            return HttpResponseCache.this.put(uri, urlconnection);
        }

        public void trackConditionalCacheHit()
        {
            HttpResponseCache.this.trackConditionalCacheHit();
        }

        public void trackResponse(ResponseSource responsesource)
        {
            HttpResponseCache.this.trackResponse(responsesource);
        }

        public void update(CacheResponse cacheresponse, HttpURLConnection httpurlconnection)
            throws IOException
        {
            HttpResponseCache.this.update(cacheresponse, httpurlconnection);
        }

            
            {
                this$0 = HttpResponseCache.this;
                super();
            }
    };
    private int requestCount;
    private int writeAbortCount;
    private int writeSuccessCount;

    public HttpResponseCache(File file, long l)
        throws IOException
    {
        cache = DiskLruCache.open(file, 0x31191, 2, l);
    }

    private void abortQuietly(com.squareup.okhttp.internal.DiskLruCache.Editor editor)
    {
        if (editor == null)
        {
            break MISSING_BLOCK_LABEL_8;
        }
        editor.abort();
        return;
        IOException ioexception;
        ioexception;
    }

    private static String bytesToHexString(byte abyte0[])
    {
        char ac[] = DIGITS;
        char ac1[] = new char[2 * abyte0.length];
        int i = abyte0.length;
        int j = 0;
        int k = 0;
        for (; j < i; j++)
        {
            byte byte0 = abyte0[j];
            int l = k + 1;
            ac1[k] = ac[0xf & byte0 >> 4];
            k = l + 1;
            ac1[l] = ac[byte0 & 0xf];
        }

        return new String(ac1);
    }

    private HttpEngine getHttpEngine(URLConnection urlconnection)
    {
        if (urlconnection instanceof HttpURLConnectionImpl)
        {
            return ((HttpURLConnectionImpl)urlconnection).getHttpEngine();
        }
        if (urlconnection instanceof HttpsURLConnectionImpl)
        {
            return ((HttpsURLConnectionImpl)urlconnection).getHttpEngine();
        } else
        {
            return null;
        }
    }

    private static InputStream newBodyInputStream(com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot)
    {
        return new FilterInputStream(snapshot.getInputStream(1), snapshot) {

            final com.squareup.okhttp.internal.DiskLruCache.Snapshot val$snapshot;

            public void close()
                throws IOException
            {
                snapshot.close();
                close();
            }

            
            {
                snapshot = snapshot1;
                super(inputstream);
            }
        };
    }

    private void trackConditionalCacheHit()
    {
        this;
        JVM INSTR monitorenter ;
        hitCount = 1 + hitCount;
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    private void trackResponse(ResponseSource responsesource)
    {
        this;
        JVM INSTR monitorenter ;
        int i;
        requestCount = 1 + requestCount;
        static class _cls3
        {

            static final int $SwitchMap$com$squareup$okhttp$ResponseSource[];

            static 
            {
                $SwitchMap$com$squareup$okhttp$ResponseSource = new int[ResponseSource.values().length];
                try
                {
                    $SwitchMap$com$squareup$okhttp$ResponseSource[ResponseSource.CACHE.ordinal()] = 1;
                }
                catch (NoSuchFieldError nosuchfielderror) { }
                try
                {
                    $SwitchMap$com$squareup$okhttp$ResponseSource[ResponseSource.CONDITIONAL_CACHE.ordinal()] = 2;
                }
                catch (NoSuchFieldError nosuchfielderror1) { }
                try
                {
                    $SwitchMap$com$squareup$okhttp$ResponseSource[ResponseSource.NETWORK.ordinal()] = 3;
                }
                catch (NoSuchFieldError nosuchfielderror2)
                {
                    return;
                }
            }
        }

        i = _cls3..SwitchMap.com.squareup.okhttp.ResponseSource[responsesource.ordinal()];
        i;
        JVM INSTR tableswitch 1 3: default 48
    //                   1 51
    //                   2 69
    //                   3 69;
           goto _L1 _L2 _L3 _L3
_L1:
        this;
        JVM INSTR monitorexit ;
        return;
_L2:
        hitCount = 1 + hitCount;
          goto _L1
        Exception exception;
        exception;
        throw exception;
_L3:
        networkCount = 1 + networkCount;
          goto _L1
    }

    private void update(CacheResponse cacheresponse, HttpURLConnection httpurlconnection)
        throws IOException
    {
        HttpEngine httpengine = getHttpEngine(httpurlconnection);
        URI uri = httpengine.getUri();
        ResponseHeaders responseheaders = httpengine.getResponseHeaders();
        Entry entry = new Entry(uri, httpengine.getRequestHeaders().getHeaders().getAll(responseheaders.getVaryFields()), httpurlconnection);
        com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot;
        com.squareup.okhttp.internal.DiskLruCache.Editor editor;
        if (cacheresponse instanceof EntryCacheResponse)
        {
            snapshot = ((EntryCacheResponse)cacheresponse).snapshot;
        } else
        {
            snapshot = ((EntrySecureCacheResponse)cacheresponse).snapshot;
        }
        editor = null;
        try
        {
            editor = snapshot.edit();
        }
        catch (IOException ioexception)
        {
            abortQuietly(editor);
            return;
        }
        if (editor == null)
        {
            break MISSING_BLOCK_LABEL_88;
        }
        entry.writeTo(editor);
        editor.commit();
    }

    private String uriToKey(URI uri)
    {
        String s;
        try
        {
            s = bytesToHexString(MessageDigest.getInstance("MD5").digest(uri.toString().getBytes("UTF-8")));
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            throw new AssertionError(nosuchalgorithmexception);
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new AssertionError(unsupportedencodingexception);
        }
        return s;
    }

    public void delete()
        throws IOException
    {
        cache.delete();
    }

    public CacheResponse get(URI uri, String s, Map map)
    {
        com.squareup.okhttp.internal.DiskLruCache.Snapshot snapshot;
        Entry entry;
        String s1 = uriToKey(uri);
        try
        {
            snapshot = cache.get(s1);
        }
        catch (IOException ioexception)
        {
            return null;
        }
        if (snapshot == null)
        {
            return null;
        }
        entry = new Entry(snapshot.getInputStream(0));
        if (!entry.matches(uri, s, map))
        {
            snapshot.close();
            return null;
        }
        if (entry.isHttps())
        {
            return new EntrySecureCacheResponse(entry, snapshot);
        } else
        {
            return new EntryCacheResponse(entry, snapshot);
        }
    }

    public int getHitCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = hitCount;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public int getNetworkCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = networkCount;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public int getRequestCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = requestCount;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public int getWriteAbortCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = writeAbortCount;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public int getWriteSuccessCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = writeSuccessCount;
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public CacheRequest put(URI uri, URLConnection urlconnection)
        throws IOException
    {
        if (urlconnection instanceof HttpURLConnection) goto _L2; else goto _L1
_L1:
        return null;
_L2:
        HttpURLConnection httpurlconnection;
        String s;
        String s1;
        httpurlconnection = (HttpURLConnection)urlconnection;
        s = httpurlconnection.getRequestMethod();
        s1 = uriToKey(uri);
        if (s.equals("POST") || s.equals("PUT") || s.equals("DELETE"))
        {
            try
            {
                cache.remove(s1);
            }
            catch (IOException ioexception)
            {
                return null;
            }
            return null;
        }
        if (!s.equals("GET")) goto _L1; else goto _L3
_L3:
        HttpEngine httpengine = getHttpEngine(httpurlconnection);
        if (httpengine == null) goto _L1; else goto _L4
_L4:
        ResponseHeaders responseheaders = httpengine.getResponseHeaders();
        if (responseheaders.hasVaryAll()) goto _L1; else goto _L5
_L5:
        Entry entry = new Entry(uri, httpengine.getRequestHeaders().getHeaders().getAll(responseheaders.getVaryFields()), httpurlconnection);
        com.squareup.okhttp.internal.DiskLruCache.Editor editor = null;
        CacheRequestImpl cacherequestimpl;
        try
        {
            editor = cache.edit(s1);
        }
        catch (IOException ioexception1)
        {
            abortQuietly(editor);
            return null;
        }
        if (editor == null) goto _L1; else goto _L6
_L6:
        entry.writeTo(editor);
        cacherequestimpl = new CacheRequestImpl(editor);
        return cacherequestimpl;
    }






/*
    static int access$708(HttpResponseCache httpresponsecache)
    {
        int i = httpresponsecache.writeSuccessCount;
        httpresponsecache.writeSuccessCount = i + 1;
        return i;
    }

*/


/*
    static int access$808(HttpResponseCache httpresponsecache)
    {
        int i = httpresponsecache.writeAbortCount;
        httpresponsecache.writeAbortCount = i + 1;
        return i;
    }

*/


    // Unreferenced inner class com/squareup/okhttp/HttpResponseCache$CacheRequestImpl$1

/* anonymous class */
}
