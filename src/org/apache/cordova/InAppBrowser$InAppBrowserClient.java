// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            InAppBrowser, CordovaWebView

public class edittext extends WebViewClient
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
            InAppBrowser.access$100(InAppBrowser.this, jsonobject, true);
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
            InAppBrowser.access$100(InAppBrowser.this, jsonobject, true);
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
            InAppBrowser.access$1200(InAppBrowser.this, jsonobject, true, org.apache.cordova.api.._fld0);
            return;
        }
        catch (JSONException jsonexception)
        {
            Log.d("InAppBrowser", "Should never happen");
        }
    }

    public (CordovaWebView cordovawebview, EditText edittext1)
    {
        this$0 = InAppBrowser.this;
        super();
        webView = cordovawebview;
        edittext = edittext1;
    }
}
