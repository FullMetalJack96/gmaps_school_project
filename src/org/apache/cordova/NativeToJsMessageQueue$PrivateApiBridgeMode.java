// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Referenced classes of package org.apache.cordova:
//            NativeToJsMessageQueue

private class <init>
    implements <init>
{

    private static final int EXECUTE_JS = 194;
    boolean initFailed;
    Method sendMessageMethod;
    final NativeToJsMessageQueue this$0;
    Object webViewCore;

    private void initReflection()
    {
        Object obj;
        Object obj1;
        obj = NativeToJsMessageQueue.access$300(NativeToJsMessageQueue.this);
        obj1 = android/webkit/WebView;
        Class class1;
        Field field1 = ((Class) (obj1)).getDeclaredField("mProvider");
        field1.setAccessible(true);
        obj = field1.get(NativeToJsMessageQueue.access$300(NativeToJsMessageQueue.this));
        class1 = obj.getClass();
        obj1 = class1;
_L2:
        try
        {
            Field field = ((Class) (obj1)).getDeclaredField("mWebViewCore");
            field.setAccessible(true);
            webViewCore = field.get(obj);
            if (webViewCore != null)
            {
                sendMessageMethod = webViewCore.getClass().getDeclaredMethod("sendMessage", new Class[] {
                    android/os/Message
                });
                sendMessageMethod.setAccessible(true);
            }
            return;
        }
        catch (Throwable throwable1)
        {
            initFailed = true;
            Log.e("JsMessageQueue", "PrivateApiBridgeMode failed to find the expected APIs.", throwable1);
            return;
        }
        Throwable throwable;
        throwable;
        if (true) goto _L2; else goto _L1
_L1:
    }

    public void onNativeToJsMessageAvailable()
    {
        Message message;
        if (sendMessageMethod == null && !initFailed)
        {
            initReflection();
        }
        if (sendMessageMethod == null)
        {
            break MISSING_BLOCK_LABEL_60;
        }
        message = Message.obtain(null, 194, NativeToJsMessageQueue.access$200(NativeToJsMessageQueue.this));
        sendMessageMethod.invoke(webViewCore, new Object[] {
            message
        });
        return;
        Throwable throwable;
        throwable;
        Log.e("JsMessageQueue", "Reflection message bridge failed.", throwable);
        return;
    }

    private ()
    {
        this$0 = NativeToJsMessageQueue.this;
        super();
    }

    this._cls0(this._cls0 _pcls0)
    {
        this();
    }
}
