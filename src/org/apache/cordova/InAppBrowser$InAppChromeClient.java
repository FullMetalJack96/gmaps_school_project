// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            InAppBrowser, CordovaWebView

public class webView extends WebChromeClient
{

    final InAppBrowser this$0;
    private CordovaWebView webView;

    public void onExceededDatabaseQuota(String s, String s1, long l, long l1, long l2, android.webkit.ent ent)
    {
        Object aobj[] = new Object[3];
        aobj[0] = Long.valueOf(l1);
        aobj[1] = Long.valueOf(l);
        aobj[2] = Long.valueOf(l2);
        LOG.d("InAppBrowser", "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", aobj);
        if (l1 < InAppBrowser.access$1100(InAppBrowser.this))
        {
            Object aobj1[] = new Object[1];
            aobj1[0] = Long.valueOf(l1);
            LOG.d("InAppBrowser", "calling quotaUpdater.updateQuota newQuota: %d", aobj1);
            ent.uota(l1);
            return;
        } else
        {
            ent.uota(l);
            return;
        }
    }

    public void onGeolocationPermissionsShowPrompt(String s, android.webkit. )
    {
        super.onGeolocationPermissionsShowPrompt(s, );
        .invoke(s, true, false);
    }

    public boolean onJsPrompt(WebView webview, String s, String s1, String s2, JsPromptResult jspromptresult)
    {
        if (s2 != null && s2.startsWith("gap-iab://"))
        {
            String s3 = s2.substring(10);
            if (s3.startsWith("InAppBrowser"))
            {
                PluginResult pluginresult;
                if (s1 == null || s1.length() == 0)
                {
                    pluginresult = new PluginResult(org.apache.cordova.api.sShowPrompt, new JSONArray());
                } else
                {
                    try
                    {
                        pluginresult = new PluginResult(org.apache.cordova.api.sShowPrompt, new JSONArray(s1));
                    }
                    catch (JSONException jsonexception)
                    {
                        pluginresult = new PluginResult(org.apache.cordova.api.ION, jsonexception.getMessage());
                    }
                }
                webView.sendPluginResult(pluginresult, s3);
                jspromptresult.confirm("");
                return true;
            }
        }
        return false;
    }

    public (CordovaWebView cordovawebview)
    {
        this$0 = InAppBrowser.this;
        super();
        webView = cordovawebview;
    }
}
