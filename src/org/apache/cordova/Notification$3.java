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

class text
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
        android.app.lder lder = new android.app.lder(val$cordova.getActivity());
        lder.setMessage(val$message);
        lder.setTitle(val$title);
        lder.setCancelable(true);
        lder.setView(val$promptInput);
        final JSONObject result = new JSONObject();
        if (val$buttonLabels.length() > 0)
        {
            try
            {
                lder.setNegativeButton(val$buttonLabels.getString(0), new android.content.DialogInterface.OnClickListener() {

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
                        JSONException jsonexception3;
                        jsonexception3;
                        jsonexception3.printStackTrace();
                          goto _L4
                    }

            
            {
                this$1 = Notification._cls3.this;
                result = jsonobject;
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

                    final Notification._cls3 this$1;
                    final JSONObject val$result;

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                        JSONObject jsonobject;
                        result.put("buttonIndex", 2);
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
                        JSONException jsonexception3;
                        jsonexception3;
                        jsonexception3.printStackTrace();
                          goto _L4
                    }

            
            {
                this$1 = Notification._cls3.this;
                result = jsonobject;
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
                        JSONException jsonexception3;
                        jsonexception3;
                        jsonexception3.printStackTrace();
                          goto _L4
                    }

            
            {
                this$1 = Notification._cls3.this;
                result = jsonobject;
                super();
            }
                });
            }
            catch (JSONException jsonexception) { }
        }
        lder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

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
                JSONException jsonexception3;
                jsonexception3;
                jsonexception3.printStackTrace();
                  goto _L4
            }

            
            {
                this$1 = Notification._cls3.this;
                result = jsonobject;
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
        val$promptInput = edittext;
        val$buttonLabels = jsonarray;
        val$defaultText = s2;
        val$callbackContext = CallbackContext.this;
        super();
    }
}
