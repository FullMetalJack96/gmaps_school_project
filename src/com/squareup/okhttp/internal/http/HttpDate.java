// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final class HttpDate
{

    private static final String BROWSER_COMPATIBLE_DATE_FORMATS[] = {
        "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", 
        "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z"
    };
    private static final ThreadLocal STANDARD_DATE_FORMAT = new ThreadLocal() {

        protected volatile Object initialValue()
        {
            return initialValue();
        }

        protected DateFormat initialValue()
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            simpledateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpledateformat;
        }

    };

    private HttpDate()
    {
    }

    public static String format(Date date)
    {
        return ((DateFormat)STANDARD_DATE_FORMAT.get()).format(date);
    }

    public static Date parse(String s)
    {
        Date date1 = ((DateFormat)STANDARD_DATE_FORMAT.get()).parse(s);
        return date1;
        ParseException parseexception;
        parseexception;
        String as[];
        int i;
        int j;
        as = BROWSER_COMPATIBLE_DATE_FORMATS;
        i = as.length;
        j = 0;
_L2:
        String s1;
        if (j >= i)
        {
            break; /* Loop/switch isn't completed */
        }
        s1 = as[j];
        Date date = (new SimpleDateFormat(s1, Locale.US)).parse(s);
        return date;
        ParseException parseexception1;
        parseexception1;
        j++;
        if (true) goto _L2; else goto _L1
_L1:
        return null;
    }

}
