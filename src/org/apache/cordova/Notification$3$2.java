// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.content.DialogInterface;
import android.widget.EditText;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            Notification

class val.result
    implements android.content.nClickListener
{

    final ce this$1;
    final JSONObject val$result;

    public void onClick(DialogInterface dialoginterface, int i)
    {
        dialoginterface.dismiss();
        JSONObject jsonobject;
        val$result.put("buttonIndex", 2);
        jsonobject = val$result;
        if (promptInput.getText().toString().trim().length() != 0) goto _L2; else goto _L1
_L1:
        Object obj = defaultText;
_L3:
        jsonobject.put("input1", obj);
_L4:
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.us.OK, val$result));
        return;
_L2:
        android.text.Editable editable = promptInput.getText();
        obj = editable;
          goto _L3
        JSONException jsonexception;
        jsonexception;
        jsonexception.printStackTrace();
          goto _L4
    }

    xt()
    {
        this$1 = final_xt;
        val$result = JSONObject.this;
        super();
    }

    // Unreferenced inner class org/apache/cordova/Notification$3

/* anonymous class */
    class Notification._cls3
        implements Runnable
    {

        final Notification this$0;
        final JSONArray val$buttonLabels;
        final CallbackContext val$callbackContext;
        final CordovaInterface val$cordova;
        final String val$defaultText;
        final String val$message;
        final EditText val$promptInput;
        final String val$title;

        public void run()
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cordova.getActivity());
            builder.setMessage(message);
            builder.setTitle(title);
            builder.setCancelable(true);
            builder.setView(promptInput);
            final JSONObject result = new JSONObject();
            if (buttonLabels.length() > 0)
            {
                try
                {
                    builder.setNegativeButton(buttonLabels.getString(0), new Notification._cls3._cls1());
                }
                catch (JSONException jsonexception2) { }
            }
            if (buttonLabels.length() > 1)
            {
                try
                {
                    builder.setNeutralButton(buttonLabels.getString(1), result. new Notification._cls3._cls2());
                }
                catch (JSONException jsonexception1) { }
            }
            if (buttonLabels.length() > 2)
            {
                try
                {
                    builder.setPositiveButton(buttonLabels.getString(2), new Notification._cls3._cls3());
                }
                catch (JSONException jsonexception) { }
            }
            builder.setOnCancelListener(new Notification._cls3._cls4());
            builder.create();
            builder.show();
        }

            
            {
                this$0 = final_notification;
                cordova = cordovainterface;
                message = s;
                title = s1;
                promptInput = edittext;
                buttonLabels = jsonarray;
                defaultText = s2;
                callbackContext = CallbackContext.this;
                super();
            }

        // Unreferenced inner class org/apache/cordova/Notification$3$1

/* anonymous class */
        class Notification._cls3._cls1
            implements android.content.DialogInterface.OnClickListener
        {

            final Notification._cls3 this$1;
            final JSONObject val$result;

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.dismiss();
                JSONObject jsonobject;
                result.put("buttonIndex", 1);
                jsonobject = result;
                if (promptInput.getText().toString().trim().length() != 0) goto _L2; else goto _L1
_L1:
                Object obj = defaultText;
_L3:
                jsonobject.put("input1", obj);
_L4:
                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, result));
                return;
_L2:
                android.text.Editable editable = promptInput.getText();
                obj = editable;
                  goto _L3
                JSONException jsonexception;
                jsonexception;
                jsonexception.printStackTrace();
                  goto _L4
            }

                    
                    {
                        this$1 = Notification._cls3.this;
                        result = jsonobject;
                        super();
                    }
        }


        // Unreferenced inner class org/apache/cordova/Notification$3$3

/* anonymous class */
        class Notification._cls3._cls3
            implements android.content.DialogInterface.OnClickListener
        {

            final Notification._cls3 this$1;
            final JSONObject val$result;

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.dismiss();
                JSONObject jsonobject;
                result.put("buttonIndex", 3);
                jsonobject = result;
                if (promptInput.getText().toString().trim().length() != 0) goto _L2; else goto _L1
_L1:
                Object obj = defaultText;
_L3:
                jsonobject.put("input1", obj);
_L4:
                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, result));
                return;
_L2:
                android.text.Editable editable = promptInput.getText();
                obj = editable;
                  goto _L3
                JSONException jsonexception;
                jsonexception;
                jsonexception.printStackTrace();
                  goto _L4
            }

                    
                    {
                        this$1 = Notification._cls3.this;
                        result = jsonobject;
                        super();
                    }
        }


        // Unreferenced inner class org/apache/cordova/Notification$3$4

/* anonymous class */
        class Notification._cls3._cls4
            implements android.content.DialogInterface.OnCancelListener
        {

            final Notification._cls3 this$1;
            final JSONObject val$result;

            public void onCancel(DialogInterface dialoginterface)
            {
                dialoginterface.dismiss();
                JSONObject jsonobject;
                result.put("buttonIndex", 0);
                jsonobject = result;
                if (promptInput.getText().toString().trim().length() != 0) goto _L2; else goto _L1
_L1:
                Object obj = defaultText;
_L3:
                jsonobject.put("input1", obj);
_L4:
                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, result));
                return;
_L2:
                android.text.Editable editable = promptInput.getText();
                obj = editable;
                  goto _L3
                JSONException jsonexception;
                jsonexception;
                jsonexception.printStackTrace();
                  goto _L4
            }

                    
                    {
                        this$1 = Notification._cls3.this;
                        result = jsonobject;
                        super();
                    }
        }

    }

}
