// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.util.Log;
import android.webkit.CookieManager;
import com.squareup.okhttp.OkHttpClient;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            Config, FileHelper, FileUploadResult, FileProgressResult, 
//            FileUtils

public class FileTransfer extends CordovaPlugin
{
    private static class ExposedGZIPInputStream extends GZIPInputStream
    {

        public Inflater getInflater()
        {
            return inf;
        }

        public ExposedGZIPInputStream(InputStream inputstream)
            throws IOException
        {
            super(inputstream);
        }
    }

    private static final class RequestContext
    {

        boolean aborted;
        CallbackContext callbackContext;
        InputStream currentInputStream;
        OutputStream currentOutputStream;
        String source;
        String target;
        File targetFile;

        void sendPluginResult(PluginResult pluginresult)
        {
            this;
            JVM INSTR monitorenter ;
            if (!aborted)
            {
                callbackContext.sendPluginResult(pluginresult);
            }
            this;
            JVM INSTR monitorexit ;
            return;
            Exception exception;
            exception;
            this;
            JVM INSTR monitorexit ;
            throw exception;
        }

        RequestContext(String s, String s1, CallbackContext callbackcontext)
        {
            source = s;
            target = s1;
            callbackContext = callbackcontext;
        }
    }

    private static class TrackingGZIPInputStream extends TrackingInputStream
    {

        private ExposedGZIPInputStream gzin;

        public long getTotalRawBytesRead()
        {
            return gzin.getInflater().getBytesRead();
        }

        public TrackingGZIPInputStream(ExposedGZIPInputStream exposedgzipinputstream)
            throws IOException
        {
            super(exposedgzipinputstream);
            gzin = exposedgzipinputstream;
        }
    }

    private static class TrackingHTTPInputStream extends TrackingInputStream
    {

        private long bytesRead;

        private int updateBytesRead(int i)
        {
            if (i != -1)
            {
                bytesRead = bytesRead + (long)i;
            }
            return i;
        }

        public long getTotalRawBytesRead()
        {
            return bytesRead;
        }

        public int read()
            throws IOException
        {
            return updateBytesRead(super.read());
        }

        public int read(byte abyte0[])
            throws IOException
        {
            return updateBytesRead(super.read(abyte0));
        }

        public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            return updateBytesRead(super.read(abyte0, i, j));
        }

        public TrackingHTTPInputStream(InputStream inputstream)
        {
            super(inputstream);
            bytesRead = 0L;
        }
    }

    private static abstract class TrackingInputStream extends FilterInputStream
    {

        public abstract long getTotalRawBytesRead();

        public TrackingInputStream(InputStream inputstream)
        {
            super(inputstream);
        }
    }


    public static int ABORTED_ERR = 0;
    private static final String BOUNDARY = "+++++";
    public static int CONNECTION_ERR = 0;
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String s, SSLSession sslsession)
        {
            return true;
        }

    };
    public static int FILE_NOT_FOUND_ERR = 0;
    public static int INVALID_URL_ERR = 0;
    private static final String LINE_END = "\r\n";
    private static final String LINE_START = "--";
    private static final String LOG_TAG = "FileTransfer";
    private static final int MAX_BUFFER_SIZE = 16384;
    private static HashMap activeRequests = new HashMap();
    private static OkHttpClient httpClient = new OkHttpClient();
    private static final TrustManager trustAllCerts[];

    public FileTransfer()
    {
    }

    private void abort(String s)
    {
        final RequestContext context;
        synchronized (activeRequests)
        {
            context = (RequestContext)activeRequests.remove(s);
        }
        if (context == null)
        {
            break MISSING_BLOCK_LABEL_119;
        }
        File file = context.targetFile;
        if (file != null)
        {
            file.delete();
        }
        JSONObject jsonobject = createFileTransferError(ABORTED_ERR, context.source, context.target, null, Integer.valueOf(-1));
        context;
        JVM INSTR monitorenter ;
        context.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, jsonobject));
        context.aborted = true;
        context;
        JVM INSTR monitorexit ;
        cordova.getThreadPool().execute(new Runnable() {

            final FileTransfer this$0;
            final RequestContext val$context;

            public void run()
            {
                synchronized (context)
                {
                    FileTransfer.safeClose(context.currentInputStream);
                    FileTransfer.safeClose(context.currentOutputStream);
                }
                return;
                exception2;
                requestcontext;
                JVM INSTR monitorexit ;
                throw exception2;
            }

            
            {
                this$0 = FileTransfer.this;
                context = requestcontext;
                super();
            }
        });
        return;
        exception;
        hashmap;
        JVM INSTR monitorexit ;
        throw exception;
        Exception exception1;
        exception1;
        context;
        JVM INSTR monitorexit ;
        throw exception1;
    }

    private static void addHeadersToRequest(URLConnection urlconnection, JSONObject jsonobject)
    {
        Iterator iterator = jsonobject.keys();
_L4:
        String s;
        JSONArray jsonarray;
        if (!iterator.hasNext())
        {
            break MISSING_BLOCK_LABEL_104;
        }
        s = iterator.next().toString();
        jsonarray = jsonobject.optJSONArray(s);
        if (jsonarray != null)
        {
            break MISSING_BLOCK_LABEL_59;
        }
        jsonarray = new JSONArray();
        jsonarray.put(jsonobject.getString(s));
        urlconnection.setRequestProperty(s, jsonarray.getString(0));
        int i = 1;
_L2:
        if (i >= jsonarray.length())
        {
            break; /* Loop/switch isn't completed */
        }
        urlconnection.addRequestProperty(s, jsonarray.getString(i));
        i++;
        if (true) goto _L2; else goto _L1
_L1:
        if (true) goto _L4; else goto _L3
_L3:
        JSONException jsonexception;
        jsonexception;
    }

    private static JSONObject createFileTransferError(int i, String s, String s1, String s2, Integer integer)
    {
        JSONObject jsonobject = null;
        JSONObject jsonobject1 = new JSONObject();
        jsonobject1.put("code", i);
        jsonobject1.put("source", s);
        jsonobject1.put("target", s1);
        if (s2 == null)
        {
            break MISSING_BLOCK_LABEL_53;
        }
        jsonobject1.put("body", s2);
        if (integer == null)
        {
            break MISSING_BLOCK_LABEL_69;
        }
        jsonobject1.put("http_status", integer);
        return jsonobject1;
        JSONException jsonexception;
        jsonexception;
_L2:
        Log.e("FileTransfer", jsonexception.getMessage(), jsonexception);
        return jsonobject;
        jsonexception;
        jsonobject = jsonobject1;
        if (true) goto _L2; else goto _L1
_L1:
    }

    private static JSONObject createFileTransferError(int i, String s, String s1, URLConnection urlconnection)
    {
        StringBuilder stringbuilder;
        String s2;
        int j;
        stringbuilder = new StringBuilder();
        s2 = null;
        j = 0;
        if (urlconnection == null) goto _L2; else goto _L1
_L1:
        boolean flag = urlconnection instanceof HttpURLConnection;
        s2 = null;
        j = 0;
        if (!flag) goto _L2; else goto _L3
_L3:
        InputStream inputstream;
        j = ((HttpURLConnection)urlconnection).getResponseCode();
        inputstream = ((HttpURLConnection)urlconnection).getErrorStream();
        s2 = null;
        if (inputstream == null) goto _L2; else goto _L4
_L4:
        BufferedReader bufferedreader;
        String s3;
        bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        s3 = bufferedreader.readLine();
_L8:
        if (s3 == null) goto _L6; else goto _L5
_L5:
        stringbuilder.append(s3);
        s3 = bufferedreader.readLine();
        if (s3 == null) goto _L8; else goto _L7
_L7:
        stringbuilder.append('\n');
          goto _L8
        IOException ioexception;
        ioexception;
        Log.w("FileTransfer", "Error getting HTTP status code from connection.", ioexception);
_L2:
        return createFileTransferError(i, s, s1, s2, Integer.valueOf(j));
_L6:
        String s4 = stringbuilder.toString();
        s2 = s4;
        if (true) goto _L2; else goto _L9
_L9:
    }

    private void download(final String source, final String target, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        Log.d("FileTransfer", (new StringBuilder()).append("download ").append(source).append(" to ").append(target).toString());
        final boolean trustEveryone = jsonarray.optBoolean(2);
        final String objectId = jsonarray.getString(3);
        final JSONObject headers = jsonarray.optJSONObject(4);
        final URL url;
        final boolean useHttps;
        try
        {
            url = new URL(source);
        }
        catch (MalformedURLException malformedurlexception)
        {
            JSONObject jsonobject1 = createFileTransferError(INVALID_URL_ERR, source, target, null, Integer.valueOf(0));
            Log.e("FileTransfer", jsonobject1.toString(), malformedurlexception);
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject1));
            return;
        }
        useHttps = url.getProtocol().equals("https");
        if (!Config.isUrlWhiteListed(source))
        {
            Log.w("FileTransfer", (new StringBuilder()).append("Source URL is not in white list: '").append(source).append("'").toString());
            JSONObject jsonobject = createFileTransferError(CONNECTION_ERR, source, target, null, Integer.valueOf(401));
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject));
            return;
        }
        final RequestContext context = new RequestContext(source, target, callbackcontext);
        synchronized (activeRequests)
        {
            activeRequests.put(objectId, context);
        }
        cordova.getThreadPool().execute(new Runnable() {

            final FileTransfer this$0;
            final RequestContext val$context;
            final JSONObject val$headers;
            final String val$objectId;
            final String val$source;
            final String val$target;
            final boolean val$trustEveryone;
            final URL val$url;
            final boolean val$useHttps;

            public void run()
            {
                Object obj;
                HostnameVerifier hostnameverifier;
                SSLSocketFactory sslsocketfactory;
                File file;
                if (context.aborted)
                {
                    return;
                }
                obj = null;
                hostnameverifier = null;
                sslsocketfactory = null;
                file = null;
                file = getFileFromPath(target);
                context.targetFile = file;
                file.getParentFile().mkdirs();
                if (!useHttps) goto _L2; else goto _L1
_L1:
                boolean flag = trustEveryone;
                obj = null;
                hostnameverifier = null;
                sslsocketfactory = null;
                if (flag) goto _L4; else goto _L3
_L3:
                obj = (HttpsURLConnection)FileTransfer.httpClient.open(url);
_L7:
                String s;
                if (obj instanceof HttpURLConnection)
                {
                    ((HttpURLConnection)obj).setRequestMethod("GET");
                }
                s = CookieManager.getInstance().getCookie(source);
                if (s == null)
                {
                    break MISSING_BLOCK_LABEL_130;
                }
                ((URLConnection) (obj)).setRequestProperty("cookie", s);
                FileProgressResult fileprogressresult;
                ((URLConnection) (obj)).setRequestProperty("Accept-Encoding", "gzip");
                if (headers != null)
                {
                    FileTransfer.addHeadersToRequest(((URLConnection) (obj)), headers);
                }
                ((URLConnection) (obj)).connect();
                Log.d("FileTransfer", (new StringBuilder()).append("Download file:").append(url).toString());
                fileprogressresult = new FileProgressResult();
                if (((URLConnection) (obj)).getContentEncoding() == null || ((URLConnection) (obj)).getContentEncoding().equalsIgnoreCase("gzip"))
                {
                    fileprogressresult.setLengthComputable(true);
                    fileprogressresult.setTotal(((URLConnection) (obj)).getContentLength());
                }
                TrackingInputStream trackinginputstream = null;
                FileOutputStream fileoutputstream;
                trackinginputstream = FileTransfer.getInputStream(((URLConnection) (obj)));
                fileoutputstream = new FileOutputStream(file);
                RequestContext requestcontext1 = context;
                requestcontext1;
                JVM INSTR monitorenter ;
                if (!context.aborted) goto _L6; else goto _L5
_L5:
                PluginResult pluginresult2;
                RequestContext requestcontext;
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                FileTransfer.safeClose(fileoutputstream);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    HttpsURLConnection httpsurlconnection6 = (HttpsURLConnection)obj;
                    httpsurlconnection6.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection6.setSSLSocketFactory(sslsocketfactory);
                }
                pluginresult2 = null;
                if (true)
                {
                    org.apache.cordova.api.PluginResult.Status status6 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    JSONObject jsonobject12 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status6, jsonobject12);
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
_L11:
                requestcontext.sendPluginResult(pluginresult2);
                return;
_L4:
                HttpsURLConnection httpsurlconnection7;
                httpsurlconnection7 = (HttpsURLConnection)FileTransfer.httpClient.open(url);
                sslsocketfactory = FileTransfer.trustAllHosts(httpsurlconnection7);
                hostnameverifier = httpsurlconnection7.getHostnameVerifier();
                httpsurlconnection7.setHostnameVerifier(FileTransfer.DO_NOT_VERIFY);
                obj = httpsurlconnection7;
                  goto _L7
_L2:
                HttpURLConnection httpurlconnection = FileTransfer.httpClient.open(url);
                obj = httpurlconnection;
                hostnameverifier = null;
                sslsocketfactory = null;
                  goto _L7
_L6:
                context.currentInputStream = trackinginputstream;
                requestcontext1;
                JVM INSTR monitorexit ;
                byte abyte0[] = new byte[16384];
_L10:
                int i = trackinginputstream.read(abyte0);
                if (i <= 0) goto _L9; else goto _L8
_L8:
                fileoutputstream.write(abyte0, 0, i);
                fileprogressresult.setLoaded(trackinginputstream.getTotalRawBytesRead());
                PluginResult pluginresult6 = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, fileprogressresult.toJSONObject());
                pluginresult6.setKeepCallback(true);
                context.sendPluginResult(pluginresult6);
                  goto _L10
                Exception exception7;
                exception7;
                Object obj1 = fileoutputstream;
_L12:
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                FileTransfer.safeClose(((Closeable) (obj1)));
                throw exception7;
                FileNotFoundException filenotfoundexception;
                filenotfoundexception;
                PluginResult pluginresult5;
                JSONObject jsonobject8 = FileTransfer.createFileTransferError(FileTransfer.FILE_NOT_FOUND_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject8.toString(), filenotfoundexception);
                pluginresult5 = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject8);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    HttpsURLConnection httpsurlconnection4 = (HttpsURLConnection)obj;
                    httpsurlconnection4.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection4.setSSLSocketFactory(sslsocketfactory);
                }
                Exception exception1;
                PluginResult pluginresult;
                org.apache.cordova.api.PluginResult.Status status;
                JSONObject jsonobject2;
                HttpsURLConnection httpsurlconnection;
                Throwable throwable;
                JSONObject jsonobject3;
                PluginResult pluginresult1;
                org.apache.cordova.api.PluginResult.Status status1;
                JSONObject jsonobject4;
                HttpsURLConnection httpsurlconnection1;
                JSONException jsonexception;
                PluginResult pluginresult3;
                org.apache.cordova.api.PluginResult.Status status2;
                JSONObject jsonobject5;
                HttpsURLConnection httpsurlconnection2;
                IOException ioexception;
                JSONObject jsonobject6;
                PluginResult pluginresult4;
                org.apache.cordova.api.PluginResult.Status status3;
                JSONObject jsonobject7;
                HttpsURLConnection httpsurlconnection3;
                Exception exception8;
                JSONObject jsonobject10;
                PluginResult pluginresult7;
                org.apache.cordova.api.PluginResult.Status status5;
                JSONObject jsonobject11;
                HttpsURLConnection httpsurlconnection5;
                if (pluginresult5 == null)
                {
                    org.apache.cordova.api.PluginResult.Status status4 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    JSONObject jsonobject9 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status4, jsonobject9);
                } else
                {
                    pluginresult2 = pluginresult5;
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
                  goto _L11
                exception8;
                requestcontext1;
                JVM INSTR monitorexit ;
                throw exception8;
_L9:
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                FileTransfer.safeClose(fileoutputstream);
                Log.d("FileTransfer", (new StringBuilder()).append("Saved file: ").append(target).toString());
                jsonobject10 = FileUtils.getEntry(file);
                pluginresult7 = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, jsonobject10);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    httpsurlconnection5 = (HttpsURLConnection)obj;
                    httpsurlconnection5.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection5.setSSLSocketFactory(sslsocketfactory);
                }
                if (pluginresult7 == null)
                {
                    status5 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    jsonobject11 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status5, jsonobject11);
                } else
                {
                    pluginresult2 = pluginresult7;
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
                  goto _L11
                ioexception;
                jsonobject6 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject6.toString(), ioexception);
                pluginresult4 = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject6);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    httpsurlconnection3 = (HttpsURLConnection)obj;
                    httpsurlconnection3.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection3.setSSLSocketFactory(sslsocketfactory);
                }
                if (pluginresult4 == null)
                {
                    status3 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    jsonobject7 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status3, jsonobject7);
                } else
                {
                    pluginresult2 = pluginresult4;
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
                  goto _L11
                jsonexception;
                Log.e("FileTransfer", jsonexception.getMessage(), jsonexception);
                pluginresult3 = new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    httpsurlconnection2 = (HttpsURLConnection)obj;
                    httpsurlconnection2.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection2.setSSLSocketFactory(sslsocketfactory);
                }
                if (pluginresult3 == null)
                {
                    status2 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    jsonobject5 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status2, jsonobject5);
                } else
                {
                    pluginresult2 = pluginresult3;
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
                  goto _L11
                throwable;
                jsonobject3 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject3.toString(), throwable);
                pluginresult1 = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject3);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    httpsurlconnection1 = (HttpsURLConnection)obj;
                    httpsurlconnection1.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection1.setSSLSocketFactory(sslsocketfactory);
                }
                if (pluginresult1 == null)
                {
                    status1 = org.apache.cordova.api.PluginResult.Status.ERROR;
                    jsonobject4 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult2 = new PluginResult(status1, jsonobject4);
                } else
                {
                    pluginresult2 = pluginresult1;
                }
                if (pluginresult2.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                requestcontext = context;
                  goto _L11
                exception1;
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    httpsurlconnection = (HttpsURLConnection)obj;
                    httpsurlconnection.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection.setSSLSocketFactory(sslsocketfactory);
                }
                pluginresult = null;
                if (true)
                {
                    status = org.apache.cordova.api.PluginResult.Status.ERROR;
                    jsonobject2 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                    pluginresult = new PluginResult(status, jsonobject2);
                }
                if (pluginresult.getStatus() != org.apache.cordova.api.PluginResult.Status.OK.ordinal() && file != null)
                {
                    file.delete();
                }
                context.sendPluginResult(pluginresult);
                throw exception1;
                exception2;
                hashmap1;
                JVM INSTR monitorexit ;
                throw exception2;
                exception6;
                hashmap5;
                JVM INSTR monitorexit ;
                throw exception6;
                exception5;
                hashmap4;
                JVM INSTR monitorexit ;
                throw exception5;
                exception4;
                hashmap3;
                JVM INSTR monitorexit ;
                throw exception4;
                exception3;
                hashmap2;
                JVM INSTR monitorexit ;
                throw exception3;
                exception10;
                hashmap7;
                JVM INSTR monitorexit ;
                throw exception10;
                exception9;
                hashmap6;
                JVM INSTR monitorexit ;
                throw exception9;
                exception7;
                obj1 = null;
                  goto _L12
            }

            
            {
                this$0 = FileTransfer.this;
                context = requestcontext;
                target = s;
                useHttps = flag;
                trustEveryone = flag1;
                url = url1;
                source = s1;
                headers = jsonobject;
                objectId = s2;
                super();
            }
        });
        return;
        exception;
        hashmap;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private static String getArgument(JSONArray jsonarray, int i, String s)
    {
        String s1 = s;
        if (jsonarray.length() > i)
        {
            s1 = jsonarray.optString(i);
            if (s1 == null || "null".equals(s1))
            {
                s1 = s;
            }
        }
        return s1;
    }

    private File getFileFromPath(String s)
        throws FileNotFoundException
    {
        File file;
        if (s.startsWith("file://"))
        {
            file = new File(s.substring("file://".length()));
        } else
        {
            file = new File(s);
        }
        if (file.getParent() == null)
        {
            throw new FileNotFoundException();
        } else
        {
            return file;
        }
    }

    private static TrackingInputStream getInputStream(URLConnection urlconnection)
        throws IOException
    {
        String s = urlconnection.getContentEncoding();
        if (s != null && s.equalsIgnoreCase("gzip"))
        {
            return new TrackingGZIPInputStream(new ExposedGZIPInputStream(urlconnection.getInputStream()));
        } else
        {
            return new TrackingHTTPInputStream(urlconnection.getInputStream());
        }
    }

    private InputStream getPathFromUri(String s)
        throws FileNotFoundException
    {
        Object obj;
        try
        {
            obj = FileHelper.getInputStreamFromUriString(s, cordova);
        }
        catch (IOException ioexception)
        {
            throw new FileNotFoundException();
        }
        if (obj != null)
        {
            break MISSING_BLOCK_LABEL_22;
        }
        obj = new FileInputStream(s);
        return ((InputStream) (obj));
    }

    private static void safeClose(Closeable closeable)
    {
        if (closeable == null)
        {
            break MISSING_BLOCK_LABEL_10;
        }
        closeable.close();
        return;
        IOException ioexception;
        ioexception;
    }

    private static SSLSocketFactory trustAllHosts(HttpsURLConnection httpsurlconnection)
    {
        SSLSocketFactory sslsocketfactory = httpsurlconnection.getSSLSocketFactory();
        try
        {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, trustAllCerts, new SecureRandom());
            httpsurlconnection.setSSLSocketFactory(sslcontext.getSocketFactory());
        }
        catch (Exception exception)
        {
            Log.e("FileTransfer", exception.getMessage(), exception);
            return sslsocketfactory;
        }
        return sslsocketfactory;
    }

    private void upload(final String source, final String target, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        Log.d("FileTransfer", (new StringBuilder()).append("upload ").append(source).append(" to ").append(target).toString());
        final String fileKey = getArgument(jsonarray, 2, "file");
        final String fileName = getArgument(jsonarray, 3, "image.jpg");
        final String mimeType = getArgument(jsonarray, 4, "image/jpeg");
        final JSONObject params;
        final boolean trustEveryone;
        final boolean chunkedMode;
        final JSONObject headers;
        final String objectId;
        final String httpMethod;
        final URL url;
        final boolean useHttps;
        final RequestContext context;
        if (jsonarray.optJSONObject(5) == null)
        {
            params = new JSONObject();
        } else
        {
            params = jsonarray.optJSONObject(5);
        }
        trustEveryone = jsonarray.optBoolean(6);
        if (jsonarray.optBoolean(7) || jsonarray.isNull(7))
        {
            chunkedMode = true;
        } else
        {
            chunkedMode = false;
        }
        if (jsonarray.optJSONObject(8) == null)
        {
            headers = params.optJSONObject("headers");
        } else
        {
            headers = jsonarray.optJSONObject(8);
        }
        objectId = jsonarray.getString(9);
        httpMethod = getArgument(jsonarray, 10, "POST");
        Log.d("FileTransfer", (new StringBuilder()).append("fileKey: ").append(fileKey).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("fileName: ").append(fileName).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("mimeType: ").append(mimeType).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("params: ").append(params).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("trustEveryone: ").append(trustEveryone).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("chunkedMode: ").append(chunkedMode).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("headers: ").append(headers).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("objectId: ").append(objectId).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("httpMethod: ").append(httpMethod).toString());
        try
        {
            url = new URL(target);
        }
        catch (MalformedURLException malformedurlexception)
        {
            JSONObject jsonobject = createFileTransferError(INVALID_URL_ERR, source, target, null, Integer.valueOf(0));
            Log.e("FileTransfer", jsonobject.toString(), malformedurlexception);
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject));
            return;
        }
        useHttps = url.getProtocol().equals("https");
        context = new RequestContext(source, target, callbackcontext);
        synchronized (activeRequests)
        {
            activeRequests.put(objectId, context);
        }
        cordova.getThreadPool().execute(new Runnable() {

            final FileTransfer this$0;
            final boolean val$chunkedMode;
            final RequestContext val$context;
            final String val$fileKey;
            final String val$fileName;
            final JSONObject val$headers;
            final String val$httpMethod;
            final String val$mimeType;
            final String val$objectId;
            final JSONObject val$params;
            final String val$source;
            final String val$target;
            final boolean val$trustEveryone;
            final URL val$url;
            final boolean val$useHttps;

            public void run()
            {
                if (!context.aborted) goto _L2; else goto _L1
_L1:
                return;
_L2:
                Object obj;
                HostnameVerifier hostnameverifier;
                SSLSocketFactory sslsocketfactory;
                int i;
                int j;
                obj = null;
                hostnameverifier = null;
                sslsocketfactory = null;
                i = 0;
                j = -1;
                FileUploadResult fileuploadresult;
                FileProgressResult fileprogressresult;
                fileuploadresult = new FileUploadResult();
                fileprogressresult = new FileProgressResult();
                if (!useHttps) goto _L4; else goto _L3
_L3:
                boolean flag4 = trustEveryone;
                i = 0;
                obj = null;
                hostnameverifier = null;
                sslsocketfactory = null;
                if (flag4) goto _L6; else goto _L5
_L5:
                obj = (HttpsURLConnection)FileTransfer.httpClient.open(url);
_L13:
                String s;
                ((HttpURLConnection) (obj)).setDoInput(true);
                ((HttpURLConnection) (obj)).setDoOutput(true);
                ((HttpURLConnection) (obj)).setUseCaches(false);
                ((HttpURLConnection) (obj)).setRequestMethod(httpMethod);
                ((HttpURLConnection) (obj)).setRequestProperty("Content-Type", "multipart/form-data;boundary=+++++");
                s = CookieManager.getInstance().getCookie(target);
                if (s == null)
                {
                    break MISSING_BLOCK_LABEL_138;
                }
                ((HttpURLConnection) (obj)).setRequestProperty("Cookie", s);
                StringBuilder stringbuilder;
                if (headers != null)
                {
                    FileTransfer.addHeadersToRequest(((URLConnection) (obj)), headers);
                }
                stringbuilder = new StringBuilder();
                Iterator iterator = params.keys();
_L7:
                boolean flag3 = iterator.hasNext();
                i = 0;
                if (!flag3)
                {
                    break MISSING_BLOCK_LABEL_306;
                }
                Object obj1 = iterator.next();
                if (!String.valueOf(obj1).equals("headers"))
                {
                    stringbuilder.append("--").append("+++++").append("\r\n");
                    stringbuilder.append("Content-Disposition: form-data; name=\"").append(obj1.toString()).append('"');
                    stringbuilder.append("\r\n").append("\r\n");
                    stringbuilder.append(params.getString(obj1.toString()));
                    stringbuilder.append("\r\n");
                }
                  goto _L7
                JSONException jsonexception1;
                jsonexception1;
                Log.e("FileTransfer", jsonexception1.getMessage(), jsonexception1);
                byte abyte0[];
                byte abyte1[];
                InputStream inputstream;
                boolean flag;
                stringbuilder.append("--").append("+++++").append("\r\n");
                stringbuilder.append("Content-Disposition: form-data; name=\"").append(fileKey).append("\";");
                stringbuilder.append(" filename=\"").append(fileName).append('"').append("\r\n");
                stringbuilder.append("Content-Type: ").append(mimeType).append("\r\n").append("\r\n");
                abyte0 = stringbuilder.toString().getBytes("UTF-8");
                abyte1 = "\r\n--+++++--\r\n".getBytes("UTF-8");
                inputstream = getPathFromUri(source);
                int k = abyte0.length + abyte1.length;
                if (inputstream instanceof FileInputStream)
                {
                    j = k + (int)((FileInputStream)inputstream).getChannel().size();
                    fileprogressresult.setLengthComputable(true);
                    fileprogressresult.setTotal(j);
                }
                Log.d("FileTransfer", (new StringBuilder()).append("Content Length: ").append(j).toString());
                flag = chunkedMode;
                i = 0;
                if (!flag)
                {
                    break MISSING_BLOCK_LABEL_2084;
                }
                break MISSING_BLOCK_LABEL_516;
_L24:
                boolean flag2;
                if (!flag2) goto _L9; else goto _L8
_L8:
                ((HttpURLConnection) (obj)).setChunkedStreamingMode(16384);
                ((HttpURLConnection) (obj)).setRequestProperty("Transfer-Encoding", "chunked");
_L14:
                ((HttpURLConnection) (obj)).connect();
                outputstream = null;
                outputstream = ((HttpURLConnection) (obj)).getOutputStream();
                requestcontext3 = context;
                requestcontext3;
                JVM INSTR monitorenter ;
                if (!context.aborted) goto _L11; else goto _L10
_L10:
                FileTransfer.safeClose(inputstream);
                FileTransfer.safeClose(outputstream);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L12
_L12:
                HttpsURLConnection httpsurlconnection7 = (HttpsURLConnection)obj;
                httpsurlconnection7.setHostnameVerifier(hostnameverifier);
                httpsurlconnection7.setSSLSocketFactory(sslsocketfactory);
                return;
_L6:
                httpsurlconnection8 = (HttpsURLConnection)FileTransfer.httpClient.open(url);
                sslsocketfactory = FileTransfer.trustAllHosts(httpsurlconnection8);
                hostnameverifier = httpsurlconnection8.getHostnameVerifier();
                httpsurlconnection8.setHostnameVerifier(FileTransfer.DO_NOT_VERIFY);
                obj = httpsurlconnection8;
                  goto _L13
_L4:
                obj = FileTransfer.httpClient.open(url);
                hostnameverifier = null;
                sslsocketfactory = null;
                  goto _L13
_L9:
                ((HttpURLConnection) (obj)).setFixedLengthStreamingMode(j);
                  goto _L14
                filenotfoundexception;
                JSONObject jsonobject3 = FileTransfer.createFileTransferError(FileTransfer.FILE_NOT_FOUND_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject3.toString(), filenotfoundexception);
                RequestContext requestcontext2 = context;
                PluginResult pluginresult2 = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject3);
                requestcontext2.sendPluginResult(pluginresult2);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L15
_L15:
                HttpsURLConnection httpsurlconnection4 = (HttpsURLConnection)obj;
                httpsurlconnection4.setHostnameVerifier(hostnameverifier);
                httpsurlconnection4.setSSLSocketFactory(sslsocketfactory);
                return;
                exception13;
                hashmap8;
                JVM INSTR monitorexit ;
                throw exception13;
_L11:
                context.currentOutputStream = outputstream;
                requestcontext3;
                JVM INSTR monitorexit ;
                outputstream.write(abyte0);
                i = 0 + abyte0.length;
                int l = Math.min(inputstream.available(), 16384);
                abyte2 = new byte[l];
                i1 = inputstream.read(abyte2, 0, l);
                l1 = 0L;
_L17:
                if (i1 <= 0)
                {
                    break; /* Loop/switch isn't completed */
                }
                fileuploadresult.setBytesSent(i);
                outputstream.write(abyte2, 0, i1);
                i += i1;
                if ((long)i <= 0x19000L + l1)
                {
                    break MISSING_BLOCK_LABEL_1006;
                }
                l1 = i;
                Log.d("FileTransfer", (new StringBuilder()).append("Uploaded ").append(i).append(" of ").append(j).append(" bytes").toString());
                i1 = inputstream.read(abyte2, 0, Math.min(inputstream.available(), 16384));
                fileprogressresult.setLoaded(i);
                PluginResult pluginresult3 = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, fileprogressresult.toJSONObject());
                pluginresult3.setKeepCallback(true);
                context.sendPluginResult(pluginresult3);
                if (true) goto _L17; else goto _L16
                exception7;
                FileTransfer.safeClose(inputstream);
                FileTransfer.safeClose(outputstream);
                throw exception7;
                ioexception;
                JSONObject jsonobject2 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject2.toString(), ioexception);
                Log.e("FileTransfer", (new StringBuilder()).append("Failed after uploading ").append(i).append(" of ").append(j).append(" bytes.").toString());
                RequestContext requestcontext1 = context;
                PluginResult pluginresult1 = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject2);
                requestcontext1.sendPluginResult(pluginresult1);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L18
_L18:
                HttpsURLConnection httpsurlconnection3 = (HttpsURLConnection)obj;
                httpsurlconnection3.setHostnameVerifier(hostnameverifier);
                httpsurlconnection3.setSSLSocketFactory(sslsocketfactory);
                return;
                exception8;
                requestcontext3;
                JVM INSTR monitorexit ;
                throw exception8;
_L16:
                outputstream.write(abyte1);
                i += abyte1.length;
                outputstream.flush();
                FileTransfer.safeClose(inputstream);
                FileTransfer.safeClose(outputstream);
                context.currentOutputStream = null;
                Log.d("FileTransfer", (new StringBuilder()).append("Sent ").append(i).append(" of ").append(j).toString());
                j1 = ((HttpURLConnection) (obj)).getResponseCode();
                Log.d("FileTransfer", (new StringBuilder()).append("response code: ").append(j1).toString());
                Log.d("FileTransfer", (new StringBuilder()).append("response headers: ").append(((HttpURLConnection) (obj)).getHeaderFields()).toString());
                trackinginputstream = null;
                trackinginputstream = FileTransfer.getInputStream(((URLConnection) (obj)));
                synchronized (context)
                {
                    if (!context.aborted)
                    {
                        break MISSING_BLOCK_LABEL_1504;
                    }
                }
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L19
_L19:
                HttpsURLConnection httpsurlconnection6 = (HttpsURLConnection)obj;
                httpsurlconnection6.setHostnameVerifier(hostnameverifier);
                httpsurlconnection6.setSSLSocketFactory(sslsocketfactory);
                return;
                exception12;
                hashmap7;
                JVM INSTR monitorexit ;
                throw exception12;
                context.currentInputStream = trackinginputstream;
                requestcontext4;
                JVM INSTR monitorexit ;
                bytearrayoutputstream = new ByteArrayOutputStream(Math.max(1024, ((HttpURLConnection) (obj)).getContentLength()));
                abyte3 = new byte[1024];
_L20:
                k1 = trackinginputstream.read(abyte3);
                if (k1 <= 0)
                {
                    break MISSING_BLOCK_LABEL_1686;
                }
                bytearrayoutputstream.write(abyte3, 0, k1);
                  goto _L20
                exception9;
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                throw exception9;
                jsonexception;
                Log.e("FileTransfer", jsonexception.getMessage(), jsonexception);
                context.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION));
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L21
_L21:
                HttpsURLConnection httpsurlconnection2 = (HttpsURLConnection)obj;
                httpsurlconnection2.setHostnameVerifier(hostnameverifier);
                httpsurlconnection2.setSSLSocketFactory(sslsocketfactory);
                return;
                exception10;
                requestcontext4;
                JVM INSTR monitorexit ;
                throw exception10;
                s1 = bytearrayoutputstream.toString("UTF-8");
                context.currentInputStream = null;
                FileTransfer.safeClose(trackinginputstream);
                Log.d("FileTransfer", "got response from server");
                Log.d("FileTransfer", s1.substring(0, Math.min(256, s1.length())));
                fileuploadresult.setResponseCode(j1);
                fileuploadresult.setResponse(s1);
                context.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, fileuploadresult.toJSONObject()));
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L22
_L22:
                HttpsURLConnection httpsurlconnection5 = (HttpsURLConnection)obj;
                httpsurlconnection5.setHostnameVerifier(hostnameverifier);
                httpsurlconnection5.setSSLSocketFactory(sslsocketfactory);
                return;
                exception11;
                hashmap6;
                JVM INSTR monitorexit ;
                throw exception11;
                exception6;
                hashmap5;
                JVM INSTR monitorexit ;
                throw exception6;
                exception5;
                hashmap4;
                JVM INSTR monitorexit ;
                throw exception5;
                exception4;
                hashmap3;
                JVM INSTR monitorexit ;
                throw exception4;
                throwable;
                JSONObject jsonobject1 = FileTransfer.createFileTransferError(FileTransfer.CONNECTION_ERR, source, target, ((URLConnection) (obj)));
                Log.e("FileTransfer", jsonobject1.toString(), throwable);
                RequestContext requestcontext = context;
                PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, jsonobject1);
                requestcontext.sendPluginResult(pluginresult);
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj == null || !trustEveryone || !useHttps) goto _L1; else goto _L23
_L23:
                HttpsURLConnection httpsurlconnection1 = (HttpsURLConnection)obj;
                httpsurlconnection1.setHostnameVerifier(hostnameverifier);
                httpsurlconnection1.setSSLSocketFactory(sslsocketfactory);
                return;
                exception3;
                hashmap2;
                JVM INSTR monitorexit ;
                throw exception3;
                exception1;
                synchronized (FileTransfer.activeRequests)
                {
                    FileTransfer.activeRequests.remove(objectId);
                }
                if (obj != null && trustEveryone && useHttps)
                {
                    HttpsURLConnection httpsurlconnection = (HttpsURLConnection)obj;
                    httpsurlconnection.setHostnameVerifier(hostnameverifier);
                    httpsurlconnection.setSSLSocketFactory(sslsocketfactory);
                }
                throw exception1;
                exception2;
                hashmap1;
                JVM INSTR monitorexit ;
                throw exception2;
                boolean flag1;
                Exception exception1;
                Throwable throwable;
                JSONException jsonexception;
                IOException ioexception;
                FileNotFoundException filenotfoundexception;
                OutputStream outputstream;
                Exception exception7;
                RequestContext requestcontext3;
                Exception exception8;
                byte abyte2[];
                int i1;
                long l1;
                int j1;
                TrackingInputStream trackinginputstream;
                Exception exception9;
                ByteArrayOutputStream bytearrayoutputstream;
                byte abyte3[];
                int k1;
                String s1;
                HttpsURLConnection httpsurlconnection8;
                if (android.os.Build.VERSION.SDK_INT >= 8 && !useHttps)
                {
                    break MISSING_BLOCK_LABEL_2084;
                }
                flag1 = true;
_L25:
                if (flag1 || j == -1)
                {
                    flag2 = true;
                } else
                {
                    flag2 = false;
                }
                  goto _L24
                flag1 = false;
                  goto _L25
            }

            
            {
                this$0 = FileTransfer.this;
                context = requestcontext;
                useHttps = flag;
                trustEveryone = flag1;
                url = url1;
                httpMethod = s;
                target = s1;
                headers = jsonobject;
                params = jsonobject1;
                fileKey = s2;
                fileName = s3;
                mimeType = s4;
                source = s5;
                chunkedMode = flag2;
                objectId = s6;
                super();
            }
        });
        return;
        exception;
        hashmap;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (s.equals("upload") || s.equals("download"))
        {
            String s1 = jsonarray.getString(0);
            String s2 = jsonarray.getString(1);
            if (s.equals("upload"))
            {
                try
                {
                    upload(URLDecoder.decode(s1, "UTF-8"), s2, jsonarray, callbackcontext);
                }
                catch (UnsupportedEncodingException unsupportedencodingexception)
                {
                    callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.MALFORMED_URL_EXCEPTION, "UTF-8 error."));
                    return true;
                }
                return true;
            } else
            {
                download(s1, s2, jsonarray, callbackcontext);
                return true;
            }
        }
        if (s.equals("abort"))
        {
            abort(jsonarray.getString(0));
            callbackcontext.success();
            return true;
        } else
        {
            return false;
        }
    }

    static 
    {
        FILE_NOT_FOUND_ERR = 1;
        INVALID_URL_ERR = 2;
        CONNECTION_ERR = 3;
        ABORTED_ERR = 4;
        TrustManager atrustmanager[] = new TrustManager[1];
        atrustmanager[0] = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate ax509certificate[], String s)
                throws CertificateException
            {
            }

            public void checkServerTrusted(X509Certificate ax509certificate[], String s)
                throws CertificateException
            {
            }

            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }

        };
        trustAllCerts = atrustmanager;
    }










}
