// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import org.apache.cordova.api.PluginResult;

// Referenced classes of package org.apache.cordova:
//            NativeToJsMessageQueue

private static class pluginResult
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
        if (i == org.apache.cordova.api.inResult.inResult() || i == org.apache.cordova.api.inResult.inResult())
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
        if (i == org.apache.cordova.api.inResult.inResult())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        if (i == org.apache.cordova.api.inResult.inResult())
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

    _cls9(String s)
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

    pluginResult(PluginResult pluginresult, String s)
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
