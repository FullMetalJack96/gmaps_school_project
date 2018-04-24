// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Base64;
import com.squareup.okhttp.internal.StrictLineReader;
import com.squareup.okhttp.internal.Util;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            RawHeaders, ResponseHeaders, HttpResponseCache

private static final class localCertificates
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
            throw new IOException(certificateexception);
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
            throw new IOException(certificateencodingexception);
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

    public void writeTo(com.squareup.okhttp.internal.ntry ntry)
        throws IOException
    {
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(ntry.utputStream(0), Util.UTF_8));
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






    public (InputStream inputstream)
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
        if (!s.isEmpty())
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

    public localCertificates(URI uri1, RawHeaders rawheaders, HttpURLConnection httpurlconnection)
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
