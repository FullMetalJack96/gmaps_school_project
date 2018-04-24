// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;


final class HeaderParser
{
    public static interface CacheControlHandler
    {

        public abstract void handle(String s, String s1);
    }


    private HeaderParser()
    {
    }

    public static void parseCacheControl(String s, CacheControlHandler cachecontrolhandler)
    {
        for (int i = 0; i < s.length();)
        {
            int j = i;
            int k = skipUntil(s, i, "=,");
            String s1 = s.substring(j, k).trim();
            if (k == s.length() || s.charAt(k) == ',')
            {
                i = k + 1;
                cachecontrolhandler.handle(s1, null);
            } else
            {
                int l = skipWhitespace(s, k + 1);
                String s2;
                if (l < s.length() && s.charAt(l) == '"')
                {
                    int i1 = l + 1;
                    int j1 = skipUntil(s, i1, "\"");
                    s2 = s.substring(i1, j1);
                    i = j1 + 1;
                } else
                {
                    i = skipUntil(s, l, ",");
                    s2 = s.substring(l, i).trim();
                }
                cachecontrolhandler.handle(s1, s2);
            }
        }

    }

    public static int parseSeconds(String s)
    {
        long l;
        try
        {
            l = Long.parseLong(s);
        }
        catch (NumberFormatException numberformatexception)
        {
            return -1;
        }
        if (l > 0x7fffffffL)
        {
            return 0x7fffffff;
        }
        if (l < 0L)
        {
            return 0;
        } else
        {
            return (int)l;
        }
    }

    public static int skipUntil(String s, int i, String s1)
    {
        do
        {
            if (i >= s.length() || s1.indexOf(s.charAt(i)) != -1)
            {
                return i;
            }
            i++;
        } while (true);
    }

    public static int skipWhitespace(String s, int i)
    {
        do
        {
label0:
            {
                if (i < s.length())
                {
                    char c = s.charAt(i);
                    if (c == ' ' || c == '\t')
                    {
                        break label0;
                    }
                }
                return i;
            }
            i++;
        } while (true);
    }
}
