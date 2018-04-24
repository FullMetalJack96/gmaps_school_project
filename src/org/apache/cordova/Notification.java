// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.widget.EditText;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification extends CordovaPlugin
{

    public int confirmResult;
    public ProgressDialog progressDialog;
    public ProgressDialog spinnerDialog;

    public Notification()
    {
        confirmResult = -1;
        spinnerDialog = null;
        progressDialog = null;
    }

    public void activityStart(String s, String s1)
    {
        this;
        JVM INSTR monitorenter ;
        if (spinnerDialog != null)
        {
            spinnerDialog.dismiss();
            spinnerDialog = null;
        }
        Runnable runnable = new Runnable() {

            final Notification this$0;
            final CordovaInterface val$cordova;
            final String val$message;
            final String val$title;

            public void run()
            {
                spinnerDialog = ProgressDialog.show(cordova.getActivity(), title, message, true, true, new android.content.DialogInterface.OnCancelListener() {

                    final _cls4 this$1;

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        spinnerDialog = null;
                    }

            
            {
                this$1 = _cls4.this;
                super();
            }
                });
            }

            
            {
                this$0 = Notification.this;
                cordova = cordovainterface;
                title = s;
                message = s1;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void activityStop()
    {
        this;
        JVM INSTR monitorenter ;
        if (spinnerDialog != null)
        {
            spinnerDialog.dismiss();
            spinnerDialog = null;
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void alert(String s, String s1, String s2, CallbackContext callbackcontext)
    {
        this;
        JVM INSTR monitorenter ;
        Runnable runnable = new Runnable() {

            final Notification this$0;
            final String val$buttonLabel;
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
                builder.setPositiveButton(buttonLabel, new android.content.DialogInterface.OnClickListener() {

                    final _cls1 this$1;

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 0));
                    }

            
            {
                this$1 = _cls1.this;
                super();
            }
                });
                builder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    final _cls1 this$1;

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 0));
                    }

            
            {
                this$1 = _cls1.this;
                super();
            }
                });
                builder.create();
                builder.show();
            }

            
            {
                this$0 = Notification.this;
                cordova = cordovainterface;
                message = s;
                title = s1;
                buttonLabel = s2;
                callbackContext = callbackcontext;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void beep(long l)
    {
        android.net.Uri uri = RingtoneManager.getDefaultUri(2);
        Ringtone ringtone = RingtoneManager.getRingtone(cordova.getActivity().getBaseContext(), uri);
        if (ringtone != null)
        {
            for (long l1 = 0L; l1 < l; l1++)
            {
                ringtone.play();
                for (long l2 = 5000L; ringtone.isPlaying() && l2 > 0L;)
                {
                    l2 -= 100L;
                    try
                    {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException interruptedexception) { }
                }

            }

        }
    }

    public void confirm(String s, String s1, JSONArray jsonarray, CallbackContext callbackcontext)
    {
        this;
        JVM INSTR monitorenter ;
        Runnable runnable = new Runnable() {

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
                        builder.setNegativeButton(buttonLabels.getString(0), new android.content.DialogInterface.OnClickListener() {

                            final _cls2 this$1;

                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                dialoginterface.dismiss();
                                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 1));
                            }

            
            {
                this$1 = _cls2.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception2) { }
                }
                if (buttonLabels.length() > 1)
                {
                    try
                    {
                        builder.setNeutralButton(buttonLabels.getString(1), new android.content.DialogInterface.OnClickListener() {

                            final _cls2 this$1;

                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                dialoginterface.dismiss();
                                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 2));
                            }

            
            {
                this$1 = _cls2.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception1) { }
                }
                if (buttonLabels.length() > 2)
                {
                    try
                    {
                        builder.setPositiveButton(buttonLabels.getString(2), new android.content.DialogInterface.OnClickListener() {

                            final _cls2 this$1;

                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                dialoginterface.dismiss();
                                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 3));
                            }

            
            {
                this$1 = _cls2.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception) { }
                }
                builder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    final _cls2 this$1;

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        dialoginterface.dismiss();
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, 0));
                    }

            
            {
                this$1 = _cls2.this;
                super();
            }
                });
                builder.create();
                builder.show();
            }

            
            {
                this$0 = Notification.this;
                cordova = cordovainterface;
                message = s;
                title = s1;
                buttonLabels = jsonarray;
                callbackContext = callbackcontext;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (!s.equals("beep")) goto _L2; else goto _L1
_L1:
        beep(jsonarray.getLong(0));
_L6:
        boolean flag1;
        callbackcontext.success();
        flag1 = true;
_L4:
        return flag1;
_L2:
        boolean flag;
        if (s.equals("vibrate"))
        {
            vibrate(jsonarray.getLong(0));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("alert"))
        {
            alert(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getString(2), callbackcontext);
            return true;
        }
        if (s.equals("confirm"))
        {
            confirm(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getJSONArray(2), callbackcontext);
            return true;
        }
        if (s.equals("prompt"))
        {
            prompt(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getJSONArray(2), jsonarray.getString(3), callbackcontext);
            return true;
        }
        if (s.equals("activityStart"))
        {
            activityStart(jsonarray.getString(0), jsonarray.getString(1));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("activityStop"))
        {
            activityStop();
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("progressStart"))
        {
            progressStart(jsonarray.getString(0), jsonarray.getString(1));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("progressValue"))
        {
            progressValue(jsonarray.getInt(0));
            continue; /* Loop/switch isn't completed */
        }
        flag = s.equals("progressStop");
        flag1 = false;
        if (!flag) goto _L4; else goto _L3
_L3:
        progressStop();
        if (true) goto _L6; else goto _L5
_L5:
    }

    public void progressStart(String s, String s1)
    {
        this;
        JVM INSTR monitorenter ;
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
        Runnable runnable = new Runnable() {

            final Notification this$0;
            final CordovaInterface val$cordova;
            final String val$message;
            final Notification val$notification;
            final String val$title;

            public void run()
            {
                notification.progressDialog = new ProgressDialog(cordova.getActivity());
                notification.progressDialog.setProgressStyle(1);
                notification.progressDialog.setTitle(title);
                notification.progressDialog.setMessage(message);
                notification.progressDialog.setCancelable(true);
                notification.progressDialog.setMax(100);
                notification.progressDialog.setProgress(0);
                notification.progressDialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    final _cls5 this$1;

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        notification.progressDialog = null;
                    }

            
            {
                this$1 = _cls5.this;
                super();
            }
                });
                notification.progressDialog.show();
            }

            
            {
                this$0 = Notification.this;
                notification = notification2;
                cordova = cordovainterface;
                title = s;
                message = s1;
                super();
            }
        };
        cordova.getActivity().runOnUiThread(runnable);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void progressStop()
    {
        this;
        JVM INSTR monitorenter ;
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void progressValue(int i)
    {
        this;
        JVM INSTR monitorenter ;
        if (progressDialog != null)
        {
            progressDialog.setProgress(i);
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void prompt(final String message, final String title, final JSONArray buttonLabels, final String defaultText, final CallbackContext callbackContext)
    {
        this;
        JVM INSTR monitorenter ;
        final CordovaInterface cordova = this.cordova;
        final EditText promptInput = new EditText(cordova.getActivity());
        promptInput.setHint(defaultText);
        Runnable runnable = new Runnable() {

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
                JSONObject jsonobject = new JSONObject();
                if (buttonLabels.length() > 0)
                {
                    try
                    {
                        builder.setNegativeButton(buttonLabels.getString(0), jsonobject. new android.content.DialogInterface.OnClickListener() {

                            final _cls3 this$1;
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
                this$1 = final__pcls3;
                result = JSONObject.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception2) { }
                }
                if (buttonLabels.length() > 1)
                {
                    try
                    {
                        builder.setNeutralButton(buttonLabels.getString(1), jsonobject. new android.content.DialogInterface.OnClickListener() {

                            final _cls3 this$1;
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
                                JSONException jsonexception;
                                jsonexception;
                                jsonexception.printStackTrace();
                                  goto _L4
                            }

            
            {
                this$1 = final__pcls3;
                result = JSONObject.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception1) { }
                }
                if (buttonLabels.length() > 2)
                {
                    try
                    {
                        builder.setPositiveButton(buttonLabels.getString(2), jsonobject. new android.content.DialogInterface.OnClickListener() {

                            final _cls3 this$1;
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
                this$1 = final__pcls3;
                result = JSONObject.this;
                super();
            }
                        });
                    }
                    catch (JSONException jsonexception) { }
                }
                builder.setOnCancelListener(jsonobject. new android.content.DialogInterface.OnCancelListener() {

                    final _cls3 this$1;
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
                this$1 = final__pcls3;
                result = JSONObject.this;
                super();
            }
                });
                builder.create();
                builder.show();
            }

            
            {
                this$0 = Notification.this;
                cordova = cordovainterface;
                message = s;
                title = s1;
                promptInput = edittext;
                buttonLabels = jsonarray;
                defaultText = s2;
                callbackContext = callbackcontext;
                super();
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void vibrate(long l)
    {
        if (l == 0L)
        {
            l = 500L;
        }
        ((Vibrator)cordova.getActivity().getSystemService("vibrator")).vibrate(l);
    }
}
