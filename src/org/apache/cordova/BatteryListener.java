// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BatteryListener extends CordovaPlugin
{

    private static final String LOG_TAG = "BatteryManager";
    private CallbackContext batteryCallbackContext;
    BroadcastReceiver receiver;

    public BatteryListener()
    {
        batteryCallbackContext = null;
        receiver = null;
    }

    private JSONObject getBatteryInfo(Intent intent)
    {
        JSONObject jsonobject = new JSONObject();
        int i;
        boolean flag;
        try
        {
            jsonobject.put("level", intent.getIntExtra("level", 0));
            i = intent.getIntExtra("plugged", -1);
        }
        catch (JSONException jsonexception)
        {
            Log.e("BatteryManager", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        flag = false;
        if (i > 0)
        {
            flag = true;
        }
        jsonobject.put("isPlugged", flag);
        return jsonobject;
    }

    private void removeBatteryListener()
    {
        if (receiver == null)
        {
            break MISSING_BLOCK_LABEL_28;
        }
        cordova.getActivity().unregisterReceiver(receiver);
        receiver = null;
        return;
        Exception exception;
        exception;
        Log.e("BatteryManager", (new StringBuilder()).append("Error unregistering battery receiver: ").append(exception.getMessage()).toString(), exception);
        return;
    }

    private void sendUpdate(JSONObject jsonobject, boolean flag)
    {
        if (batteryCallbackContext != null)
        {
            PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, jsonobject);
            pluginresult.setKeepCallback(flag);
            batteryCallbackContext.sendPluginResult(pluginresult);
        }
    }

    private void updateBatteryInfo(Intent intent)
    {
        sendUpdate(getBatteryInfo(intent), true);
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
    {
        if (s.equals("start"))
        {
            if (batteryCallbackContext != null)
            {
                callbackcontext.error("Battery listener already running.");
                return true;
            }
            batteryCallbackContext = callbackcontext;
            IntentFilter intentfilter = new IntentFilter();
            intentfilter.addAction("android.intent.action.BATTERY_CHANGED");
            if (receiver == null)
            {
                receiver = new BroadcastReceiver() {

                    final BatteryListener this$0;

                    public void onReceive(Context context, Intent intent)
                    {
                        updateBatteryInfo(intent);
                    }

            
            {
                this$0 = BatteryListener.this;
                super();
            }
                };
                cordova.getActivity().registerReceiver(receiver, intentfilter);
            }
            PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.NO_RESULT);
            pluginresult.setKeepCallback(true);
            callbackcontext.sendPluginResult(pluginresult);
            return true;
        }
        if (s.equals("stop"))
        {
            removeBatteryListener();
            sendUpdate(new JSONObject(), false);
            batteryCallbackContext = null;
            callbackcontext.success();
            return true;
        } else
        {
            return false;
        }
    }

    public void onDestroy()
    {
        removeBatteryListener();
    }

    public void onReset()
    {
        removeBatteryListener();
    }

}
