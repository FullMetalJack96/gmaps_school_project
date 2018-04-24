// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;


// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpAuthenticator

private static final class realm
{

    final String realm;
    final String scheme;

    public boolean equals(Object obj)
    {
        return (obj instanceof realm) && ((realm)obj).scheme.equals(scheme) && ((scheme)obj).realm.equals(realm);
    }

    public int hashCode()
    {
        return scheme.hashCode() + 31 * realm.hashCode();
    }

    (String s, String s1)
    {
        scheme = s;
        realm = s1;
    }
}
