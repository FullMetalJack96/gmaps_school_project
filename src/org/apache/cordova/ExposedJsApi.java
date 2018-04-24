// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import org.apache.cordova.api.PluginManager;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            NativeToJsMessageQueue

class ExposedJsApi
{

    private NativeToJsMessageQueue jsMessageQueue;
    private PluginManager pluginManager;

    public ExposedJsApi(PluginManager pluginmanager, NativeToJsMessageQueue nativetojsmessagequeue)
    {
        pluginManager = pluginmanager;
        jsMessageQueue = nativetojsmessagequeue;
    }

    public String exec(String s, String s1, String s2, String s3)
        throws JSONException
    {
        if (s3 == null)
        {
            return "@Null arguments.";
        }
        jsMessageQueue.setPaused(true);
        String s4;
        pluginManager.exec(s, s1, s2, s3);
        s4 = jsMessageQueue.popAndEncode();
        jsMessageQueue.setPaused(false);
        return s4;
        Exception exception;
        exception;
        jsMessageQueue.setPaused(false);
        throw exception;
    }

    public String retrieveJsMessages()
    {
        return jsMessageQueue.popAndEncode();
    }

    public void setNativeToJsBridgeMode(int i)
    {
        jsMessageQueue.setBridgeMode(i);
    }
}
