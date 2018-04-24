// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import org.apache.cordova.api.CallbackContext;

// Referenced classes of package org.apache.cordova:
//            CordovaLocationListener

private class listener extends TimerTask
{

    private CallbackContext callbackContext;
    private CordovaLocationListener listener;
    final CordovaLocationListener this$0;

    public void run()
    {
        Iterator iterator = CordovaLocationListener.access$000(listener).iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            CallbackContext callbackcontext = (CallbackContext)iterator.next();
            if (callbackContext != callbackcontext)
            {
                continue;
            }
            CordovaLocationListener.access$000(listener).remove(callbackcontext);
            break;
        } while (true);
        if (listener.size() == 0)
        {
            CordovaLocationListener.access$100(listener);
        }
    }

    public (CallbackContext callbackcontext, CordovaLocationListener cordovalocationlistener1)
    {
        this$0 = CordovaLocationListener.this;
        super();
        callbackContext = null;
        listener = null;
        callbackContext = callbackcontext;
        listener = cordovalocationlistener1;
    }
}
