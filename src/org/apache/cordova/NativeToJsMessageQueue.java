// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView

public class NativeToJsMessageQueue
{
    private static interface BridgeMode
    {

        public abstract void onNativeToJsMessageAvailable();
    }

    private static class JsMessage
    {

        final String jsPayloadOrCallbackId;
        final PluginResult pluginResult;

        int calculateEncodedLength()
        {
            if (pluginResult == null)
            {
                return 1 + jsPayloadOrCallbackId.length();
            }
            int i = 1 + (1 + (2 + String.valueOf(pluginResult.getStatus()).length()) + jsPayloadOrCallbackId.length());
            switch (pluginResult.getMessageType())
            {
            case 2: // '\002'
            default:
                return i + pluginResult.getMessage().length();

            case 4: // '\004'
            case 5: // '\005'
                return i + 1;

            case 3: // '\003'
                return i + (1 + pluginResult.getMessage().length());

            case 1: // '\001'
                return i + (1 + pluginResult.getStrMessage().length());

            case 7: // '\007'
                return i + (1 + pluginResult.getMessage().length());

            case 6: // '\006'
                return i + (1 + pluginResult.getMessage().length());
            }
        }

        void encodeAsJsMessage(StringBuilder stringbuilder)
        {
            if (pluginResult == null)
            {
                stringbuilder.append(jsPayloadOrCallbackId);
                return;
            }
            int i = pluginResult.getStatus();
            boolean flag;
            if (i == org.apache.cordova.api.PluginResult.Status.OK.ordinal() || i == org.apache.cordova.api.PluginResult.Status.NO_RESULT.ordinal())
            {
                flag = true;
            } else
            {
                flag = false;
            }
            stringbuilder.append("cordova.callbackFromNative('").append(jsPayloadOrCallbackId).append("',").append(flag).append(",").append(i).append(",[").append(pluginResult.getMessage()).append("],").append(pluginResult.getKeepCallback()).append(");");
        }

        void encodeAsMessage(StringBuilder stringbuilder)
        {
            if (pluginResult == null)
            {
                stringbuilder.append('J').append(jsPayloadOrCallbackId);
                return;
            }
            int i = pluginResult.getStatus();
            boolean flag;
            boolean flag1;
            boolean flag2;
            char c;
            StringBuilder stringbuilder1;
            char c1;
            if (i == org.apache.cordova.api.PluginResult.Status.NO_RESULT.ordinal())
            {
                flag = true;
            } else
            {
                flag = false;
            }
            if (i == org.apache.cordova.api.PluginResult.Status.OK.ordinal())
            {
                flag1 = true;
            } else
            {
                flag1 = false;
            }
            flag2 = pluginResult.getKeepCallback();
            if (flag || flag1)
            {
                c = 'S';
            } else
            {
                c = 'F';
            }
            stringbuilder1 = stringbuilder.append(c);
            if (flag2)
            {
                c1 = '1';
            } else
            {
                c1 = '0';
            }
            stringbuilder1.append(c1).append(i).append(' ').append(jsPayloadOrCallbackId).append(' ');
            switch (pluginResult.getMessageType())
            {
            case 2: // '\002'
            default:
                stringbuilder.append(pluginResult.getMessage());
                return;

            case 4: // '\004'
                stringbuilder.append(pluginResult.getMessage().charAt(0));
                return;

            case 5: // '\005'
                stringbuilder.append('N');
                return;

            case 3: // '\003'
                stringbuilder.append('n').append(pluginResult.getMessage());
                return;

            case 1: // '\001'
                stringbuilder.append('s');
                stringbuilder.append(pluginResult.getStrMessage());
                return;

            case 7: // '\007'
                stringbuilder.append('S');
                stringbuilder.append(pluginResult.getMessage());
                return;

            case 6: // '\006'
                stringbuilder.append('A');
                stringbuilder.append(pluginResult.getMessage());
                return;
            }
        }

        JsMessage(String s)
        {
            if (s == null)
            {
                throw new NullPointerException();
            } else
            {
                jsPayloadOrCallbackId = s;
                pluginResult = null;
                return;
            }
        }

        JsMessage(PluginResult pluginresult, String s)
        {
            if (s == null || pluginresult == null)
            {
                throw new NullPointerException();
            } else
            {
                jsPayloadOrCallbackId = s;
                pluginResult = pluginresult;
                return;
            }
        }
    }

    private class LoadUrlBridgeMode
        implements BridgeMode
    {

        final Runnable runnable;
        final NativeToJsMessageQueue this$0;

        public void onNativeToJsMessageAvailable()
        {
            cordova.getActivity().runOnUiThread(runnable);
        }

        private LoadUrlBridgeMode()
        {
            this$0 = NativeToJsMessageQueue.this;
            super();
            runnable = new _cls1();
        }

    }

    private class OnlineEventsBridgeMode
        implements BridgeMode
    {

        boolean online;
        final Runnable runnable = new _cls1();
        final NativeToJsMessageQueue this$0;

        public void onNativeToJsMessageAvailable()
        {
            cordova.getActivity().runOnUiThread(runnable);
        }

        OnlineEventsBridgeMode()
        {
            this$0 = NativeToJsMessageQueue.this;
            super();
            online = true;
            webView.setNetworkAvailable(true);
        }
    }

    private class PrivateApiBridgeMode
        implements BridgeMode
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
            obj = webView;
            obj1 = android/webkit/WebView;
            Class class1;
            Field field1 = ((Class) (obj1)).getDeclaredField("mProvider");
            field1.setAccessible(true);
            obj = field1.get(webView);
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
            message = Message.obtain(null, 194, popAndEncodeAsJs());
            sendMessageMethod.invoke(webViewCore, new Object[] {
                message
            });
            return;
            Throwable throwable;
            throwable;
            Log.e("JsMessageQueue", "Reflection message bridge failed.", throwable);
            return;
        }

        private PrivateApiBridgeMode()
        {
            this$0 = NativeToJsMessageQueue.this;
            super();
        }

    }


    private static final int DEFAULT_BRIDGE_MODE = 2;
    static final boolean DISABLE_EXEC_CHAINING = false;
    static final boolean ENABLE_LOCATION_CHANGE_EXEC_MODE = false;
    private static final boolean FORCE_ENCODE_USING_EVAL = false;
    private static final String LOG_TAG = "JsMessageQueue";
    private static int MAX_PAYLOAD_SIZE = 0x1f400000;
    private int activeListenerIndex;
    private final CordovaInterface cordova;
    private boolean paused;
    private final LinkedList queue = new LinkedList();
    private final BridgeMode registeredListeners[] = new BridgeMode[4];
    private final CordovaWebView webView;

    public NativeToJsMessageQueue(CordovaWebView cordovawebview, CordovaInterface cordovainterface)
    {
        cordova = cordovainterface;
        webView = cordovawebview;
        registeredListeners[0] = null;
        registeredListeners[1] = new LoadUrlBridgeMode();
        registeredListeners[2] = new OnlineEventsBridgeMode();
        registeredListeners[3] = new PrivateApiBridgeMode();
        reset();
    }

    private int calculatePackedMessageLength(JsMessage jsmessage)
    {
        int i = jsmessage.calculateEncodedLength();
        return 1 + (i + String.valueOf(i).length());
    }

    private void enqueueMessage(JsMessage jsmessage)
    {
        this;
        JVM INSTR monitorenter ;
        queue.add(jsmessage);
        if (!paused && registeredListeners[activeListenerIndex] != null)
        {
            registeredListeners[activeListenerIndex].onNativeToJsMessageAvailable();
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private void packMessage(JsMessage jsmessage, StringBuilder stringbuilder)
    {
        stringbuilder.append(jsmessage.calculateEncodedLength()).append(' ');
        jsmessage.encodeAsMessage(stringbuilder);
    }

    private String popAndEncodeAsJs()
    {
        this;
        JVM INSTR monitorenter ;
        if (queue.size() != 0)
        {
            break MISSING_BLOCK_LABEL_16;
        }
        this;
        JVM INSTR monitorexit ;
        return null;
        int i;
        int j;
        i = 0;
        j = 0;
        Iterator iterator = queue.iterator();
_L12:
        if (!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        int j1 = 50 + ((JsMessage)iterator.next()).calculateEncodedLength();
        if (j <= 0) goto _L4; else goto _L3
_L3:
        if (i + j1 <= MAX_PAYLOAD_SIZE || MAX_PAYLOAD_SIZE <= 0) goto _L4; else goto _L2
_L2:
        boolean flag;
        int i1;
        Exception exception;
        int k;
        StringBuilder stringbuilder;
        int l;
        JsMessage jsmessage;
        String s;
        if (j == queue.size())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        if (flag)
        {
            k = 0;
        } else
        {
            k = 100;
        }
        stringbuilder = new StringBuilder(k + i);
        l = 0;
_L11:
        if (l >= j) goto _L6; else goto _L5
_L5:
        jsmessage = (JsMessage)queue.removeFirst();
        if (!flag || l + 1 != j) goto _L8; else goto _L7
_L7:
        jsmessage.encodeAsJsMessage(stringbuilder);
          goto _L9
_L8:
        stringbuilder.append("try{");
        jsmessage.encodeAsJsMessage(stringbuilder);
        stringbuilder.append("}finally{");
          goto _L9
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
_L6:
        if (flag)
        {
            break MISSING_BLOCK_LABEL_265;
        }
        stringbuilder.append("window.setTimeout(function(){cordova.require('cordova/plugin/android/polling').pollOnce();},0);");
        break MISSING_BLOCK_LABEL_265;
_L10:
        if (i1 >= j)
        {
            break MISSING_BLOCK_LABEL_223;
        }
        stringbuilder.append('}');
        i1++;
          goto _L10
        s = stringbuilder.toString();
        this;
        JVM INSTR monitorexit ;
        return s;
_L9:
        l++;
          goto _L11
_L4:
        i += j1;
        j++;
          goto _L12
        if (flag)
        {
            i1 = 1;
        } else
        {
            i1 = 0;
        }
          goto _L10
    }

    public void addJavaScript(String s)
    {
        enqueueMessage(new JsMessage(s));
    }

    public void addPluginResult(PluginResult pluginresult, String s)
    {
        if (s == null)
        {
            Log.e("JsMessageQueue", "Got plugin result with no callbackId", new Throwable());
        } else
        {
            boolean flag;
            boolean flag1;
            if (pluginresult.getStatus() == org.apache.cordova.api.PluginResult.Status.NO_RESULT.ordinal())
            {
                flag = true;
            } else
            {
                flag = false;
            }
            flag1 = pluginresult.getKeepCallback();
            if (!flag || !flag1)
            {
                enqueueMessage(new JsMessage(pluginresult, s));
                return;
            }
        }
    }

    public boolean getPaused()
    {
        return paused;
    }

    public String popAndEncode()
    {
        this;
        JVM INSTR monitorenter ;
        if (!queue.isEmpty())
        {
            break MISSING_BLOCK_LABEL_16;
        }
        this;
        JVM INSTR monitorexit ;
        return null;
        int i;
        int j;
        i = 0;
        j = 0;
        Iterator iterator = queue.iterator();
_L5:
        if (!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        int l = calculatePackedMessageLength((JsMessage)iterator.next());
        if (j <= 0)
        {
            break MISSING_BLOCK_LABEL_151;
        }
        if (i + l <= MAX_PAYLOAD_SIZE || MAX_PAYLOAD_SIZE <= 0)
        {
            break MISSING_BLOCK_LABEL_151;
        }
_L2:
        StringBuilder stringbuilder = new StringBuilder(i);
        int k = 0;
_L4:
        if (k >= j)
        {
            break; /* Loop/switch isn't completed */
        }
        packMessage((JsMessage)queue.removeFirst(), stringbuilder);
        k++;
        if (true) goto _L4; else goto _L3
_L3:
        String s;
        if (!queue.isEmpty())
        {
            stringbuilder.append('*');
        }
        s = stringbuilder.toString();
        this;
        JVM INSTR monitorexit ;
        return s;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        i += l;
        j++;
          goto _L5
    }

    public void reset()
    {
        this;
        JVM INSTR monitorenter ;
        queue.clear();
        setBridgeMode(2);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void setBridgeMode(int i)
    {
        if (i >= 0 && i < registeredListeners.length) goto _L2; else goto _L1
_L1:
        Log.d("JsMessageQueue", (new StringBuilder()).append("Invalid NativeToJsBridgeMode: ").append(i).toString());
_L4:
        return;
_L2:
        if (i == activeListenerIndex) goto _L4; else goto _L3
_L3:
        Log.d("JsMessageQueue", (new StringBuilder()).append("Set native->JS mode to ").append(i).toString());
        this;
        JVM INSTR monitorenter ;
        BridgeMode bridgemode;
        activeListenerIndex = i;
        bridgemode = registeredListeners[i];
        if (paused || queue.isEmpty() || bridgemode == null)
        {
            break MISSING_BLOCK_LABEL_116;
        }
        bridgemode.onNativeToJsMessageAvailable();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void setPaused(boolean flag)
    {
        if (paused && flag)
        {
            Log.e("JsMessageQueue", "nested call to setPaused detected.", new Throwable());
        }
        paused = flag;
        if (flag)
        {
            break MISSING_BLOCK_LABEL_81;
        }
        this;
        JVM INSTR monitorenter ;
        if (!queue.isEmpty() && registeredListeners[activeListenerIndex] != null)
        {
            registeredListeners[activeListenerIndex].onNativeToJsMessageAvailable();
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }






    // Unreferenced inner class org/apache/cordova/NativeToJsMessageQueue$LoadUrlBridgeMode$1

/* anonymous class */
    class LoadUrlBridgeMode._cls1
        implements Runnable
    {

        final LoadUrlBridgeMode this$1;

        public void run()
        {
            String s = popAndEncodeAsJs();
            if (s != null)
            {
                webView.loadUrlNow((new StringBuilder()).append("javascript:").append(s).toString());
            }
        }

            
            {
                this$1 = LoadUrlBridgeMode.this;
                super();
            }
    }


    // Unreferenced inner class org/apache/cordova/NativeToJsMessageQueue$OnlineEventsBridgeMode$1

/* anonymous class */
    class OnlineEventsBridgeMode._cls1
        implements Runnable
    {

        final OnlineEventsBridgeMode this$1;

        public void run()
        {
            if (!queue.isEmpty())
            {
                OnlineEventsBridgeMode onlineeventsbridgemode = OnlineEventsBridgeMode.this;
                boolean flag;
                if (!online)
                {
                    flag = true;
                } else
                {
                    flag = false;
                }
                onlineeventsbridgemode.online = flag;
                webView.setNetworkAvailable(online);
            }
        }

            
            {
                this$1 = OnlineEventsBridgeMode.this;
                super();
            }
    }

}
