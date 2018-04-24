// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

// Referenced classes of package com.squareup.okhttp.internal:
//            Util, Platform

private static class protocols
    implements InvocationHandler
{

    private final List protocols;
    private String selected;
    private boolean unsupported;

    public Object invoke(Object obj, Method method, Object aobj[])
        throws Throwable
    {
        String s = method.getName();
        Class class1 = method.getReturnType();
        if (aobj == null)
        {
            aobj = Util.EMPTY_STRING_ARRAY;
        }
        if (s.equals("supports") && Boolean.TYPE == class1)
        {
            return Boolean.valueOf(true);
        }
        if (s.equals("unsupported") && Void.TYPE == class1)
        {
            unsupported = true;
            return null;
        }
        if (s.equals("protocols") && aobj.length == 0)
        {
            return protocols;
        }
        if (s.equals("selectProtocol") && java/lang/String == class1 && aobj.length == 1 && (aobj[0] == null || (aobj[0] instanceof List)))
        {
            List _tmp = (List)aobj[0];
            selected = (String)protocols.get(0);
            return selected;
        }
        if (s.equals("protocolSelected") && aobj.length == 1)
        {
            selected = (String)aobj[0];
            return null;
        } else
        {
            return method.invoke(this, aobj);
        }
    }



    public (List list)
    {
        protocols = list;
    }
}
