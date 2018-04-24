// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Hashtable;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginManager;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView, AuthenticationToken, NativeToJsMessageQueue, Config

public class CordovaWebViewClient extends WebViewClient
{

    private static final String CORDOVA_EXEC_URL_PREFIX = "http://cdv_exec/";
    private static final String TAG = "Cordova";
    CordovaWebView appView;
    private Hashtable authenticationTokens;
    CordovaInterface cordova;
    private boolean doClearHistory;

    public CordovaWebViewClient(CordovaInterface cordovainterface)
    {
        doClearHistory = false;
        authenticationTokens = new Hashtable();
        cordova = cordovainterface;
    }

    public CordovaWebViewClient(CordovaInterface cordovainterface, CordovaWebView cordovawebview)
    {
        doClearHistory = false;
        authenticationTokens = new Hashtable();
        cordova = cordovainterface;
        appView = cordovawebview;
    }

    private void handleExecUrl(String s)
    {
        int i = "http://cdv_exec/".length();
        int j = s.indexOf('#', i + 1);
        int k = s.indexOf('#', j + 1);
        int l = s.indexOf('#', k + 1);
        if (i == -1 || j == -1 || k == -1 || l == -1)
        {
            Log.e("Cordova", (new StringBuilder()).append("Could not decode URL command: ").append(s).toString());
            return;
        } else
        {
            String s1 = s.substring(i, j);
            String s2 = s.substring(j + 1, k);
            String s3 = s.substring(k + 1, l);
            String s4 = s.substring(l + 1);
            appView.pluginManager.exec(s1, s2, s3, s4);
            return;
        }
    }

    public void clearAuthenticationTokens()
    {
        authenticationTokens.clear();
    }

    public AuthenticationToken getAuthenticationToken(String s, String s1)
    {
        AuthenticationToken authenticationtoken = (AuthenticationToken)authenticationTokens.get(s.concat(s1));
        if (authenticationtoken == null)
        {
            authenticationtoken = (AuthenticationToken)authenticationTokens.get(s);
            if (authenticationtoken == null)
            {
                authenticationtoken = (AuthenticationToken)authenticationTokens.get(s1);
            }
            if (authenticationtoken == null)
            {
                authenticationtoken = (AuthenticationToken)authenticationTokens.get("");
            }
        }
        return authenticationtoken;
    }

    public void onPageFinished(WebView webview, String s)
    {
        super.onPageFinished(webview, s);
        LOG.d("Cordova", (new StringBuilder()).append("onPageFinished(").append(s).append(")").toString());
        if (doClearHistory)
        {
            webview.clearHistory();
            doClearHistory = false;
        }
        CordovaWebView cordovawebview = appView;
        cordovawebview.loadUrlTimeout = 1 + cordovawebview.loadUrlTimeout;
        appView.postMessage("onPageFinished", s);
        if (appView.getVisibility() == 4)
        {
            (new Thread(new Runnable() {

                final CordovaWebViewClient this$0;

                public void run()
                {
                    try
                    {
                        Thread.sleep(2000L);
                        cordova.getActivity().runOnUiThread(new Runnable() {

                            final _cls1 this$1;

                            public void run()
                            {
                                appView.postMessage("spinner", "stop");
                            }

            
            {
                this$1 = _cls1.this;
                super();
            }
                        });
                        return;
                    }
                    catch (InterruptedException interruptedexception)
                    {
                        return;
                    }
                }

            
            {
                this$0 = CordovaWebViewClient.this;
                super();
            }
            })).start();
        }
        if (s.equals("about:blank"))
        {
            appView.postMessage("exit", null);
        }
    }

    public void onPageStarted(WebView webview, String s, Bitmap bitmap)
    {
        appView.jsMessageQueue.reset();
        appView.postMessage("onPageStarted", s);
        if (appView.pluginManager != null)
        {
            appView.pluginManager.onReset();
        }
    }

    public void onReceivedError(WebView webview, int i, String s, String s1)
    {
        Object aobj[] = new Object[3];
        aobj[0] = Integer.valueOf(i);
        aobj[1] = s;
        aobj[2] = s1;
        LOG.d("Cordova", "CordovaWebViewClient.onReceivedError: Error code=%s Description=%s URL=%s", aobj);
        CordovaWebView cordovawebview = appView;
        cordovawebview.loadUrlTimeout = 1 + cordovawebview.loadUrlTimeout;
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("errorCode", i);
            jsonobject.put("description", s);
            jsonobject.put("url", s1);
        }
        catch (JSONException jsonexception)
        {
            jsonexception.printStackTrace();
        }
        appView.postMessage("onReceivedError", jsonobject);
    }

    public void onReceivedHttpAuthRequest(WebView webview, HttpAuthHandler httpauthhandler, String s, String s1)
    {
        AuthenticationToken authenticationtoken = getAuthenticationToken(s, s1);
        if (authenticationtoken != null)
        {
            httpauthhandler.proceed(authenticationtoken.getUserName(), authenticationtoken.getPassword());
            return;
        } else
        {
            super.onReceivedHttpAuthRequest(webview, httpauthhandler, s, s1);
            return;
        }
    }

    public void onReceivedSslError(WebView webview, SslErrorHandler sslerrorhandler, SslError sslerror)
    {
        String s;
        PackageManager packagemanager;
        s = cordova.getActivity().getPackageName();
        packagemanager = cordova.getActivity().getPackageManager();
        if ((2 & packagemanager.getApplicationInfo(s, 128).flags) != 0)
        {
            sslerrorhandler.proceed();
            return;
        }
        try
        {
            super.onReceivedSslError(webview, sslerrorhandler, sslerror);
            return;
        }
        catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
        {
            super.onReceivedSslError(webview, sslerrorhandler, sslerror);
        }
        return;
    }

    public AuthenticationToken removeAuthenticationToken(String s, String s1)
    {
        return (AuthenticationToken)authenticationTokens.remove(s.concat(s1));
    }

    public void setAuthenticationToken(AuthenticationToken authenticationtoken, String s, String s1)
    {
        if (s == null)
        {
            s = "";
        }
        if (s1 == null)
        {
            s1 = "";
        }
        authenticationTokens.put(s.concat(s1), authenticationtoken);
    }

    public void setWebView(CordovaWebView cordovawebview)
    {
        appView = cordovawebview;
    }

    public boolean shouldOverrideUrlLoading(WebView webview, String s)
    {
        if (appView.pluginManager == null || !appView.pluginManager.onOverrideUrlLoading(s)) goto _L2; else goto _L1
_L1:
        return true;
_L2:
        if (s.startsWith("tel:"))
        {
            try
            {
                Intent intent5 = new Intent("android.intent.action.DIAL");
                intent5.setData(Uri.parse(s));
                cordova.getActivity().startActivity(intent5);
            }
            catch (ActivityNotFoundException activitynotfoundexception5)
            {
                LOG.e("Cordova", (new StringBuilder()).append("Error dialing ").append(s).append(": ").append(activitynotfoundexception5.toString()).toString());
            }
            continue; /* Loop/switch isn't completed */
        }
        if (s.startsWith("geo:"))
        {
            try
            {
                Intent intent4 = new Intent("android.intent.action.VIEW");
                intent4.setData(Uri.parse(s));
                cordova.getActivity().startActivity(intent4);
            }
            catch (ActivityNotFoundException activitynotfoundexception4)
            {
                LOG.e("Cordova", (new StringBuilder()).append("Error showing map ").append(s).append(": ").append(activitynotfoundexception4.toString()).toString());
            }
            continue; /* Loop/switch isn't completed */
        }
        if (s.startsWith("mailto:"))
        {
            try
            {
                Intent intent3 = new Intent("android.intent.action.VIEW");
                intent3.setData(Uri.parse(s));
                cordova.getActivity().startActivity(intent3);
            }
            catch (ActivityNotFoundException activitynotfoundexception3)
            {
                LOG.e("Cordova", (new StringBuilder()).append("Error sending email ").append(s).append(": ").append(activitynotfoundexception3.toString()).toString());
            }
            continue; /* Loop/switch isn't completed */
        }
        if (!s.startsWith("sms:")) goto _L4; else goto _L3
_L3:
        Intent intent2;
        int i;
        String s1;
        try
        {
            intent2 = new Intent("android.intent.action.VIEW");
            i = s.indexOf('?');
        }
        catch (ActivityNotFoundException activitynotfoundexception2)
        {
            LOG.e("Cordova", (new StringBuilder()).append("Error sending sms ").append(s).append(":").append(activitynotfoundexception2.toString()).toString());
            continue; /* Loop/switch isn't completed */
        }
        if (i != -1) goto _L6; else goto _L5
_L5:
        s1 = s.substring(4);
_L7:
        intent2.setData(Uri.parse((new StringBuilder()).append("sms:").append(s1).toString()));
        intent2.putExtra("address", s1);
        intent2.setType("vnd.android-dir/mms-sms");
        cordova.getActivity().startActivity(intent2);
        continue; /* Loop/switch isn't completed */
_L6:
        String s2;
        s1 = s.substring(4, i);
        s2 = Uri.parse(s).getQuery();
        if (s2 == null)
        {
            continue; /* Loop/switch isn't completed */
        }
        if (s2.startsWith("body="))
        {
            intent2.putExtra("sms_body", s2.substring(5));
        }
        if (true) goto _L7; else goto _L4
_L4:
        if (s.startsWith("market:"))
        {
            try
            {
                Intent intent1 = new Intent("android.intent.action.VIEW");
                intent1.setData(Uri.parse(s));
                cordova.getActivity().startActivity(intent1);
            }
            catch (ActivityNotFoundException activitynotfoundexception1)
            {
                LOG.e("Cordova", (new StringBuilder()).append("Error loading Google Play Store: ").append(s).toString(), activitynotfoundexception1);
            }
        } else
        {
            if (s.startsWith("file://") || s.startsWith("data:") || Config.isUrlWhiteListed(s))
            {
                return false;
            }
            try
            {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(s));
                cordova.getActivity().startActivity(intent);
            }
            catch (ActivityNotFoundException activitynotfoundexception)
            {
                LOG.e("Cordova", (new StringBuilder()).append("Error loading url ").append(s).toString(), activitynotfoundexception);
            }
        }
        if (true) goto _L1; else goto _L8
_L8:
    }
}
