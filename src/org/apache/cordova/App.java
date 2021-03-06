// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import java.util.HashMap;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView

public class App extends CordovaPlugin
{

    public App()
    {
    }

    public void backHistory()
    {
        cordova.getActivity().runOnUiThread(new Runnable() {

            final App this$0;

            public void run()
            {
                webView.backHistory();
            }

            
            {
                this$0 = App.this;
                super();
            }
        });
    }

    public void clearCache()
    {
        webView.clearCache(true);
    }

    public void clearHistory()
    {
        webView.clearHistory();
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        org.apache.cordova.api.PluginResult.Status status = org.apache.cordova.api.PluginResult.Status.OK;
        if (!s.equals("clearCache")) goto _L2; else goto _L1
_L1:
        clearCache();
_L3:
        callbackcontext.sendPluginResult(new PluginResult(status, ""));
        return true;
_L2:
        JSONException jsonexception;
label0:
        {
            if (!s.equals("show"))
            {
                break label0;
            }
            cordova.getActivity().runOnUiThread(new Runnable() {

                final App this$0;

                public void run()
                {
                    webView.postMessage("spinner", "stop");
                }

            
            {
                this$0 = App.this;
                super();
            }
            });
        }
          goto _L3
        if (!s.equals("loadUrl")) goto _L5; else goto _L4
_L4:
        loadUrl(jsonarray.getString(0), jsonarray.optJSONObject(1));
          goto _L3
_L5:
        if (s.equals("cancelLoadUrl")) goto _L3; else goto _L6
_L6:
        if (!s.equals("clearHistory"))
        {
            break MISSING_BLOCK_LABEL_136;
        }
        clearHistory();
          goto _L3
label1:
        {
            if (!s.equals("backHistory"))
            {
                break label1;
            }
            backHistory();
        }
          goto _L3
        try
        {
label2:
            {
                if (!s.equals("overrideButton"))
                {
                    break label2;
                }
                overrideButton(jsonarray.getString(0), jsonarray.getBoolean(1));
            }
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION));
            return false;
        }
          goto _L3
        if (!s.equals("overrideBackbutton")) goto _L8; else goto _L7
_L7:
        overrideBackbutton(jsonarray.getBoolean(0));
          goto _L3
_L8:
        if (!s.equals("exitApp")) goto _L3; else goto _L9
_L9:
        exitApp();
          goto _L3
    }

    public void exitApp()
    {
        webView.postMessage("exit", null);
    }

    public boolean isBackbuttonOverridden()
    {
        return webView.isBackButtonBound();
    }

    public void loadUrl(String s, JSONObject jsonobject)
        throws JSONException
    {
        HashMap hashmap;
        boolean flag;
        boolean flag1;
        int i;
        LOG.d("App", (new StringBuilder()).append("App.loadUrl(").append(s).append(",").append(jsonobject).append(")").toString());
        hashmap = new HashMap();
        flag = false;
        flag1 = false;
        i = 0;
        if (jsonobject != null)
        {
            JSONArray jsonarray = jsonobject.names();
            int j = 0;
            while (j < jsonarray.length()) 
            {
                String s1 = jsonarray.getString(j);
                if (s1.equals("wait"))
                {
                    i = jsonobject.getInt(s1);
                } else
                if (s1.equalsIgnoreCase("openexternal"))
                {
                    flag1 = jsonobject.getBoolean(s1);
                } else
                if (s1.equalsIgnoreCase("clearhistory"))
                {
                    flag = jsonobject.getBoolean(s1);
                } else
                {
                    Object obj = jsonobject.get(s1);
                    if (obj != null)
                    {
                        if (obj.getClass().equals(java/lang/String))
                        {
                            hashmap.put(s1, (String)obj);
                        } else
                        if (obj.getClass().equals(java/lang/Boolean))
                        {
                            hashmap.put(s1, (Boolean)obj);
                        } else
                        if (obj.getClass().equals(java/lang/Integer))
                        {
                            hashmap.put(s1, (Integer)obj);
                        }
                    }
                }
                j++;
            }
        }
        if (i <= 0) goto _L2; else goto _L1
_L1:
        this;
        JVM INSTR monitorenter ;
        long l = i;
        wait(l);
        this;
        JVM INSTR monitorexit ;
_L2:
        webView.showWebPage(s, flag1, flag, hashmap);
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        try
        {
            throw exception;
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
        if (true) goto _L2; else goto _L3
_L3:
    }

    public void overrideBackbutton(boolean flag)
    {
        LOG.i("App", "WARNING: Back Button Default Behaviour will be overridden.  The backbutton event will be fired!");
        webView.bindButton(flag);
    }

    public void overrideButton(String s, boolean flag)
    {
        LOG.i("App", "WARNING: Volume Button Default Behaviour will be overridden.  The volume event will be fired!");
        webView.bindButton(s, flag);
    }
}
