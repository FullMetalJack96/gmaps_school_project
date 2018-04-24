// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova.api;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.webkit.WebResourceResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package org.apache.cordova.api:
//            LOG, PluginEntry, PluginResult, CallbackContext, 
//            CordovaPlugin, CordovaInterface

public class PluginManager
{

    private static String TAG = "PluginManager";
    private final CordovaWebView app;
    private final CordovaInterface ctx;
    private final HashMap entries = new HashMap();
    private boolean firstRun;
    protected HashMap urlMap;

    public PluginManager(CordovaWebView cordovawebview, CordovaInterface cordovainterface)
    {
        urlMap = new HashMap();
        ctx = cordovainterface;
        app = cordovawebview;
        firstRun = true;
    }

    private void pluginConfigurationMissing()
    {
        LOG.e(TAG, "=====================================================================================");
        LOG.e(TAG, "ERROR: config.xml is missing.  Add res/xml/plugins.xml to your project.");
        LOG.e(TAG, "https://git-wip-us.apache.org/repos/asf?p=incubator-cordova-android.git;a=blob;f=framework/res/xml/plugins.xml");
        LOG.e(TAG, "=====================================================================================");
    }

    public void addService(String s, String s1)
    {
        addService(new PluginEntry(s, s1, false));
    }

    public void addService(PluginEntry pluginentry)
    {
        entries.put(pluginentry.service, pluginentry);
    }

    public void clearPluginObjects()
    {
        for (Iterator iterator = entries.values().iterator(); iterator.hasNext();)
        {
            ((PluginEntry)iterator.next()).plugin = null;
        }

    }

    public boolean exec(String s, String s1, String s2, String s3)
    {
        CordovaPlugin cordovaplugin;
        cordovaplugin = getPlugin(s);
        if (cordovaplugin == null)
        {
            Log.d(TAG, (new StringBuilder()).append("exec() call to unknown plugin: ").append(s).toString());
            PluginResult pluginresult2 = new PluginResult(PluginResult.Status.CLASS_NOT_FOUND_EXCEPTION);
            app.sendPluginResult(pluginresult2, s2);
            return true;
        }
        CallbackContext callbackcontext;
        callbackcontext = new CallbackContext(s2, app);
        if (cordovaplugin.execute(s1, s3, callbackcontext))
        {
            break MISSING_BLOCK_LABEL_139;
        }
        PluginResult pluginresult1 = new PluginResult(PluginResult.Status.INVALID_ACTION);
        app.sendPluginResult(pluginresult1, s2);
        JSONException jsonexception;
        PluginResult pluginresult;
        return true;
        boolean flag;
        try
        {
            flag = callbackcontext.isFinished();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            pluginresult = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            app.sendPluginResult(pluginresult, s2);
            return true;
        }
        return flag;
    }

    public boolean exec(String s, String s1, String s2, String s3, boolean flag)
    {
        return exec(s, s1, s2, s3);
    }

    public CordovaPlugin getPlugin(String s)
    {
        PluginEntry pluginentry = (PluginEntry)entries.get(s);
        CordovaPlugin cordovaplugin;
        if (pluginentry == null)
        {
            cordovaplugin = null;
        } else
        {
            cordovaplugin = pluginentry.plugin;
            if (cordovaplugin == null)
            {
                return pluginentry.createPlugin(app, ctx);
            }
        }
        return cordovaplugin;
    }

    public void init()
    {
        LOG.d(TAG, "init()");
        if (firstRun)
        {
            loadPlugins();
            firstRun = false;
        } else
        {
            onPause(false);
            onDestroy();
            clearPluginObjects();
        }
        startupPlugins();
    }

    public void loadPlugins()
    {
        int i;
        i = ctx.getActivity().getResources().getIdentifier("config", "xml", ctx.getActivity().getPackageName());
        if (i == 0)
        {
            i = ctx.getActivity().getResources().getIdentifier("plugins", "xml", ctx.getActivity().getPackageName());
            LOG.i(TAG, "Using plugins.xml instead of config.xml.  plugins.xml will eventually be deprecated");
        }
        if (i != 0) goto _L2; else goto _L1
_L1:
        pluginConfigurationMissing();
_L6:
        return;
_L2:
        XmlResourceParser xmlresourceparser;
        int j;
        String s;
        String s1;
        boolean flag;
        boolean flag1;
        xmlresourceparser = ctx.getActivity().getResources().getXml(i);
        j = -1;
        s = "";
        s1 = "";
        flag = false;
        flag1 = false;
_L4:
        if (j == 1)
        {
            continue; /* Loop/switch isn't completed */
        }
        if (j == 2)
        {
            String s3 = xmlresourceparser.getName();
            int k;
            if (s3.equals("plugin"))
            {
                s = xmlresourceparser.getAttributeValue(null, "name");
                s1 = xmlresourceparser.getAttributeValue(null, "value");
                Log.d(TAG, "<plugin> tags are deprecated, please use <features> instead. <plugin> will no longer work as of Cordova 3.0");
                flag = "true".equals(xmlresourceparser.getAttributeValue(null, "onload"));
            } else
            if (s3.equals("url-filter"))
            {
                urlMap.put(xmlresourceparser.getAttributeValue(null, "value"), s);
            } else
            if (s3.equals("feature"))
            {
                flag1 = true;
                s = xmlresourceparser.getAttributeValue(null, "name");
            } else
            if (flag1 && s3.equals("param"))
            {
                String s4 = xmlresourceparser.getAttributeValue(null, "name");
                if (s4.equals("service"))
                {
                    s = xmlresourceparser.getAttributeValue(null, "value");
                } else
                if (s4.equals("package") || s4.equals("android-package"))
                {
                    s1 = xmlresourceparser.getAttributeValue(null, "value");
                } else
                if (s4.equals("onload"))
                {
                    flag = "true".equals(xmlresourceparser.getAttributeValue(null, "value"));
                }
            }
        } else
        if (j == 3)
        {
            String s2 = xmlresourceparser.getName();
            if (s2.equals("feature") || s2.equals("plugin"))
            {
                addService(new PluginEntry(s, s1, flag));
                s = "";
                s1 = "";
                flag1 = false;
            }
        }
        k = xmlresourceparser.next();
        j = k;
        break; /* Loop/switch isn't completed */
        XmlPullParserException xmlpullparserexception;
        xmlpullparserexception;
        xmlpullparserexception.printStackTrace();
        break; /* Loop/switch isn't completed */
        IOException ioexception;
        ioexception;
        ioexception.printStackTrace();
        if (true) goto _L4; else goto _L3
_L3:
        if (true) goto _L6; else goto _L5
_L5:
    }

    public void onDestroy()
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.plugin != null)
            {
                pluginentry.plugin.onDestroy();
            }
        } while (true);
    }

    public void onNewIntent(Intent intent)
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.plugin != null)
            {
                pluginentry.plugin.onNewIntent(intent);
            }
        } while (true);
    }

    public boolean onOverrideUrlLoading(String s)
    {
        for (Iterator iterator = urlMap.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if (s.startsWith((String)entry.getKey()))
            {
                return getPlugin((String)entry.getValue()).onOverrideUrlLoading(s);
            }
        }

        return false;
    }

    public void onPause(boolean flag)
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.plugin != null)
            {
                pluginentry.plugin.onPause(flag);
            }
        } while (true);
    }

    public void onReset()
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            CordovaPlugin cordovaplugin = ((PluginEntry)iterator.next()).plugin;
            if (cordovaplugin != null)
            {
                cordovaplugin.onReset();
            }
        } while (true);
    }

    public void onResume(boolean flag)
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.plugin != null)
            {
                pluginentry.plugin.onResume(flag);
            }
        } while (true);
    }

    public Object postMessage(String s, Object obj)
    {
        Object obj1 = ctx.onMessage(s, obj);
        if (obj1 != null)
        {
            return obj1;
        }
        for (Iterator iterator = entries.values().iterator(); iterator.hasNext();)
        {
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.plugin != null)
            {
                Object obj2 = pluginentry.plugin.onMessage(s, obj);
                if (obj2 != null)
                {
                    return obj2;
                }
            }
        }

        return null;
    }

    public WebResourceResponse shouldInterceptRequest(String s)
    {
        for (Iterator iterator = urlMap.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if (s.startsWith((String)entry.getKey()))
            {
                return getPlugin((String)entry.getValue()).shouldInterceptRequest(s);
            }
        }

        return null;
    }

    public void startupPlugins()
    {
        Iterator iterator = entries.values().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            PluginEntry pluginentry = (PluginEntry)iterator.next();
            if (pluginentry.onload)
            {
                pluginentry.createPlugin(app, ctx);
            }
        } while (true);
    }

}
