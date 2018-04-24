// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.util.Log;
import android.webkit.CookieManager;
import com.squareup.okhttp.OkHttpClient;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import org.apache.cordova.api.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            FileTransfer, FileUploadResult, FileProgressResult

class val.objectId
    implements Runnable
{

    final FileTransfer this$0;
    final boolean val$chunkedMode;
    final questContext val$context;
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
        if (!val$context.aborted) goto _L2; else goto _L1
_L1:
        return;
_L2:
        Object obj;
        javax.net.ssl.HostnameVerifier hostnameverifier;
        javax.net.ssl.SSLSocketFactory sslsocketfactory;
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
        if (!val$useHttps) goto _L4; else goto _L3
_L3:
        boolean flag4 = val$trustEveryone;
        i = 0;
        obj = null;
        hostnameverifier = null;
        sslsocketfactory = null;
        if (flag4) goto _L6; else goto _L5
_L5:
        obj = (HttpsURLConnection)FileTransfer.access$000().open(val$url);
_L13:
        String s;
        ((HttpURLConnection) (obj)).setDoInput(true);
        ((HttpURLConnection) (obj)).setDoOutput(true);
        ((HttpURLConnection) (obj)).setUseCaches(false);
        ((HttpURLConnection) (obj)).setRequestMethod(val$httpMethod);
        ((HttpURLConnection) (obj)).setRequestProperty("Content-Type", "multipart/form-data;boundary=+++++");
        s = CookieManager.getInstance().getCookie(val$target);
        if (s == null)
        {
            break MISSING_BLOCK_LABEL_138;
        }
        ((HttpURLConnection) (obj)).setRequestProperty("Cookie", s);
        StringBuilder stringbuilder;
        if (val$headers != null)
        {
            FileTransfer.access$300(((java.net.URLConnection) (obj)), val$headers);
        }
        stringbuilder = new StringBuilder();
        Iterator iterator = val$params.keys();
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
            stringbuilder.append(val$params.getString(obj1.toString()));
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
        stringbuilder.append("Content-Disposition: form-data; name=\"").append(val$fileKey).append("\";");
        stringbuilder.append(" filename=\"").append(val$fileName).append('"').append("\r\n");
        stringbuilder.append("Content-Type: ").append(val$mimeType).append("\r\n").append("\r\n");
        abyte0 = stringbuilder.toString().getBytes("UTF-8");
        abyte1 = "\r\n--+++++--\r\n".getBytes("UTF-8");
        inputstream = FileTransfer.access$400(FileTransfer.this, val$source);
        int k = abyte0.length + abyte1.length;
        if (inputstream instanceof FileInputStream)
        {
            j = k + (int)((FileInputStream)inputstream).getChannel().size();
            fileprogressresult.setLengthComputable(true);
            fileprogressresult.setTotal(j);
        }
        Log.d("FileTransfer", (new StringBuilder()).append("Content Length: ").append(j).toString());
        flag = val$chunkedMode;
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
        questcontext3 = val$context;
        questcontext3;
        JVM INSTR monitorenter ;
        if (!val$context.aborted) goto _L11; else goto _L10
_L10:
        FileTransfer.access$500(inputstream);
        FileTransfer.access$500(outputstream);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L12
_L12:
        HttpsURLConnection httpsurlconnection7 = (HttpsURLConnection)obj;
        httpsurlconnection7.setHostnameVerifier(hostnameverifier);
        httpsurlconnection7.setSSLSocketFactory(sslsocketfactory);
        return;
_L6:
        httpsurlconnection8 = (HttpsURLConnection)FileTransfer.access$000().open(val$url);
        sslsocketfactory = FileTransfer.access$100(httpsurlconnection8);
        hostnameverifier = httpsurlconnection8.getHostnameVerifier();
        httpsurlconnection8.setHostnameVerifier(FileTransfer.access$200());
        obj = httpsurlconnection8;
          goto _L13
_L4:
        obj = FileTransfer.access$000().open(val$url);
        hostnameverifier = null;
        sslsocketfactory = null;
          goto _L13
_L9:
        ((HttpURLConnection) (obj)).setFixedLengthStreamingMode(j);
          goto _L14
        filenotfoundexception;
        JSONObject jsonobject2 = FileTransfer.access$700(FileTransfer.FILE_NOT_FOUND_ERR, val$source, val$target, ((java.net.URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject2.toString(), filenotfoundexception);
        questContext questcontext2 = val$context;
        PluginResult pluginresult2 = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject2);
        questcontext2.sendPluginResult(pluginresult2);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L15
_L15:
        HttpsURLConnection httpsurlconnection4 = (HttpsURLConnection)obj;
        httpsurlconnection4.setHostnameVerifier(hostnameverifier);
        httpsurlconnection4.setSSLSocketFactory(sslsocketfactory);
        return;
        exception12;
        hashmap7;
        JVM INSTR monitorexit ;
        throw exception12;
_L11:
        val$context.currentOutputStream = outputstream;
        questcontext3;
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
        PluginResult pluginresult3 = new PluginResult(org.apache.cordova.api.atus.OK, fileprogressresult.toJSONObject());
        pluginresult3.setKeepCallback(true);
        val$context.sendPluginResult(pluginresult3);
        if (true) goto _L17; else goto _L16
        exception6;
        FileTransfer.access$500(inputstream);
        FileTransfer.access$500(outputstream);
        throw exception6;
        ioexception;
        JSONObject jsonobject1 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((java.net.URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject1.toString(), ioexception);
        Log.e("FileTransfer", (new StringBuilder()).append("Failed after uploading ").append(i).append(" of ").append(j).append(" bytes.").toString());
        questContext questcontext1 = val$context;
        PluginResult pluginresult1 = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject1);
        questcontext1.sendPluginResult(pluginresult1);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L18
_L18:
        HttpsURLConnection httpsurlconnection3 = (HttpsURLConnection)obj;
        httpsurlconnection3.setHostnameVerifier(hostnameverifier);
        httpsurlconnection3.setSSLSocketFactory(sslsocketfactory);
        return;
        exception7;
        questcontext3;
        JVM INSTR monitorexit ;
        throw exception7;
_L16:
        outputstream.write(abyte1);
        i += abyte1.length;
        outputstream.flush();
        FileTransfer.access$500(inputstream);
        FileTransfer.access$500(outputstream);
        val$context.currentOutputStream = null;
        Log.d("FileTransfer", (new StringBuilder()).append("Sent ").append(i).append(" of ").append(j).toString());
        j1 = ((HttpURLConnection) (obj)).getResponseCode();
        Log.d("FileTransfer", (new StringBuilder()).append("response code: ").append(j1).toString());
        Log.d("FileTransfer", (new StringBuilder()).append("response headers: ").append(((HttpURLConnection) (obj)).getHeaderFields()).toString());
        ackinginputstream = null;
        ackinginputstream = FileTransfer.access$600(((java.net.URLConnection) (obj)));
        synchronized (val$context)
        {
            if (!val$context.aborted)
            {
                break MISSING_BLOCK_LABEL_1504;
            }
        }
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L19
_L19:
        HttpsURLConnection httpsurlconnection6 = (HttpsURLConnection)obj;
        httpsurlconnection6.setHostnameVerifier(hostnameverifier);
        httpsurlconnection6.setSSLSocketFactory(sslsocketfactory);
        return;
        exception11;
        hashmap6;
        JVM INSTR monitorexit ;
        throw exception11;
        val$context.currentInputStream = ackinginputstream;
        questcontext4;
        JVM INSTR monitorexit ;
        bytearrayoutputstream = new ByteArrayOutputStream(Math.max(1024, ((HttpURLConnection) (obj)).getContentLength()));
        abyte3 = new byte[1024];
_L20:
        k1 = ackinginputstream.read(abyte3);
        if (k1 <= 0)
        {
            break MISSING_BLOCK_LABEL_1686;
        }
        bytearrayoutputstream.write(abyte3, 0, k1);
          goto _L20
        exception8;
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        throw exception8;
        jsonexception;
        Log.e("FileTransfer", jsonexception.getMessage(), jsonexception);
        val$context.sendPluginResult(new PluginResult(org.apache.cordova.api.atus.JSON_EXCEPTION));
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L21
_L21:
        HttpsURLConnection httpsurlconnection2 = (HttpsURLConnection)obj;
        httpsurlconnection2.setHostnameVerifier(hostnameverifier);
        httpsurlconnection2.setSSLSocketFactory(sslsocketfactory);
        return;
        exception9;
        questcontext4;
        JVM INSTR monitorexit ;
        throw exception9;
        s1 = bytearrayoutputstream.toString("UTF-8");
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        Log.d("FileTransfer", "got response from server");
        Log.d("FileTransfer", s1.substring(0, Math.min(256, s1.length())));
        fileuploadresult.setResponseCode(j1);
        fileuploadresult.setResponse(s1);
        val$context.sendPluginResult(new PluginResult(org.apache.cordova.api.atus.OK, fileuploadresult.toJSONObject()));
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L22
_L22:
        HttpsURLConnection httpsurlconnection5 = (HttpsURLConnection)obj;
        httpsurlconnection5.setHostnameVerifier(hostnameverifier);
        httpsurlconnection5.setSSLSocketFactory(sslsocketfactory);
        return;
        exception10;
        hashmap5;
        JVM INSTR monitorexit ;
        throw exception10;
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
        throwable;
        JSONObject jsonobject = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((java.net.URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject.toString(), throwable);
        questContext questcontext = val$context;
        PluginResult pluginresult = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject);
        questcontext.sendPluginResult(pluginresult);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj == null || !val$trustEveryone || !val$useHttps) goto _L1; else goto _L23
_L23:
        HttpsURLConnection httpsurlconnection1 = (HttpsURLConnection)obj;
        httpsurlconnection1.setHostnameVerifier(hostnameverifier);
        httpsurlconnection1.setSSLSocketFactory(sslsocketfactory);
        return;
        exception2;
        hashmap1;
        JVM INSTR monitorexit ;
        throw exception2;
        exception;
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            HttpsURLConnection httpsurlconnection = (HttpsURLConnection)obj;
            httpsurlconnection.setHostnameVerifier(hostnameverifier);
            httpsurlconnection.setSSLSocketFactory(sslsocketfactory);
        }
        throw exception;
        exception1;
        hashmap;
        JVM INSTR monitorexit ;
        throw exception1;
        boolean flag1;
        Exception exception;
        Throwable throwable;
        JSONException jsonexception;
        IOException ioexception;
        FileNotFoundException filenotfoundexception;
        OutputStream outputstream;
        Exception exception6;
        questContext questcontext3;
        Exception exception7;
        byte abyte2[];
        int i1;
        long l1;
        int j1;
        ackingInputStream ackinginputstream;
        Exception exception8;
        ByteArrayOutputStream bytearrayoutputstream;
        byte abyte3[];
        int k1;
        String s1;
        HttpsURLConnection httpsurlconnection8;
        if (android.os.DK_INT >= 8 && !val$useHttps)
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

    questContext()
    {
        this$0 = final_filetransfer;
        val$context = questcontext;
        val$useHttps = flag;
        val$trustEveryone = flag1;
        val$url = url1;
        val$httpMethod = s;
        val$target = s1;
        val$headers = jsonobject;
        val$params = jsonobject1;
        val$fileKey = s2;
        val$fileName = s3;
        val$mimeType = s4;
        val$source = s5;
        val$chunkedMode = flag2;
        val$objectId = String.this;
        super();
    }
}
