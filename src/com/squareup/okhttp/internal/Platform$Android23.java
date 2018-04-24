// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocket;

// Referenced classes of package com.squareup.okhttp.internal:
//            Platform

private static class <init> extends <init>
{

    protected final Class openSslSocketClass;
    private final Method setHostname;
    private final Method setUseSessionTickets;

    public void enableTlsExtensions(SSLSocket sslsocket, String s)
    {
        super.leTlsExtensions(sslsocket, s);
        if (!openSslSocketClass.isInstance(sslsocket))
        {
            break MISSING_BLOCK_LABEL_63;
        }
        Method method = setUseSessionTickets;
        Object aobj[] = new Object[1];
        aobj[0] = Boolean.valueOf(true);
        method.invoke(sslsocket, aobj);
        setHostname.invoke(sslsocket, new Object[] {
            s
        });
        return;
        InvocationTargetException invocationtargetexception;
        invocationtargetexception;
        throw new RuntimeException(invocationtargetexception);
        IllegalAccessException illegalaccessexception;
        illegalaccessexception;
        throw new AssertionError(illegalaccessexception);
    }

    private (Method method, Class class1, Method method1, Method method2)
    {
        super(method, null);
        openSslSocketClass = class1;
        setUseSessionTickets = method1;
        setHostname = method2;
    }

    setHostname(Method method, Class class1, Method method1, Method method2, setHostname sethostname)
    {
        this(method, class1, method1, method2);
    }
}
