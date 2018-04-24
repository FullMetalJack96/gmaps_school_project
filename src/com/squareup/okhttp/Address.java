// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.net.Proxy;
import java.net.UnknownHostException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public final class Address
{

    final HostnameVerifier hostnameVerifier;
    final Proxy proxy;
    final SSLSocketFactory sslSocketFactory;
    final String uriHost;
    final int uriPort;

    public Address(String s, int i, SSLSocketFactory sslsocketfactory, HostnameVerifier hostnameverifier, Proxy proxy1)
        throws UnknownHostException
    {
        if (s == null)
        {
            throw new NullPointerException("uriHost == null");
        }
        if (i <= 0)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("uriPort <= 0: ").append(i).toString());
        } else
        {
            proxy = proxy1;
            uriHost = s;
            uriPort = i;
            sslSocketFactory = sslsocketfactory;
            hostnameVerifier = hostnameverifier;
            return;
        }
    }

    public boolean equals(Object obj)
    {
        boolean flag = obj instanceof Address;
        boolean flag1 = false;
        if (flag)
        {
            Address address = (Address)obj;
            boolean flag2 = Util.equal(proxy, address.proxy);
            flag1 = false;
            if (flag2)
            {
                boolean flag3 = uriHost.equals(address.uriHost);
                flag1 = false;
                if (flag3)
                {
                    int i = uriPort;
                    int j = address.uriPort;
                    flag1 = false;
                    if (i == j)
                    {
                        boolean flag4 = Util.equal(sslSocketFactory, address.sslSocketFactory);
                        flag1 = false;
                        if (flag4)
                        {
                            boolean flag5 = Util.equal(hostnameVerifier, address.hostnameVerifier);
                            flag1 = false;
                            if (flag5)
                            {
                                flag1 = true;
                            }
                        }
                    }
                }
            }
        }
        return flag1;
    }

    public HostnameVerifier getHostnameVerifier()
    {
        return hostnameVerifier;
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public SSLSocketFactory getSslSocketFactory()
    {
        return sslSocketFactory;
    }

    public String getUriHost()
    {
        return uriHost;
    }

    public int getUriPort()
    {
        return uriPort;
    }

    public int hashCode()
    {
        int i = 31 * (31 * (527 + uriHost.hashCode()) + uriPort);
        int j;
        int k;
        int l;
        int i1;
        Proxy proxy1;
        int j1;
        if (sslSocketFactory != null)
        {
            j = sslSocketFactory.hashCode();
        } else
        {
            j = 0;
        }
        k = 31 * (i + j);
        if (hostnameVerifier != null)
        {
            l = hostnameVerifier.hashCode();
        } else
        {
            l = 0;
        }
        i1 = 31 * (k + l);
        proxy1 = proxy;
        j1 = 0;
        if (proxy1 != null)
        {
            j1 = proxy.hashCode();
        }
        return i1 + j1;
    }
}
