// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.net.ssl.SSLSocket;

// Referenced classes of package com.squareup.okhttp.internal:
//            Util

public class Platform
{
    private static class Android23 extends Java5
    {

        protected final Class openSslSocketClass;
        private final Method setHostname;
        private final Method setUseSessionTickets;

        public void enableTlsExtensions(SSLSocket sslsocket, String s)
        {
            super.enableTlsExtensions(sslsocket, s);
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

        private Android23(Method method, Class class1, Method method1, Method method2)
        {
            super(method);
            openSslSocketClass = class1;
            setUseSessionTickets = method1;
            setHostname = method2;
        }

    }

    private static class Android41 extends Android23
    {

        private final Method getNpnSelectedProtocol;
        private final Method setNpnProtocols;

        public byte[] getNpnSelectedProtocol(SSLSocket sslsocket)
        {
            if (!openSslSocketClass.isInstance(sslsocket))
            {
                return null;
            }
            byte abyte0[];
            try
            {
                abyte0 = (byte[])(byte[])getNpnSelectedProtocol.invoke(sslsocket, new Object[0]);
            }
            catch (InvocationTargetException invocationtargetexception)
            {
                throw new RuntimeException(invocationtargetexception);
            }
            catch (IllegalAccessException illegalaccessexception)
            {
                throw new AssertionError(illegalaccessexception);
            }
            return abyte0;
        }

        public void setNpnProtocols(SSLSocket sslsocket, byte abyte0[])
        {
            if (!openSslSocketClass.isInstance(sslsocket))
            {
                return;
            }
            try
            {
                setNpnProtocols.invoke(sslsocket, new Object[] {
                    abyte0
                });
                return;
            }
            catch (IllegalAccessException illegalaccessexception)
            {
                throw new AssertionError(illegalaccessexception);
            }
            catch (InvocationTargetException invocationtargetexception)
            {
                throw new RuntimeException(invocationtargetexception);
            }
        }

        private Android41(Method method, Class class1, Method method1, Method method2, Method method3, Method method4)
        {
            super(method, class1, method1, method2);
            setNpnProtocols = method3;
            getNpnSelectedProtocol = method4;
        }

    }

    private static class Java5 extends Platform
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

        private Java5(Method method)
        {
            getMtu = method;
        }

    }

    private static class JdkWithJettyNpnPlatform extends Java5
    {

        private final Class clientProviderClass;
        private final Method getMethod;
        private final Method putMethod;
        private final Class serverProviderClass;

        public byte[] getNpnSelectedProtocol(SSLSocket sslsocket)
        {
            JettyNpnProvider jettynpnprovider;
            jettynpnprovider = (JettyNpnProvider)Proxy.getInvocationHandler(getMethod.invoke(null, new Object[] {
                sslsocket
            }));
            if (jettynpnprovider.unsupported || jettynpnprovider.selected != null)
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
                    if (jettynpnprovider.unsupported)
                    {
                        break label0;
                    }
                    abyte0 = jettynpnprovider.selected.getBytes("US-ASCII");
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
            obj = Proxy.newProxyInstance(classloader, aclass, new JettyNpnProvider(arraylist));
            putMethod.invoke(null, new Object[] {
                sslsocket, obj
            });
            return;
        }

        public JdkWithJettyNpnPlatform(Method method, Method method1, Method method2, Class class1, Class class2)
        {
            super(method);
            putMethod = method1;
            getMethod = method2;
            clientProviderClass = class1;
            serverProviderClass = class2;
        }
    }

    private static class JettyNpnProvider
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



        public JettyNpnProvider(List list)
        {
            protocols = list;
        }
    }


    private static final Platform PLATFORM = findPlatform();
    private Constructor deflaterConstructor;

    public Platform()
    {
    }

    private static Platform findPlatform()
    {
        Method method;
        Class class5;
        Class aclass[];
        Method method1;
        Method method2;
        Android41 android41;
        try
        {
            method = java/net/NetworkInterface.getMethod("getMTU", new Class[0]);
        }
        catch (NoSuchMethodException nosuchmethodexception)
        {
            return new Platform();
        }
        class5 = Class.forName("org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
        aclass = new Class[1];
        aclass[0] = Boolean.TYPE;
        method1 = class5.getMethod("setUseSessionTickets", aclass);
        method2 = class5.getMethod("setHostname", new Class[] {
            java/lang/String
        });
        android41 = new Android41(method, class5, method1, method2, class5.getMethod("setNpnProtocols", new Class[] {
            [B
        }), class5.getMethod("getNpnSelectedProtocol", new Class[0]));
        return android41;
        NoSuchMethodException nosuchmethodexception3;
        nosuchmethodexception3;
        Android23 android23 = new Android23(method, class5, method1, method2);
        return android23;
        NoSuchMethodException nosuchmethodexception2;
        nosuchmethodexception2;
_L4:
        JdkWithJettyNpnPlatform jdkwithjettynpnplatform;
        Class class1 = Class.forName("org.eclipse.jetty.npn.NextProtoNego");
        Class class2 = Class.forName((new StringBuilder()).append("org.eclipse.jetty.npn.NextProtoNego").append("$Provider").toString());
        Class class3 = Class.forName((new StringBuilder()).append("org.eclipse.jetty.npn.NextProtoNego").append("$ClientProvider").toString());
        Class class4 = Class.forName((new StringBuilder()).append("org.eclipse.jetty.npn.NextProtoNego").append("$ServerProvider").toString());
        jdkwithjettynpnplatform = new JdkWithJettyNpnPlatform(method, class1.getMethod("put", new Class[] {
            javax/net/ssl/SSLSocket, class2
        }), class1.getMethod("get", new Class[] {
            javax/net/ssl/SSLSocket
        }), class3, class4);
        return jdkwithjettynpnplatform;
        NoSuchMethodException nosuchmethodexception1;
        nosuchmethodexception1;
_L2:
        if (method != null)
        {
            return new Java5(method);
        } else
        {
            return new Platform();
        }
        ClassNotFoundException classnotfoundexception1;
        classnotfoundexception1;
        if (true) goto _L2; else goto _L1
_L1:
        ClassNotFoundException classnotfoundexception;
        classnotfoundexception;
        if (true) goto _L4; else goto _L3
_L3:
    }

    public static Platform get()
    {
        return PLATFORM;
    }

    public void enableTlsExtensions(SSLSocket sslsocket, String s)
    {
    }

    public int getMtu(Socket socket)
        throws IOException
    {
        return 1400;
    }

    public byte[] getNpnSelectedProtocol(SSLSocket sslsocket)
    {
        return null;
    }

    public void logW(String s)
    {
        System.out.println(s);
    }

    public OutputStream newDeflaterOutputStream(OutputStream outputstream, Deflater deflater, boolean flag)
    {
        Constructor constructor;
        Class aclass[];
        Object aobj[];
        OutputStream outputstream1;
        try
        {
            constructor = deflaterConstructor;
        }
        catch (NoSuchMethodException nosuchmethodexception)
        {
            throw new UnsupportedOperationException("Cannot SPDY; no SYNC_FLUSH available");
        }
        catch (InvocationTargetException invocationtargetexception)
        {
            RuntimeException runtimeexception;
            if (invocationtargetexception.getCause() instanceof RuntimeException)
            {
                runtimeexception = (RuntimeException)invocationtargetexception.getCause();
            } else
            {
                runtimeexception = new RuntimeException(invocationtargetexception.getCause());
            }
            throw runtimeexception;
        }
        catch (InstantiationException instantiationexception)
        {
            throw new RuntimeException(instantiationexception);
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            throw new AssertionError();
        }
        if (constructor != null)
        {
            break MISSING_BLOCK_LABEL_51;
        }
        aclass = new Class[3];
        aclass[0] = java/io/OutputStream;
        aclass[1] = java/util/zip/Deflater;
        aclass[2] = Boolean.TYPE;
        constructor = java/util/zip/DeflaterOutputStream.getConstructor(aclass);
        deflaterConstructor = constructor;
        aobj = new Object[3];
        aobj[0] = outputstream;
        aobj[1] = deflater;
        aobj[2] = Boolean.valueOf(flag);
        outputstream1 = (OutputStream)constructor.newInstance(aobj);
        return outputstream1;
    }

    public void setNpnProtocols(SSLSocket sslsocket, byte abyte0[])
    {
    }

    public void supportTlsIntolerantServer(SSLSocket sslsocket)
    {
        sslsocket.setEnabledProtocols(new String[] {
            "SSLv3"
        });
    }

    public void tagSocket(Socket socket)
        throws SocketException
    {
    }

    public URI toUriLenient(URL url)
        throws URISyntaxException
    {
        return url.toURI();
    }

    public void untagSocket(Socket socket)
        throws SocketException
    {
    }

}
