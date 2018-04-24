// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView, Config

public class InAppBrowser extends CordovaPlugin
{
    public class InAppBrowserClient extends WebViewClient
    {

        EditText edittext;
        final InAppBrowser this$0;
        CordovaWebView webView;

        public void onPageFinished(WebView webview, String s)
        {
            super.onPageFinished(webview, s);
            try
            {
                JSONObject jsonobject = new JSONObject();
                jsonobject.put("type", "loadstop");
                jsonobject.put("url", s);
                sendUpdate(jsonobject, true);
                return;
            }
            catch (JSONException jsonexception)
            {
                Log.d("InAppBrowser", "Should never happen");
            }
        }

        public void onPageStarted(WebView webview, String s, Bitmap bitmap)
        {
            String s1;
            super.onPageStarted(webview, s, bitmap);
            s1 = "";
            JSONObject jsonobject;
            if (s.startsWith("http:") || s.startsWith("https:") || s.startsWith("file:"))
            {
                s1 = s;
            } else
            if (s.startsWith("tel:"))
            {
                try
                {
                    Intent intent2 = new Intent("android.intent.action.DIAL");
                    intent2.setData(Uri.parse(s));
                    cordova.getActivity().startActivity(intent2);
                }
                catch (ActivityNotFoundException activitynotfoundexception2)
                {
                    LOG.e("InAppBrowser", (new StringBuilder()).append("Error dialing ").append(s).append(": ").append(activitynotfoundexception2.toString()).toString());
                }
            } else
            {
label0:
                {
                    if (!s.startsWith("geo:") && !s.startsWith("mailto:") && !s.startsWith("market:"))
                    {
                        break label0;
                    }
                    try
                    {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(s));
                        cordova.getActivity().startActivity(intent);
                    }
                    catch (ActivityNotFoundException activitynotfoundexception)
                    {
                        LOG.e("InAppBrowser", (new StringBuilder()).append("Error with ").append(s).append(": ").append(activitynotfoundexception.toString()).toString());
                    }
                }
            }
_L3:
            if (!s1.equals(edittext.getText().toString()))
            {
                edittext.setText(s1);
            }
            Intent intent1;
            ActivityNotFoundException activitynotfoundexception1;
            int i;
            String s2;
            String s3;
            try
            {
                jsonobject = new JSONObject();
                jsonobject.put("type", "loadstart");
                jsonobject.put("url", s1);
                sendUpdate(jsonobject, true);
                return;
            }
            catch (JSONException jsonexception)
            {
                Log.d("InAppBrowser", "Should never happen");
            }
            break MISSING_BLOCK_LABEL_544;
            if (!s.startsWith("sms:"))
            {
                break MISSING_BLOCK_LABEL_510;
            }
            intent1 = new Intent("android.intent.action.VIEW");
            i = s.indexOf('?');
            if (i != -1) goto _L2; else goto _L1
_L1:
            s2 = s.substring(4);
_L5:
            intent1.setData(Uri.parse((new StringBuilder()).append("sms:").append(s2).toString()));
            intent1.putExtra("address", s2);
            intent1.setType("vnd.android-dir/mms-sms");
            cordova.getActivity().startActivity(intent1);
              goto _L3
            activitynotfoundexception1;
            LOG.e("InAppBrowser", (new StringBuilder()).append("Error sending sms ").append(s).append(":").append(activitynotfoundexception1.toString()).toString());
              goto _L3
_L2:
            s2 = s.substring(4, i);
            s3 = Uri.parse(s).getQuery();
            if (s3 == null) goto _L5; else goto _L4
_L4:
            if (!s3.startsWith("body=")) goto _L5; else goto _L6
_L6:
            intent1.putExtra("sms_body", s3.substring(5));
              goto _L5
            s1 = (new StringBuilder()).append("http://").append(s).toString();
              goto _L3
        }

        public void onReceivedError(WebView webview, int i, String s, String s1)
        {
            super.onReceivedError(webview, i, s, s1);
            try
            {
                JSONObject jsonobject = new JSONObject();
                jsonobject.put("type", "loaderror");
                jsonobject.put("url", s1);
                jsonobject.put("code", i);
                jsonobject.put("message", s);
                sendUpdate(jsonobject, true, org.apache.cordova.api.PluginResult.Status.ERROR);
                return;
            }
            catch (JSONException jsonexception)
            {
                Log.d("InAppBrowser", "Should never happen");
            }
        }

        public InAppBrowserClient(CordovaWebView cordovawebview, EditText edittext1)
        {
            this$0 = InAppBrowser.this;
            super();
            webView = cordovawebview;
            edittext = edittext1;
        }
    }

    public class InAppChromeClient extends WebChromeClient
    {

        final InAppBrowser this$0;
        private CordovaWebView webView;

        public void onExceededDatabaseQuota(String s, String s1, long l, long l1, long l2, android.webkit.WebStorage.QuotaUpdater quotaupdater)
        {
            Object aobj[] = new Object[3];
            aobj[0] = Long.valueOf(l1);
            aobj[1] = Long.valueOf(l);
            aobj[2] = Long.valueOf(l2);
            LOG.d("InAppBrowser", "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", aobj);
            if (l1 < MAX_QUOTA)
            {
                Object aobj1[] = new Object[1];
                aobj1[0] = Long.valueOf(l1);
                LOG.d("InAppBrowser", "calling quotaUpdater.updateQuota newQuota: %d", aobj1);
                quotaupdater.updateQuota(l1);
                return;
            } else
            {
                quotaupdater.updateQuota(l);
                return;
            }
        }

        public void onGeolocationPermissionsShowPrompt(String s, android.webkit.GeolocationPermissions.Callback callback)
        {
            super.onGeolocationPermissionsShowPrompt(s, callback);
            callback.invoke(s, true, false);
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
                        pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, new JSONArray());
                    } else
                    {
                        try
                        {
                            pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, new JSONArray(s1));
                        }
                        catch (JSONException jsonexception)
                        {
                            pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION, jsonexception.getMessage());
                        }
                    }
                    webView.sendPluginResult(pluginresult, s3);
                    jspromptresult.confirm("");
                    return true;
                }
            }
            return false;
        }

        public InAppChromeClient(CordovaWebView cordovawebview)
        {
            this$0 = InAppBrowser.this;
            super();
            webView = cordovawebview;
        }
    }


    private static final String CLOSE_BUTTON_CAPTION = "closebuttoncaption";
    private static final String EXIT_EVENT = "exit";
    private static final String HIDDEN = "hidden";
    private static final String LOAD_ERROR_EVENT = "loaderror";
    private static final String LOAD_START_EVENT = "loadstart";
    private static final String LOAD_STOP_EVENT = "loadstop";
    private static final String LOCATION = "location";
    protected static final String LOG_TAG = "InAppBrowser";
    private static final String NULL = "null";
    private static final String SELF = "_self";
    private static final String SYSTEM = "_system";
    private long MAX_QUOTA;
    private String buttonLabel;
    private CallbackContext callbackContext;
    private Dialog dialog;
    private EditText edittext;
    private WebView inAppWebView;
    private boolean openWindowHidden;
    private boolean showLocationBar;

    public InAppBrowser()
    {
        MAX_QUOTA = 0x6400000L;
        showLocationBar = true;
        openWindowHidden = false;
        buttonLabel = "Done";
    }

    private void closeDialog()
    {
        try
        {
            inAppWebView.loadUrl("about:blank");
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("type", "exit");
            sendUpdate(jsonobject, false);
        }
        catch (JSONException jsonexception)
        {
            Log.d("InAppBrowser", "Should never happen");
        }
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    private boolean getShowLocationBar()
    {
        return showLocationBar;
    }

    private void goBack()
    {
        if (inAppWebView.canGoBack())
        {
            inAppWebView.goBack();
        }
    }

    private void goForward()
    {
        if (inAppWebView.canGoForward())
        {
            inAppWebView.goForward();
        }
    }

    private void injectDeferredObject(String s, String s1)
    {
        String s3;
        if (s1 != null)
        {
            JSONArray jsonarray = new JSONArray();
            jsonarray.put(s);
            String s2 = jsonarray.toString();
            s3 = String.format(s1, new Object[] {
                s2.substring(1, -1 + s2.length())
            });
        } else
        {
            s3 = s;
        }
        inAppWebView.loadUrl((new StringBuilder()).append("javascript:").append(s3).toString());
    }

    private void navigate(String s)
    {
        ((InputMethodManager)cordova.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(edittext.getWindowToken(), 0);
        if (!s.startsWith("http") && !s.startsWith("file:"))
        {
            inAppWebView.loadUrl((new StringBuilder()).append("http://").append(s).toString());
        } else
        {
            inAppWebView.loadUrl(s);
        }
        inAppWebView.requestFocus();
    }

    private HashMap parseFeature(String s)
    {
        HashMap hashmap;
        if (s.equals("null"))
        {
            hashmap = null;
        } else
        {
            hashmap = new HashMap();
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
            while (stringtokenizer.hasMoreElements()) 
            {
                StringTokenizer stringtokenizer1 = new StringTokenizer(stringtokenizer.nextToken(), "=");
                if (stringtokenizer1.hasMoreElements())
                {
                    String s1 = stringtokenizer1.nextToken();
                    if (s1.equalsIgnoreCase("closebuttoncaption"))
                    {
                        buttonLabel = stringtokenizer1.nextToken();
                    } else
                    {
                        Boolean boolean1;
                        if (stringtokenizer1.nextToken().equals("no"))
                        {
                            boolean1 = Boolean.FALSE;
                        } else
                        {
                            boolean1 = Boolean.TRUE;
                        }
                        hashmap.put(s1, boolean1);
                    }
                }
            }
        }
        return hashmap;
    }

    private void sendUpdate(JSONObject jsonobject, boolean flag)
    {
        sendUpdate(jsonobject, flag, org.apache.cordova.api.PluginResult.Status.OK);
    }

    private void sendUpdate(JSONObject jsonobject, boolean flag, org.apache.cordova.api.PluginResult.Status status)
    {
        PluginResult pluginresult = new PluginResult(status, jsonobject);
        pluginresult.setKeepCallback(flag);
        callbackContext.sendPluginResult(pluginresult);
    }

    private String updateUrl(String s)
    {
        if (Uri.parse(s).isRelative())
        {
            s = (new StringBuilder()).append(webView.getUrl().substring(0, 1 + webView.getUrl().lastIndexOf("/"))).append(s).toString();
        }
        return s;
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (!s.equals("open")) goto _L2; else goto _L1
_L1:
        String s5;
        String s6;
        callbackContext = callbackcontext;
        s5 = jsonarray.getString(0);
        s6 = jsonarray.optString(1);
        if (s6 == null) goto _L4; else goto _L3
_L3:
        if (!s6.equals("") && !s6.equals("null")) goto _L5; else goto _L4
_L5:
        HashMap hashmap;
        String s7;
        hashmap = parseFeature(jsonarray.optString(2));
        Log.d("InAppBrowser", (new StringBuilder()).append("target = ").append(s6).toString());
        s7 = updateUrl(s5);
        String s8 = "";
        if (!"_self".equals(s6)) goto _L7; else goto _L6
_L6:
        Log.d("InAppBrowser", "in self");
        if (!s7.startsWith("file://") && !s7.startsWith("javascript:") && !Config.isUrlWhiteListed(s7)) goto _L9; else goto _L8
_L8:
        webView.loadUrl(s7);
_L13:
        PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, s8);
        pluginresult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginresult);
          goto _L10
_L9:
        boolean flag1 = s7.startsWith("tel:");
        if (!flag1) goto _L12; else goto _L11
_L11:
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse(s7));
        cordova.getActivity().startActivity(intent);
          goto _L13
        ActivityNotFoundException activitynotfoundexception;
        activitynotfoundexception;
        LOG.e("InAppBrowser", (new StringBuilder()).append("Error dialing ").append(s7).append(": ").append(activitynotfoundexception.toString()).toString());
          goto _L13
        JSONException jsonexception;
        jsonexception;
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION));
          goto _L10
_L12:
        s8 = showWebPage(s7, hashmap);
          goto _L13
_L7:
label0:
        {
            if (!"_system".equals(s6))
            {
                break label0;
            }
            Log.d("InAppBrowser", "in system");
            s8 = openExternal(s7);
        }
          goto _L13
        Log.d("InAppBrowser", "in blank");
        s8 = showWebPage(s7, hashmap);
          goto _L13
_L2:
        if (!s.equals("close")) goto _L15; else goto _L14
_L14:
        closeDialog();
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK));
          goto _L10
_L15:
        if (!s.equals("injectScriptCode")) goto _L17; else goto _L16
_L16:
        boolean flag = jsonarray.getBoolean(1);
        String s4;
        s4 = null;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_468;
        }
        Object aobj3[] = new Object[1];
        aobj3[0] = callbackcontext.getCallbackId();
        s4 = String.format("prompt(JSON.stringify([eval(%%s)]), 'gap-iab://%s')", aobj3);
        injectDeferredObject(jsonarray.getString(0), s4);
          goto _L10
_L17:
        if (!s.equals("injectScriptFile")) goto _L19; else goto _L18
_L18:
        if (!jsonarray.getBoolean(1)) goto _L21; else goto _L20
_L20:
        String s3;
        Object aobj2[] = new Object[1];
        aobj2[0] = callbackcontext.getCallbackId();
        s3 = String.format("(function(d) { var c = d.createElement('script'); c.src = %%s; c.onload = function() { prompt('', 'gap-iab://%s'); }; d.body.appendChild(c); })(document)", aobj2);
_L30:
        injectDeferredObject(jsonarray.getString(0), s3);
          goto _L10
_L19:
        if (!s.equals("injectStyleCode")) goto _L23; else goto _L22
_L22:
        if (!jsonarray.getBoolean(1)) goto _L25; else goto _L24
_L24:
        String s2;
        Object aobj1[] = new Object[1];
        aobj1[0] = callbackcontext.getCallbackId();
        s2 = String.format("(function(d) { var c = d.createElement('style'); c.innerHTML = %%s; d.body.appendChild(c); prompt('', 'gap-iab://%s');})(document)", aobj1);
_L31:
        injectDeferredObject(jsonarray.getString(0), s2);
          goto _L10
_L23:
        if (!s.equals("injectStyleFile")) goto _L27; else goto _L26
_L26:
        String s1;
        if (!jsonarray.getBoolean(1))
        {
            break MISSING_BLOCK_LABEL_731;
        }
        Object aobj[] = new Object[1];
        aobj[0] = callbackcontext.getCallbackId();
        s1 = String.format("(function(d) { var c = d.createElement('link'); c.rel='stylesheet'; c.type='text/css'; c.href = %%s; d.head.appendChild(c); prompt('', 'gap-iab://%s');})(document)", aobj);
_L32:
        injectDeferredObject(jsonarray.getString(0), s1);
          goto _L10
_L27:
        if (!s.equals("show")) goto _L29; else goto _L28
_L28:
        Runnable runnable = new Runnable() {

            final InAppBrowser this$0;

            public void run()
            {
                dialog.show();
            }

            
            {
                this$0 = InAppBrowser.this;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK));
          goto _L10
_L29:
        return false;
_L4:
        s6 = "_self";
          goto _L5
_L10:
        return true;
_L21:
        s3 = "(function(d) { var c = d.createElement('script'); c.src = %s; d.body.appendChild(c); })(document)";
          goto _L30
_L25:
        s2 = "(function(d) { var c = d.createElement('style'); c.innerHTML = %s; d.body.appendChild(c); })(document)";
          goto _L31
        s1 = "(function(d) { var c = d.createElement('link'); c.rel='stylesheet'; c.type='text/css'; c.href = %s; d.head.appendChild(c); })(document)";
          goto _L32
    }

    public String openExternal(String s)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(s));
        cordova.getActivity().startActivity(intent);
        return "";
        ActivityNotFoundException activitynotfoundexception;
        activitynotfoundexception;
_L2:
        Log.d("InAppBrowser", (new StringBuilder()).append("InAppBrowser: Error loading url ").append(s).append(":").append(activitynotfoundexception.toString()).toString());
        return activitynotfoundexception.toString();
        activitynotfoundexception;
        if (true) goto _L2; else goto _L1
_L1:
    }

    public String showWebPage(final String url, HashMap hashmap)
    {
        showLocationBar = true;
        openWindowHidden = false;
        if (hashmap != null)
        {
            Boolean boolean1 = (Boolean)hashmap.get("location");
            if (boolean1 != null)
            {
                showLocationBar = boolean1.booleanValue();
            }
            Boolean boolean2 = (Boolean)hashmap.get("hidden");
            if (boolean2 != null)
            {
                openWindowHidden = boolean2.booleanValue();
            }
        }
        Runnable runnable = new Runnable() {

            final InAppBrowser this$0;
            final CordovaWebView val$thatWebView;
            final String val$url;

            private int dpToPixels(int i)
            {
                return (int)TypedValue.applyDimension(1, i, cordova.getActivity().getResources().getDisplayMetrics());
            }

            public void run()
            {
                dialog = new Dialog(cordova.getActivity(), 0x1030006);
                dialog.getWindow().getAttributes().windowAnimations = 0x1030002;
                dialog.requestWindowFeature(1);
                dialog.setCancelable(true);
                Dialog dialog1 = dialog;
                android.content.DialogInterface.OnDismissListener ondismisslistener = new android.content.DialogInterface.OnDismissListener() {

                    final _cls2 this$1;

                    public void onDismiss(DialogInterface dialoginterface)
                    {
                        try
                        {
                            JSONObject jsonobject = new JSONObject();
                            jsonobject.put("type", "exit");
                            sendUpdate(jsonobject, false);
                            return;
                        }
                        catch (JSONException jsonexception)
                        {
                            Log.d("InAppBrowser", "Should never happen");
                        }
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                };
                dialog1.setOnDismissListener(ondismisslistener);
                LinearLayout linearlayout = new LinearLayout(cordova.getActivity());
                linearlayout.setOrientation(1);
                RelativeLayout relativelayout = new RelativeLayout(cordova.getActivity());
                relativelayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(-1, dpToPixels(44)));
                relativelayout.setPadding(dpToPixels(2), dpToPixels(2), dpToPixels(2), dpToPixels(2));
                relativelayout.setHorizontalGravity(3);
                relativelayout.setVerticalGravity(48);
                RelativeLayout relativelayout1 = new RelativeLayout(cordova.getActivity());
                relativelayout1.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(-2, -2));
                relativelayout1.setHorizontalGravity(3);
                relativelayout1.setVerticalGravity(16);
                relativelayout1.setId(1);
                Button button = new Button(cordova.getActivity());
                android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(-2, -1);
                layoutparams.addRule(5);
                button.setLayoutParams(layoutparams);
                button.setContentDescription("Back Button");
                button.setId(2);
                button.setText("<");
                android.view.View.OnClickListener onclicklistener = new android.view.View.OnClickListener() {

                    final _cls2 this$1;

                    public void onClick(View view)
                    {
                        goBack();
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                };
                button.setOnClickListener(onclicklistener);
                Button button1 = new Button(cordova.getActivity());
                android.widget.RelativeLayout.LayoutParams layoutparams1 = new android.widget.RelativeLayout.LayoutParams(-2, -1);
                layoutparams1.addRule(1, 2);
                button1.setLayoutParams(layoutparams1);
                button1.setContentDescription("Forward Button");
                button1.setId(3);
                button1.setText(">");
                android.view.View.OnClickListener onclicklistener1 = new android.view.View.OnClickListener() {

                    final _cls2 this$1;

                    public void onClick(View view)
                    {
                        goForward();
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                };
                button1.setOnClickListener(onclicklistener1);
                edittext = new EditText(cordova.getActivity());
                android.widget.RelativeLayout.LayoutParams layoutparams2 = new android.widget.RelativeLayout.LayoutParams(-1, -1);
                layoutparams2.addRule(1, 1);
                layoutparams2.addRule(0, 5);
                edittext.setLayoutParams(layoutparams2);
                edittext.setId(4);
                edittext.setSingleLine(true);
                edittext.setText(url);
                edittext.setInputType(16);
                edittext.setImeOptions(2);
                edittext.setInputType(0);
                EditText edittext1 = edittext;
                android.view.View.OnKeyListener onkeylistener = new android.view.View.OnKeyListener() {

                    final _cls2 this$1;

                    public boolean onKey(View view, int i, KeyEvent keyevent)
                    {
                        if (keyevent.getAction() == 0 && i == 66)
                        {
                            navigate(edittext.getText().toString());
                            return true;
                        } else
                        {
                            return false;
                        }
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                };
                edittext1.setOnKeyListener(onkeylistener);
                Button button2 = new Button(cordova.getActivity());
                android.widget.RelativeLayout.LayoutParams layoutparams3 = new android.widget.RelativeLayout.LayoutParams(-2, -1);
                layoutparams3.addRule(11);
                button2.setLayoutParams(layoutparams3);
                button1.setContentDescription("Close Button");
                button2.setId(5);
                button2.setText(buttonLabel);
                android.view.View.OnClickListener onclicklistener2 = new android.view.View.OnClickListener() {

                    final _cls2 this$1;

                    public void onClick(View view)
                    {
                        closeDialog();
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                };
                button2.setOnClickListener(onclicklistener2);
                inAppWebView = new WebView(cordova.getActivity());
                inAppWebView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1));
                inAppWebView.setWebChromeClient(new InAppChromeClient(thatWebView));
                InAppBrowserClient inappbrowserclient = new InAppBrowserClient(thatWebView, edittext);
                inAppWebView.setWebViewClient(inappbrowserclient);
                WebSettings websettings = inAppWebView.getSettings();
                websettings.setJavaScriptEnabled(true);
                websettings.setJavaScriptCanOpenWindowsAutomatically(true);
                websettings.setBuiltInZoomControls(true);
                websettings.setPluginsEnabled(true);
                Bundle bundle = cordova.getActivity().getIntent().getExtras();
                boolean flag;
                android.view.WindowManager.LayoutParams layoutparams4;
                if (bundle == null)
                {
                    flag = true;
                } else
                {
                    flag = bundle.getBoolean("InAppBrowserStorageEnabled", true);
                }
                if (flag)
                {
                    websettings.setDatabasePath(cordova.getActivity().getApplicationContext().getDir("inAppBrowserDB", 0).getPath());
                    websettings.setDatabaseEnabled(true);
                }
                websettings.setDomStorageEnabled(true);
                inAppWebView.loadUrl(url);
                inAppWebView.setId(6);
                inAppWebView.getSettings().setLoadWithOverviewMode(true);
                inAppWebView.getSettings().setUseWideViewPort(true);
                inAppWebView.requestFocus();
                inAppWebView.requestFocusFromTouch();
                relativelayout1.addView(button);
                relativelayout1.addView(button1);
                relativelayout.addView(relativelayout1);
                relativelayout.addView(edittext);
                relativelayout.addView(button2);
                if (getShowLocationBar())
                {
                    linearlayout.addView(relativelayout);
                }
                linearlayout.addView(inAppWebView);
                layoutparams4 = new android.view.WindowManager.LayoutParams();
                layoutparams4.copyFrom(dialog.getWindow().getAttributes());
                layoutparams4.width = -1;
                layoutparams4.height = -1;
                dialog.setContentView(linearlayout);
                dialog.show();
                dialog.getWindow().setAttributes(layoutparams4);
                if (openWindowHidden)
                {
                    dialog.hide();
                }
            }

            
            {
                this$0 = InAppBrowser.this;
                url = s;
                thatWebView = cordovawebview;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        return "";
    }



/*
    static Dialog access$002(InAppBrowser inappbrowser, Dialog dialog1)
    {
        inappbrowser.dialog = dialog1;
        return dialog1;
    }

*/









/*
    static EditText access$402(InAppBrowser inappbrowser, EditText edittext1)
    {
        inappbrowser.edittext = edittext1;
        return edittext1;
    }

*/






/*
    static WebView access$802(InAppBrowser inappbrowser, WebView webview)
    {
        inappbrowser.inAppWebView = webview;
        return webview;
    }

*/

}
