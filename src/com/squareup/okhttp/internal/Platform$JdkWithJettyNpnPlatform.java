// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import com.squareup.okhttp.OkHttpClient;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

// Referenced classes of package com.squareup.okhttp.internal:
//            Platform

private static class serverProviderClass extends serverProviderClass
{

    private final Class clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Class serverProviderClass;

    public byte[] getNpnSelectedProtocol(SSLSocket sslsocket)
    {
        serverProviderClass serverproviderclass;
        serverproviderclass = (serverProviderClass)Proxy.getInvocationHandler(getMethod.invoke(null, new Object[] {
            sslsocket
        }));
        if (_mth300(serverproviderclass) || _mth400(serverproviderclass) != null)
        {
            break MISSING_BLOCK_LABEL_58;
        }
        Logger.getLogger(com/squareup/okhttp/OkHttpClient.getName()).log(Level.INFO, "NPN callback dropped so SPDY is disabled. Is npn-boot on the boot class path?");
        return null;
label0:
        {
            byte abyte0[];
            try
            {
                if (_mth300(serverproviderclass))
                {
                    break label0;
                }
                abyte0 = _mth400(serverproviderclass).getBytes("US-ASCII");
            }
            catch (UnsupportedEncodingException unsupportedencodingexception)
            {
                throw new AssertionError();
            }
            catch (InvocationTargetException invocationtargetexception)
            {
                throw new AssertionError();
            }
            catch (IllegalAccessException illegalaccessexception)
            {
                throw new AssertionError();
            }
            return abyte0;
        }
        return null;
    }

    public void setNpnProtocols(SSLSocket sslsocket, byte abyte0[])
    {
        ArrayList arraylist;
        int i;
        int j;
        byte byte0;
        ClassLoader classloader;
        Class aclass[];
        Object obj;
        try
        {
            arraylist = new ArrayList();
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new AssertionError(unsupportedencodingexception);
        }
        catch (InvocationTargetException invocationtargetexception)
        {
            throw new AssertionError(invocationtargetexception);
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            throw new AssertionError(illegalaccessexception);
        }
        i = 0;
        if (i >= abyte0.length)
        {
            break; /* Loop/switch isn't completed */
        }
        j = i + 1;
        byte0 = abyte0[i];
        arraylist.add(new String(abyte0, j, byte0, "US-ASCII"));
        i = j + byte0;
        if (true) goto _L2; else goto _L1
_L2:
        break MISSING_BLOCK_LABEL_11;
_L1:
        classloader = com/squareup/okhttp/internal/Platform.getClassLoader();
        aclass = new Class[2];
        aclass[0] = clientProviderClass;
        aclass[1] = serverProviderClass;
        obj = Proxy.newProxyInstance(classloader, aclass, new serverProviderClass(arraylist));
        putMethod.invoke(null, new Object[] {
            sslsocket, obj
        });
        return;
    }

    public (Method method, Method method1, Method method2, Class class1, Class class2)
    {
        super(method, null);
        putMethod = method1;
        getMethod = method2;
        clientProviderClass = class1;
        serverProviderClass = class2;
    }
}
