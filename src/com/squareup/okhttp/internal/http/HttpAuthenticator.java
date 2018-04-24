// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Base64;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            RawHeaders, HeaderParser

public final class HttpAuthenticator
{
    private static final class Challenge
    {

        final String realm;
        final String scheme;

        public boolean equals(Object obj)
        {
            return (obj instanceof Challenge) && ((Challenge)obj).scheme.equals(scheme) && ((Challenge)obj).realm.equals(realm);
        }

        public int hashCode()
        {
            return scheme.hashCode() + 31 * realm.hashCode();
        }

        Challenge(String s, String s1)
        {
            scheme = s;
            realm = s1;
        }
    }


    private HttpAuthenticator()
    {
    }

    private static InetAddress getConnectToInetAddress(Proxy proxy, URL url)
        throws IOException
    {
        if (proxy != null && proxy.type() != java.net.Proxy.Type.DIRECT)
        {
            return ((InetSocketAddress)proxy.address()).getAddress();
        } else
        {
            return InetAddress.getByName(url.getHost());
        }
    }

    private static String getCredentials(RawHeaders rawheaders, String s, Proxy proxy, URL url)
        throws IOException
    {
        List list = parseChallenges(rawheaders, s);
        if (list.isEmpty())
        {
            return null;
        }
        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Challenge challenge = (Challenge)iterator.next();
            PasswordAuthentication passwordauthentication;
            if (rawheaders.getResponseCode() == 407)
            {
                InetSocketAddress inetsocketaddress = (InetSocketAddress)proxy.address();
                passwordauthentication = Authenticator.requestPasswordAuthentication(inetsocketaddress.getHostName(), getConnectToInetAddress(proxy, url), inetsocketaddress.getPort(), url.getProtocol(), challenge.realm, challenge.scheme, url, java.net.Authenticator.RequestorType.PROXY);
            } else
            {
                passwordauthentication = Authenticator.requestPasswordAuthentication(url.getHost(), getConnectToInetAddress(proxy, url), url.getPort(), url.getProtocol(), challenge.realm, challenge.scheme, url, java.net.Authenticator.RequestorType.SERVER);
            }
            if (passwordauthentication != null)
            {
                String s1 = Base64.encode((new StringBuilder()).append(passwordauthentication.getUserName()).append(":").append(new String(passwordauthentication.getPassword())).toString().getBytes("ISO-8859-1"));
                return (new StringBuilder()).append(challenge.scheme).append(" ").append(s1).toString();
            }
        }

        return null;
    }

    private static List parseChallenges(RawHeaders rawheaders, String s)
    {
        ArrayList arraylist;
        int i;
        arraylist = new ArrayList();
        i = 0;
_L2:
        if (i >= rawheaders.length())
        {
            break MISSING_BLOCK_LABEL_185;
        }
        if (s.equalsIgnoreCase(rawheaders.getFieldName(i)))
        {
            break; /* Loop/switch isn't completed */
        }
_L4:
        i++;
        if (true) goto _L2; else goto _L1
_L1:
        String s1;
        int j;
        s1 = rawheaders.getValue(i);
        j = 0;
_L6:
        if (j >= s1.length()) goto _L4; else goto _L3
_L3:
        String s2;
        int i1;
        int k = j;
        int l = HeaderParser.skipUntil(s1, j, " ");
        s2 = s1.substring(k, l).trim();
        i1 = HeaderParser.skipWhitespace(s1, l);
        if (!s1.regionMatches(i1, "realm=\"", 0, "realm=\"".length())) goto _L4; else goto _L5
_L5:
        int j1 = i1 + "realm=\"".length();
        int k1 = HeaderParser.skipUntil(s1, j1, "\"");
        String s3 = s1.substring(j1, k1);
        j = HeaderParser.skipWhitespace(s1, 1 + HeaderParser.skipUntil(s1, k1 + 1, ","));
        arraylist.add(new Challenge(s2, s3));
          goto _L6
        return arraylist;
    }

    public static boolean processAuthHeader(int i, RawHeaders rawheaders, RawHeaders rawheaders1, Proxy proxy, URL url)
        throws IOException
    {
        if (i != 407 && i != 401)
        {
            throw new IllegalArgumentException();
        }
        String s;
        String s1;
        if (i == 407)
        {
            s = "Proxy-Authenticate";
        } else
        {
            s = "WWW-Authenticate";
        }
        s1 = getCredentials(rawheaders, s, proxy, url);
        if (s1 == null)
        {
            return false;
        }
        String s2;
        if (i == 407)
        {
            s2 = "Proxy-Authorization";
        } else
        {
            s2 = "Authorization";
        }
        rawheaders1.set(s2, s1);
        return true;
    }
}
