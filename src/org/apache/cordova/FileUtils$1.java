// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.util.Base64;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;

// Referenced classes of package org.apache.cordova:
//            FileUtils, FileHelper

class Context
    implements Runnable
{

    final FileUtils this$0;
    final CallbackContext val$callbackContext;
    final String val$encoding;
    final int val$end;
    final String val$filename;
    final int val$resultType;
    final int val$start;

    public void run()
    {
        byte abyte0[] = FileUtils.access$000(FileUtils.this, val$filename, val$start, val$end);
        val$resultType;
        JVM INSTR lookupswitch 3: default 60
    //                   1: 149
    //                   6: 177
    //                   7: 194;
           goto _L1 _L2 _L3 _L4
_L4:
        break MISSING_BLOCK_LABEL_194;
_L1:
        PluginResult pluginresult;
        String s = FileHelper.getMimeType(val$filename, cordova);
        byte abyte1[] = Base64.encode(abyte0, 0);
        String s1 = (new StringBuilder()).append("data:").append(s).append(";base64,").append(new String(abyte1, "US-ASCII")).toString();
        pluginresult = new PluginResult(org.apache.cordova.api..Status.OK, s1);
_L5:
        val$callbackContext.sendPluginResult(pluginresult);
        return;
_L2:
        try
        {
            pluginresult = new PluginResult(org.apache.cordova.api..Status.OK, new String(abyte0, val$encoding));
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            val$callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api..Status.IO_EXCEPTION, FileUtils.NOT_FOUND_ERR));
            return;
        }
        catch (IOException ioexception)
        {
            Log.d("FileUtils", ioexception.getLocalizedMessage());
            val$callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api..Status.IO_EXCEPTION, FileUtils.NOT_READABLE_ERR));
            return;
        }
          goto _L5
_L3:
        pluginresult = new PluginResult(org.apache.cordova.api..Status.OK, abyte0);
          goto _L5
        pluginresult = new PluginResult(org.apache.cordova.api..Status.OK, abyte0, true);
          goto _L5
    }

    Context()
    {
        this$0 = final_fileutils;
        val$filename = s;
        val$start = i;
        val$end = j;
        val$resultType = k;
        val$encoding = s1;
        val$callbackContext = CallbackContext.this;
        super();
    }
}
