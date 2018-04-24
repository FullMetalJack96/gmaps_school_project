// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.Socket;

// Referenced classes of package com.squareup.okhttp.internal:
//            Platform

private static class <init> extends Platform
{

    private final Method getMtu;

    public int getMtu(Socket socket)
        throws IOException
    {
        int i;
        try
        {
            NetworkInterface networkinterface = NetworkInterface.getByInetAddress(socket.getLocalAddress());
            i = ((Integer)getMtu.invoke(networkinterface, new Object[0])).intValue();
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            throw new AssertionError(illegalaccessexception);
        }
        catch (InvocationTargetException invocationtargetexception)
        {
            if (invocationtargetexception.getCause() instanceof IOException)
            {
                throw (IOException)invocationtargetexception.getCause();
            } else
            {
                throw new RuntimeException(invocationtargetexception.getCause());
            }
        }
        return i;
    }

    private (Method method)
    {
        getMtu = method;
    }

    getMtu(Method method, getMtu getmtu)
    {
        this(method);
    }
}
