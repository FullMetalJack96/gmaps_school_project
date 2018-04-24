// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.io.File;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            InAppBrowser, CordovaWebView

class this._cls1
    implements android.view.ner
{

    final is._cls0 this$1;

    public void onClick(View view)
    {
        InAppBrowser.access$700(_fld0);
    }

    l.thatWebView()
    {
        this$1 = this._cls1.this;
        super();
    }

    // Unreferenced inner class org/apache/cordova/InAppBrowser$2

/* anonymous class */
    class InAppBrowser._cls2
        implements Runnable
    {

        final InAppBrowser this$0;
        final CordovaWebView val$thatWebView;
        final String val$url;

        private int dpToPixels(int i)
        {
            return (int)TypedValue.applyDimension(1, i, cordova.getActivity().getResources().getDisplayMetrics());
        }

        public void run()
        {
            InAppBrowser.access$002(InAppBrowser.this, new Dialog(cordova.getActivity(), 0x1030006));
            InAppBrowser.access$000(InAppBrowser.this).getWindow().getAttributes().windowAnimations = 0x1030002;
            InAppBrowser.access$000(InAppBrowser.this).requestWindowFeature(1);
            InAppBrowser.access$000(InAppBrowser.this).setCancelable(true);
            Dialog dialog = InAppBrowser.access$000(InAppBrowser.this);
            android.content.DialogInterface.OnDismissListener ondismisslistener = new InAppBrowser._cls2._cls1();
            dialog.setOnDismissListener(ondismisslistener);
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
            android.view.View.OnClickListener onclicklistener = new InAppBrowser._cls2._cls2();
            button.setOnClickListener(onclicklistener);
            Button button1 = new Button(cordova.getActivity());
            android.widget.RelativeLayout.LayoutParams layoutparams1 = new android.widget.RelativeLayout.LayoutParams(-2, -1);
            layoutparams1.addRule(1, 2);
            button1.setLayoutParams(layoutparams1);
            button1.setContentDescription("Forward Button");
            button1.setId(3);
            button1.setText(">");
            android.view.View.OnClickListener onclicklistener1 = new InAppBrowser._cls2._cls3();
            button1.setOnClickListener(onclicklistener1);
            InAppBrowser.access$402(InAppBrowser.this, new EditText(cordova.getActivity()));
            android.widget.RelativeLayout.LayoutParams layoutparams2 = new android.widget.RelativeLayout.LayoutParams(-1, -1);
            layoutparams2.addRule(1, 1);
            layoutparams2.addRule(0, 5);
            InAppBrowser.access$400(InAppBrowser.this).setLayoutParams(layoutparams2);
            InAppBrowser.access$400(InAppBrowser.this).setId(4);
            InAppBrowser.access$400(InAppBrowser.this).setSingleLine(true);
            InAppBrowser.access$400(InAppBrowser.this).setText(url);
            InAppBrowser.access$400(InAppBrowser.this).setInputType(16);
            InAppBrowser.access$400(InAppBrowser.this).setImeOptions(2);
            InAppBrowser.access$400(InAppBrowser.this).setInputType(0);
            EditText edittext = InAppBrowser.access$400(InAppBrowser.this);
            android.view.View.OnKeyListener onkeylistener = new InAppBrowser._cls2._cls4();
            edittext.setOnKeyListener(onkeylistener);
            Button button2 = new Button(cordova.getActivity());
            android.widget.RelativeLayout.LayoutParams layoutparams3 = new android.widget.RelativeLayout.LayoutParams(-2, -1);
            layoutparams3.addRule(11);
            button2.setLayoutParams(layoutparams3);
            button1.setContentDescription("Close Button");
            button2.setId(5);
            button2.setText(InAppBrowser.access$600(InAppBrowser.this));
            InAppBrowser._cls2._cls5 _lcls5 = new InAppBrowser._cls2._cls5();
            button2.setOnClickListener(_lcls5);
            InAppBrowser.access$802(InAppBrowser.this, new WebView(cordova.getActivity()));
            InAppBrowser.access$800(InAppBrowser.this).setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1));
            InAppBrowser.access$800(InAppBrowser.this).setWebChromeClient(new InAppBrowser.InAppChromeClient(InAppBrowser.this, thatWebView));
            InAppBrowser.InAppBrowserClient inappbrowserclient = new InAppBrowser.InAppBrowserClient(InAppBrowser.this, thatWebView, InAppBrowser.access$400(InAppBrowser.this));
            InAppBrowser.access$800(InAppBrowser.this).setWebViewClient(inappbrowserclient);
            WebSettings websettings = InAppBrowser.access$800(InAppBrowser.this).getSettings();
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
            InAppBrowser.access$800(InAppBrowser.this).loadUrl(url);
            InAppBrowser.access$800(InAppBrowser.this).setId(6);
            InAppBrowser.access$800(InAppBrowser.this).getSettings().setLoadWithOverviewMode(true);
            InAppBrowser.access$800(InAppBrowser.this).getSettings().setUseWideViewPort(true);
            InAppBrowser.access$800(InAppBrowser.this).requestFocus();
            InAppBrowser.access$800(InAppBrowser.this).requestFocusFromTouch();
            relativelayout1.addView(button);
            relativelayout1.addView(button1);
            relativelayout.addView(relativelayout1);
            relativelayout.addView(InAppBrowser.access$400(InAppBrowser.this));
            relativelayout.addView(button2);
            if (InAppBrowser.access$900(InAppBrowser.this))
            {
                linearlayout.addView(relativelayout);
            }
            linearlayout.addView(InAppBrowser.access$800(InAppBrowser.this));
            layoutparams4 = new android.view.WindowManager.LayoutParams();
            layoutparams4.copyFrom(InAppBrowser.access$000(InAppBrowser.this).getWindow().getAttributes());
            layoutparams4.width = -1;
            layoutparams4.height = -1;
            InAppBrowser.access$000(InAppBrowser.this).setContentView(linearlayout);
            InAppBrowser.access$000(InAppBrowser.this).show();
            InAppBrowser.access$000(InAppBrowser.this).getWindow().setAttributes(layoutparams4);
            if (InAppBrowser.access$1000(InAppBrowser.this))
            {
                InAppBrowser.access$000(InAppBrowser.this).hide();
            }
        }

            
            {
                this$0 = final_inappbrowser;
                url = s;
                thatWebView = CordovaWebView.this;
                super();
            }

        // Unreferenced inner class org/apache/cordova/InAppBrowser$2$1

/* anonymous class */
        class InAppBrowser._cls2._cls1
            implements android.content.DialogInterface.OnDismissListener
        {

            final InAppBrowser._cls2 this$1;

            public void onDismiss(DialogInterface dialoginterface)
            {
                try
                {
                    JSONObject jsonobject = new JSONObject();
                    jsonobject.put("type", "exit");
                    InAppBrowser.access$100(this$0, jsonobject, false);
                    return;
                }
                catch (JSONException jsonexception)
                {
                    Log.d("InAppBrowser", "Should never happen");
                }
            }

                    
                    {
                        this$1 = InAppBrowser._cls2.this;
                        super();
                    }
        }


        // Unreferenced inner class org/apache/cordova/InAppBrowser$2$2

/* anonymous class */
        class InAppBrowser._cls2._cls2
            implements android.view.View.OnClickListener
        {

            final InAppBrowser._cls2 this$1;

            public void onClick(View view)
            {
                InAppBrowser.access$200(this$0);
            }

                    
                    {
                        this$1 = InAppBrowser._cls2.this;
                        super();
                    }
        }


        // Unreferenced inner class org/apache/cordova/InAppBrowser$2$3

/* anonymous class */
        class InAppBrowser._cls2._cls3
            implements android.view.View.OnClickListener
        {

            final InAppBrowser._cls2 this$1;

            public void onClick(View view)
            {
                InAppBrowser.access$300(this$0);
            }

                    
                    {
                        this$1 = InAppBrowser._cls2.this;
                        super();
                    }
        }


        // Unreferenced inner class org/apache/cordova/InAppBrowser$2$4

/* anonymous class */
        class InAppBrowser._cls2._cls4
            implements android.view.View.OnKeyListener
        {

            final InAppBrowser._cls2 this$1;

            public boolean onKey(View view, int i, KeyEvent keyevent)
            {
                if (keyevent.getAction() == 0 && i == 66)
                {
                    InAppBrowser.access$500(this$0, InAppBrowser.access$400(this$0).getText().toString());
                    return true;
                } else
                {
                    return false;
                }
            }

                    
                    {
                        this$1 = InAppBrowser._cls2.this;
                        super();
                    }
        }

    }

}
