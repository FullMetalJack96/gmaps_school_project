// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.content.DialogInterface;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            Notification

class text
    implements Runnable
{

    final Notification this$0;
    final JSONArray val$buttonLabels;
    final CallbackContext val$callbackContext;
    final CordovaInterface val$cordova;
    final String val$message;
    final String val$title;

    public void run()
    {
        android.app.lder lder = new android.app.lder(val$cordova.getActivity());
        lder.setMessage(val$message);
        lder.setTitle(val$title);
        lder.setCancelable(true);
        if (val$buttonLabels.length() > 0)
        {
            try
            {
                lder.setNegativeButton(val$buttonLabels.getString(0), new android.content.DialogInterface.OnClickListener() {

                    final Notification._cls2 this$1;

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 1));
                    }

            
            {
                this$1 = Notification._cls2.this;
                super();
            }
                });
            }
            catch (JSONException jsonexception2) { }
        }
        if (val$buttonLabels.length() > 1)
        {
            try
            {
                lder.setNeutralButton(val$buttonLabels.getString(1), new android.content.DialogInterface.OnClickListener() {

                    final Notification._cls2 this$1;

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 2));
                    }

            
            {
                this$1 = Notification._cls2.this;
                super();
            }
                });
            }
            catch (JSONException jsonexception1) { }
        }
        if (val$buttonLabels.length() > 2)
        {
            try
            {
                lder.setPositiveButton(val$buttonLabels.getString(2), new android.content.DialogInterface.OnClickListener() {

                    final Notification._cls2 this$1;

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 3));
                    }

            
            {
                this$1 = Notification._cls2.this;
                super();
            }
                });
            }
            catch (JSONException jsonexception) { }
        }
        lder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

            final Notification._cls2 this$1;

            public void onCancel(DialogInterface dialoginterface)
            {
                dialoginterface.dismiss();
                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 0));
            }

            
            {
                this$1 = Notification._cls2.this;
                super();
            }
        });
        lder.create();
        lder.show();
    }

    text()
    {
        this$0 = final_notification;
        val$cordova = cordovainterface;
        val$message = s;
        val$title = s1;
        val$buttonLabels = jsonarray;
        val$callbackContext = CallbackContext.this;
        super();
    }
}
