// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp;

import java.net.InetSocketAddress;
import java.net.Proxy;

// Referenced classes of package com.squareup.okhttp:
//            Address

public class Route
{

    final Address address;
    final InetSocketAddress inetSocketAddress;
    final boolean modernTls;
    final Proxy proxy;

    public Route(Address address1, Proxy proxy1, InetSocketAddress inetsocketaddress, boolean flag)
    {
        if (address1 == null)
        {
            throw new NullPointerException("address == null");
        }
        if (proxy1 == null)
        {
            throw new NullPointerException("proxy == null");
        }
        if (inetsocketaddress == null)
        {
            throw new NullPointerException("inetSocketAddress == null");
        } else
        {
            address = address1;
            proxy = proxy1;
            inetSocketAddress = inetsocketaddress;
            modernTls = flag;
            return;
        }
    }

    public boolean equals(Object obj)
    {
        boolean flag = obj instanceof Route;
        boolean flag1 = false;
        if (flag)
        {
            Route route = (Route)obj;
            boolean flag2 = address.equals(route.address);
            flag1 = false;
            if (flag2)
            {
                boolean flag3 = proxy.equals(route.proxy);
                flag1 = false;
                if (flag3)
                {
                    boolean flag4 = inetSocketAddress.equals(route.inetSocketAddress);
                    flag1 = false;
                    if (flag4)
                    {
                        boolean flag5 = modernTls;
                        boolean flag6 = route.modernTls;
                        flag1 = false;
                        if (flag5 == flag6)
                        {
                            flag1 = true;
                        }
                    }
                }
            }
        }
        return flag1;
    }

    public Route flipTlsMode()
    {
        Address address1 = address;
        Proxy proxy1 = proxy;
        InetSocketAddress inetsocketaddress = inetSocketAddress;
        boolean flag;
        if (!modernTls)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        return new Route(address1, proxy1, inetsocketaddress, flag);
    }

    public Address getAddress()
    {
        return address;
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public InetSocketAddress getSocketAddress()
    {
        return inetSocketAddress;
    }

    public int hashCode()
    {
        int i = 31 * (31 * (527 + address.hashCode()) + proxy.hashCode()) + inetSocketAddress.hashCode();
        int j;
        if (modernTls)
        {
            j = i * 31;
        } else
        {
            j = 0;
        }
        return i + j;
    }

    public boolean isModernTls()
    {
        return modernTls;
    }
}
