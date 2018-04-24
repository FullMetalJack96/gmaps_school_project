// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.util.Log;
import android.webkit.CookieManager;
import com.squareup.okhttp.OkHttpClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import org.apache.cordova.api.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            FileTransfer, FileProgressResult, FileUtils

class val.objectId
    implements Runnable
{

    final FileTransfer this$0;
    final questContext val$context;
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
        javax.net.ssl.HostnameVerifier hostnameverifier;
        javax.net.ssl.SSLSocketFactory sslsocketfactory;
        File file;
        if (val$context.aborted)
        {
            return;
        }
        obj = null;
        hostnameverifier = null;
        sslsocketfactory = null;
        file = null;
        file = FileTransfer.access$900(FileTransfer.this, val$target);
        val$context.targetFile = file;
        file.getParentFile().mkdirs();
        if (!val$useHttps) goto _L2; else goto _L1
_L1:
        boolean flag = val$trustEveryone;
        obj = null;
        hostnameverifier = null;
        sslsocketfactory = null;
        if (flag) goto _L4; else goto _L3
_L3:
        obj = (HttpsURLConnection)FileTransfer.access$000().open(val$url);
_L7:
        String s;
        if (obj instanceof HttpURLConnection)
        {
            ((HttpURLConnection)obj).setRequestMethod("GET");
        }
        s = CookieManager.getInstance().getCookie(val$source);
        if (s == null)
        {
            break MISSING_BLOCK_LABEL_130;
        }
        ((URLConnection) (obj)).setRequestProperty("cookie", s);
        FileProgressResult fileprogressresult;
        ((URLConnection) (obj)).setRequestProperty("Accept-Encoding", "gzip");
        if (val$headers != null)
        {
            FileTransfer.access$300(((URLConnection) (obj)), val$headers);
        }
        ((URLConnection) (obj)).connect();
        Log.d("FileTransfer", (new StringBuilder()).append("Download file:").append(val$url).toString());
        fileprogressresult = new FileProgressResult();
        if (((URLConnection) (obj)).getContentEncoding() == null || ((URLConnection) (obj)).getContentEncoding().equalsIgnoreCase("gzip"))
        {
            fileprogressresult.setLengthComputable(true);
            fileprogressresult.setTotal(((URLConnection) (obj)).getContentLength());
        }
        ackingInputStream ackinginputstream = null;
        FileOutputStream fileoutputstream;
        ackinginputstream = FileTransfer.access$600(((URLConnection) (obj)));
        fileoutputstream = new FileOutputStream(file);
        questContext questcontext1 = val$context;
        questcontext1;
        JVM INSTR monitorenter ;
        if (!val$context.aborted) goto _L6; else goto _L5
_L5:
        PluginResult pluginresult2;
        questContext questcontext;
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        FileTransfer.access$500(fileoutputstream);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            HttpsURLConnection httpsurlconnection6 = (HttpsURLConnection)obj;
            httpsurlconnection6.setHostnameVerifier(hostnameverifier);
            httpsurlconnection6.setSSLSocketFactory(sslsocketfactory);
        }
        pluginresult2 = null;
        if (true)
        {
            org.apache.cordova.api.atus atus6 = org.apache.cordova.api.atus.ERROR;
            JSONObject jsonobject10 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus6, jsonobject10);
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
_L11:
        questcontext.sendPluginResult(pluginresult2);
        return;
_L4:
        HttpsURLConnection httpsurlconnection7;
        httpsurlconnection7 = (HttpsURLConnection)FileTransfer.access$000().open(val$url);
        sslsocketfactory = FileTransfer.access$100(httpsurlconnection7);
        hostnameverifier = httpsurlconnection7.getHostnameVerifier();
        httpsurlconnection7.setHostnameVerifier(FileTransfer.access$200());
        obj = httpsurlconnection7;
          goto _L7
_L2:
        HttpURLConnection httpurlconnection = FileTransfer.access$000().open(val$url);
        obj = httpurlconnection;
        hostnameverifier = null;
        sslsocketfactory = null;
          goto _L7
_L6:
        val$context.currentInputStream = ackinginputstream;
        questcontext1;
        JVM INSTR monitorexit ;
        byte abyte0[] = new byte[16384];
_L10:
        int i = ackinginputstream.read(abyte0);
        if (i <= 0) goto _L9; else goto _L8
_L8:
        fileoutputstream.write(abyte0, 0, i);
        fileprogressresult.setLoaded(ackinginputstream.getTotalRawBytesRead());
        PluginResult pluginresult6 = new PluginResult(org.apache.cordova.api.atus.OK, fileprogressresult.toJSONObject());
        pluginresult6.setKeepCallback(true);
        val$context.sendPluginResult(pluginresult6);
          goto _L10
        Exception exception6;
        exception6;
        Object obj1 = fileoutputstream;
_L12:
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        FileTransfer.access$500(((java.io.Closeable) (obj1)));
        throw exception6;
        FileNotFoundException filenotfoundexception;
        filenotfoundexception;
        PluginResult pluginresult5;
        JSONObject jsonobject6 = FileTransfer.access$700(FileTransfer.FILE_NOT_FOUND_ERR, val$source, val$target, ((URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject6.toString(), filenotfoundexception);
        pluginresult5 = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject6);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            HttpsURLConnection httpsurlconnection4 = (HttpsURLConnection)obj;
            httpsurlconnection4.setHostnameVerifier(hostnameverifier);
            httpsurlconnection4.setSSLSocketFactory(sslsocketfactory);
        }
        Exception exception;
        PluginResult pluginresult;
        org.apache.cordova.api.atus atus;
        JSONObject jsonobject;
        HttpsURLConnection httpsurlconnection;
        Throwable throwable;
        JSONObject jsonobject1;
        PluginResult pluginresult1;
        org.apache.cordova.api.atus atus1;
        JSONObject jsonobject2;
        HttpsURLConnection httpsurlconnection1;
        JSONException jsonexception;
        PluginResult pluginresult3;
        org.apache.cordova.api.atus atus2;
        JSONObject jsonobject3;
        HttpsURLConnection httpsurlconnection2;
        IOException ioexception;
        JSONObject jsonobject4;
        PluginResult pluginresult4;
        org.apache.cordova.api.atus atus3;
        JSONObject jsonobject5;
        HttpsURLConnection httpsurlconnection3;
        Exception exception7;
        JSONObject jsonobject8;
        PluginResult pluginresult7;
        org.apache.cordova.api.atus atus5;
        JSONObject jsonobject9;
        HttpsURLConnection httpsurlconnection5;
        if (pluginresult5 == null)
        {
            org.apache.cordova.api.atus atus4 = org.apache.cordova.api.atus.ERROR;
            JSONObject jsonobject7 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus4, jsonobject7);
        } else
        {
            pluginresult2 = pluginresult5;
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
          goto _L11
        exception7;
        questcontext1;
        JVM INSTR monitorexit ;
        throw exception7;
_L9:
        val$context.currentInputStream = null;
        FileTransfer.access$500(ackinginputstream);
        FileTransfer.access$500(fileoutputstream);
        Log.d("FileTransfer", (new StringBuilder()).append("Saved file: ").append(val$target).toString());
        jsonobject8 = FileUtils.getEntry(file);
        pluginresult7 = new PluginResult(org.apache.cordova.api.atus.OK, jsonobject8);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            httpsurlconnection5 = (HttpsURLConnection)obj;
            httpsurlconnection5.setHostnameVerifier(hostnameverifier);
            httpsurlconnection5.setSSLSocketFactory(sslsocketfactory);
        }
        if (pluginresult7 == null)
        {
            atus5 = org.apache.cordova.api.atus.ERROR;
            jsonobject9 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus5, jsonobject9);
        } else
        {
            pluginresult2 = pluginresult7;
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
          goto _L11
        ioexception;
        jsonobject4 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject4.toString(), ioexception);
        pluginresult4 = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject4);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            httpsurlconnection3 = (HttpsURLConnection)obj;
            httpsurlconnection3.setHostnameVerifier(hostnameverifier);
            httpsurlconnection3.setSSLSocketFactory(sslsocketfactory);
        }
        if (pluginresult4 == null)
        {
            atus3 = org.apache.cordova.api.atus.ERROR;
            jsonobject5 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus3, jsonobject5);
        } else
        {
            pluginresult2 = pluginresult4;
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
          goto _L11
        jsonexception;
        Log.e("FileTransfer", jsonexception.getMessage(), jsonexception);
        pluginresult3 = new PluginResult(org.apache.cordova.api.atus.JSON_EXCEPTION);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            httpsurlconnection2 = (HttpsURLConnection)obj;
            httpsurlconnection2.setHostnameVerifier(hostnameverifier);
            httpsurlconnection2.setSSLSocketFactory(sslsocketfactory);
        }
        if (pluginresult3 == null)
        {
            atus2 = org.apache.cordova.api.atus.ERROR;
            jsonobject3 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus2, jsonobject3);
        } else
        {
            pluginresult2 = pluginresult3;
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
          goto _L11
        throwable;
        jsonobject1 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
        Log.e("FileTransfer", jsonobject1.toString(), throwable);
        pluginresult1 = new PluginResult(org.apache.cordova.api.atus.IO_EXCEPTION, jsonobject1);
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            httpsurlconnection1 = (HttpsURLConnection)obj;
            httpsurlconnection1.setHostnameVerifier(hostnameverifier);
            httpsurlconnection1.setSSLSocketFactory(sslsocketfactory);
        }
        if (pluginresult1 == null)
        {
            atus1 = org.apache.cordova.api.atus.ERROR;
            jsonobject2 = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult2 = new PluginResult(atus1, jsonobject2);
        } else
        {
            pluginresult2 = pluginresult1;
        }
        if (pluginresult2.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        questcontext = val$context;
          goto _L11
        exception;
        synchronized (FileTransfer.access$800())
        {
            FileTransfer.access$800().remove(val$objectId);
        }
        if (obj != null && val$trustEveryone && val$useHttps)
        {
            httpsurlconnection = (HttpsURLConnection)obj;
            httpsurlconnection.setHostnameVerifier(hostnameverifier);
            httpsurlconnection.setSSLSocketFactory(sslsocketfactory);
        }
        pluginresult = null;
        if (true)
        {
            atus = org.apache.cordova.api.atus.ERROR;
            jsonobject = FileTransfer.access$700(FileTransfer.CONNECTION_ERR, val$source, val$target, ((URLConnection) (obj)));
            pluginresult = new PluginResult(atus, jsonobject);
        }
        if (pluginresult.getStatus() != org.apache.cordova.api.atus.OK.ordinal() && file != null)
        {
            file.delete();
        }
        val$context.sendPluginResult(pluginresult);
        throw exception;
        exception1;
        hashmap;
        JVM INSTR monitorexit ;
        throw exception1;
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
        exception2;
        hashmap1;
        JVM INSTR monitorexit ;
        throw exception2;
        exception9;
        hashmap6;
        JVM INSTR monitorexit ;
        throw exception9;
        exception8;
        hashmap5;
        JVM INSTR monitorexit ;
        throw exception8;
        exception6;
        obj1 = null;
          goto _L12
    }

    questContext()
    {
        this$0 = final_filetransfer;
        val$context = questcontext;
        val$target = s;
        val$useHttps = flag;
        val$trustEveryone = flag1;
        val$url = url1;
        val$source = s1;
        val$headers = jsonobject;
        val$objectId = String.this;
        super();
    }
}
