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

class this._cls1
    implements android.content.nCancelListener
{

    final Status this$1;

    public void onCancel(DialogInterface dialoginterface)
    {
        dialoginterface.dismiss();
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.us.OK, 0));
    }

    xt()
    {
        this$1 = this._cls1.this;
        super();
    }

    // Unreferenced inner class org/apache/cordova/Notification$2

/* anonymous class */
    class Notification._cls2
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cordova.getActivity());
            builder.setMessage(message);
            builder.setTitle(title);
            builder.setCancelable(true);
            if (buttonLabels.length() > 0)
            {
                try
                {
                    builder.setNegativeButton(buttonLabels.getString(0), new Notification._cls2._cls1());
                }
                catch (JSONException jsonexception2) { }
            }
            if (buttonLabels.length() > 1)
            {
                try
                {
                    builder.setNeutralButton(buttonLabels.getString(1), new Notification._cls2._cls2());
                }
                catch (JSONException jsonexception1) { }
            }
            if (buttonLabels.length() > 2)
            {
                try
                {
                    builder.setPositiveButton(buttonLabels.getString(2), new Notification._cls2._cls3());
                }
                catch (JSONException jsonexception) { }
            }
            builder.setOnCancelListener(new Notification._cls2._cls4());
            builder.create();
            builder.show();
        }

            
            {
                this$0 = final_notification;
                cordova = cordovainterface;
                message = s;
                title = s1;
                buttonLabels = jsonarray;
                callbackContext = CallbackContext.this;
                super();
            }

        // Unreferenced inner class org/apache/cordova/Notification$2$1

/* anonymous class */
        class Notification._cls2._cls1
            implements android.content.DialogInterface.OnClickListener
        {

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
        }


        // Unreferenced inner class org/apache/cordova/Notification$2$2

/* anonymous class */
        class Notification._cls2._cls2
            implements android.content.DialogInterface.OnClickListener
        {

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
        }


        // Unreferenced inner class org/apache/cordova/Notification$2$3

/* anonymous class */
        class Notification._cls2._cls3
            implements android.content.DialogInterface.OnClickListener
        {

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
        }

    }

}
