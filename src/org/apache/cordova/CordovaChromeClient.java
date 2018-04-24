// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView, Config, ExposedJsApi

public class CordovaChromeClient extends WebChromeClient
{

    public static final int FILECHOOSER_RESULTCODE = 5173;
    private static final String LOG_TAG = "CordovaChromeClient";
    private long MAX_QUOTA;
    private String TAG;
    private CordovaWebView appView;
    private CordovaInterface cordova;
    public ValueCallback mUploadMessage;
    private View mVideoProgressView;

    public CordovaChromeClient(CordovaInterface cordovainterface)
    {
        TAG = "CordovaLog";
        MAX_QUOTA = 0x6400000L;
        cordova = cordovainterface;
    }

    public CordovaChromeClient(CordovaInterface cordovainterface, CordovaWebView cordovawebview)
    {
        TAG = "CordovaLog";
        MAX_QUOTA = 0x6400000L;
        cordova = cordovainterface;
        appView = cordovawebview;
    }

    public ValueCallback getValueCallback()
    {
        return mUploadMessage;
    }

    public View getVideoLoadingProgressView()
    {
        if (mVideoProgressView == null)
        {
            LinearLayout linearlayout = new LinearLayout(appView.getContext());
            linearlayout.setOrientation(1);
            android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(-2, -2);
            layoutparams.addRule(13);
            linearlayout.setLayoutParams(layoutparams);
            ProgressBar progressbar = new ProgressBar(appView.getContext());
            android.widget.LinearLayout.LayoutParams layoutparams1 = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutparams1.gravity = 17;
            progressbar.setLayoutParams(layoutparams1);
            linearlayout.addView(progressbar);
            mVideoProgressView = linearlayout;
        }
        return mVideoProgressView;
    }

    public void onConsoleMessage(String s, int i, String s1)
    {
        if (android.os.Build.VERSION.SDK_INT == 7)
        {
            String s2 = TAG;
            Object aobj[] = new Object[3];
            aobj[0] = s1;
            aobj[1] = Integer.valueOf(i);
            aobj[2] = s;
            LOG.d(s2, "%s: Line %d : %s", aobj);
            super.onConsoleMessage(s, i, s1);
        }
    }

    public boolean onConsoleMessage(ConsoleMessage consolemessage)
    {
        if (consolemessage.message() != null)
        {
            String s = TAG;
            Object aobj[] = new Object[3];
            aobj[0] = consolemessage.sourceId();
            aobj[1] = Integer.valueOf(consolemessage.lineNumber());
            aobj[2] = consolemessage.message();
            LOG.d(s, "%s: Line %d : %s", aobj);
        }
        return super.onConsoleMessage(consolemessage);
    }

    public void onExceededDatabaseQuota(String s, String s1, long l, long l1, long l2, android.webkit.WebStorage.QuotaUpdater quotaupdater)
    {
        String s2 = TAG;
        Object aobj[] = new Object[3];
        aobj[0] = Long.valueOf(l1);
        aobj[1] = Long.valueOf(l);
        aobj[2] = Long.valueOf(l2);
        LOG.d(s2, "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", aobj);
        if (l1 < MAX_QUOTA)
        {
            String s3 = TAG;
            Object aobj1[] = new Object[1];
            aobj1[0] = Long.valueOf(l1);
            LOG.d(s3, "calling quotaUpdater.updateQuota newQuota: %d", aobj1);
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

    public void onHideCustomView()
    {
        appView.hideCustomView();
    }

    public boolean onJsAlert(WebView webview, String s, String s1, final JsResult result)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cordova.getActivity());
        builder.setMessage(s1);
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setPositiveButton(0x104000a, new android.content.DialogInterface.OnClickListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public void onClick(DialogInterface dialoginterface, int i)
            {
                result.confirm();
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public void onCancel(DialogInterface dialoginterface)
            {
                result.cancel();
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent)
            {
                if (i == 4)
                {
                    result.confirm();
                    return false;
                } else
                {
                    return true;
                }
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    public boolean onJsConfirm(WebView webview, String s, String s1, final JsResult result)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cordova.getActivity());
        builder.setMessage(s1);
        builder.setTitle("Confirm");
        builder.setCancelable(true);
        builder.setPositiveButton(0x104000a, new android.content.DialogInterface.OnClickListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public void onClick(DialogInterface dialoginterface, int i)
            {
                result.confirm();
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.setNegativeButton(0x1040000, new android.content.DialogInterface.OnClickListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public void onClick(DialogInterface dialoginterface, int i)
            {
                result.cancel();
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public void onCancel(DialogInterface dialoginterface)
            {
                result.cancel();
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

            final CordovaChromeClient this$0;
            final JsResult val$result;

            public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent)
            {
                if (i == 4)
                {
                    result.cancel();
                    return false;
                } else
                {
                    return true;
                }
            }

            
            {
                this$0 = CordovaChromeClient.this;
                result = jsresult;
                super();
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    public boolean onJsPrompt(WebView webview, String s, String s1, String s2, final JsPromptResult res)
    {
        boolean flag;
label0:
        {
            if (!s.startsWith("file://"))
            {
                boolean flag1 = Config.isUrlWhiteListed(s);
                flag = false;
                if (!flag1)
                {
                    break label0;
                }
            }
            flag = true;
        }
        if (!flag || s2 == null || s2.length() <= 3 || !s2.substring(0, 4).equals("gap:")) goto _L2; else goto _L1
_L1:
        String s7;
        try
        {
            JSONArray jsonarray = new JSONArray(s2.substring(4));
            String s4 = jsonarray.getString(0);
            String s5 = jsonarray.getString(1);
            String s6 = jsonarray.getString(2);
            s7 = appView.exposedJsApi.exec(s4, s5, s6, s1);
        }
        catch (JSONException jsonexception)
        {
            jsonexception.printStackTrace();
            return false;
        }
        if (s7 == null)
        {
            s7 = "";
        }
        res.confirm(s7);
_L4:
        return true;
_L2:
        if (flag && s2 != null && s2.equals("gap_bridge_mode:"))
        {
            appView.exposedJsApi.setNativeToJsBridgeMode(Integer.parseInt(s1));
            res.confirm("");
        } else
        if (flag && s2 != null && s2.equals("gap_poll:"))
        {
            String s3 = appView.exposedJsApi.retrieveJsMessages();
            if (s3 == null)
            {
                s3 = "";
            }
            res.confirm(s3);
        } else
        if (s2 != null && s2.equals("gap_init:"))
        {
            res.confirm("OK");
        } else
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cordova.getActivity());
            builder.setMessage(s1);
            final EditText input = new EditText(cordova.getActivity());
            if (s2 != null)
            {
                input.setText(s2);
            }
            builder.setView(input);
            builder.setCancelable(false);
            builder.setPositiveButton(0x104000a, new android.content.DialogInterface.OnClickListener() {

                final CordovaChromeClient this$0;
                final EditText val$input;
                final JsPromptResult val$res;

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    String s8 = input.getText().toString();
                    res.confirm(s8);
                }

            
            {
                this$0 = CordovaChromeClient.this;
                input = edittext;
                res = jspromptresult;
                super();
            }
            });
            builder.setNegativeButton(0x1040000, new android.content.DialogInterface.OnClickListener() {

                final CordovaChromeClient this$0;
                final JsPromptResult val$res;

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    res.cancel();
                }

            
            {
                this$0 = CordovaChromeClient.this;
                res = jspromptresult;
                super();
            }
            });
            builder.create();
            builder.show();
        }
        if (true) goto _L4; else goto _L3
_L3:
    }

    public void onShowCustomView(View view, android.webkit.WebChromeClient.CustomViewCallback customviewcallback)
    {
        appView.showCustomView(view, customviewcallback);
    }

    public void openFileChooser(ValueCallback valuecallback)
    {
        openFileChooser(valuecallback, "*/*");
    }

    public void openFileChooser(ValueCallback valuecallback, String s)
    {
        openFileChooser(valuecallback, s, null);
    }

    public void openFileChooser(ValueCallback valuecallback, String s, String s1)
    {
        mUploadMessage = valuecallback;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        cordova.getActivity().startActivityForResult(Intent.createChooser(intent, "File Browser"), 5173);
    }

    public void setWebView(CordovaWebView cordovawebview)
    {
        appView = cordovawebview;
    }
}
