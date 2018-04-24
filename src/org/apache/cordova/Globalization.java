// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.text.format.DateFormat;
import android.text.format.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            GlobalizationError

public class Globalization extends CordovaPlugin
{

    public static final String CURRENCY = "currency";
    public static final String CURRENCYCODE = "currencyCode";
    public static final String DATE = "date";
    public static final String DATESTRING = "dateString";
    public static final String DATETOSTRING = "dateToString";
    public static final String DAYS = "days";
    public static final String FORMATLENGTH = "formatLength";
    public static final String FULL = "full";
    public static final String GETCURRENCYPATTERN = "getCurrencyPattern";
    public static final String GETDATENAMES = "getDateNames";
    public static final String GETDATEPATTERN = "getDatePattern";
    public static final String GETFIRSTDAYOFWEEK = "getFirstDayOfWeek";
    public static final String GETLOCALENAME = "getLocaleName";
    public static final String GETNUMBERPATTERN = "getNumberPattern";
    public static final String GETPREFERREDLANGUAGE = "getPreferredLanguage";
    public static final String ISDAYLIGHTSAVINGSTIME = "isDayLightSavingsTime";
    public static final String ITEM = "item";
    public static final String LONG = "long";
    public static final String MEDIUM = "medium";
    public static final String MONTHS = "months";
    public static final String NARROW = "narrow";
    public static final String NUMBER = "number";
    public static final String NUMBERSTRING = "numberString";
    public static final String NUMBERTOSTRING = "numberToString";
    public static final String OPTIONS = "options";
    public static final String PERCENT = "percent";
    public static final String SELECTOR = "selector";
    public static final String STRINGTODATE = "stringToDate";
    public static final String STRINGTONUMBER = "stringToNumber";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String WIDE = "wide";

    public Globalization()
    {
    }

    private JSONObject getCurrencyPattern(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            String s = jsonarray.getJSONObject(0).getString("currencyCode");
            DecimalFormat decimalformat = (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
            Currency currency = Currency.getInstance(s);
            decimalformat.setCurrency(currency);
            jsonobject.put("pattern", decimalformat.toPattern());
            jsonobject.put("code", currency.getCurrencyCode());
            jsonobject.put("fraction", decimalformat.getMinimumFractionDigits());
            jsonobject.put("rounding", new Integer(0));
            jsonobject.put("decimal", String.valueOf(decimalformat.getDecimalFormatSymbols().getDecimalSeparator()));
            jsonobject.put("grouping", String.valueOf(decimalformat.getDecimalFormatSymbols().getGroupingSeparator()));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("FORMATTING_ERROR");
        }
        return jsonobject;
    }

    private JSONObject getDateNames(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject;
        JSONArray jsonarray1;
        ArrayList arraylist;
        int l;
        final Map namesMap;
        jsonobject = new JSONObject();
        jsonarray1 = new JSONArray();
        arraylist = new ArrayList();
        int i;
        int j;
        int k;
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        Iterator iterator;
        try
        {
            i = jsonarray.getJSONObject(0).length();
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        j = 0;
        k = 0;
        if (i <= 0)
        {
            break MISSING_BLOCK_LABEL_185;
        }
        flag = ((JSONObject)jsonarray.getJSONObject(0).get("options")).isNull("type");
        k = 0;
        if (flag)
        {
            break MISSING_BLOCK_LABEL_115;
        }
        flag1 = ((String)((JSONObject)jsonarray.getJSONObject(0).get("options")).get("type")).equalsIgnoreCase("narrow");
        k = 0;
        if (flag1)
        {
            k = 0 + 1;
        }
        flag2 = ((JSONObject)jsonarray.getJSONObject(0).get("options")).isNull("item");
        j = 0;
        if (flag2)
        {
            break MISSING_BLOCK_LABEL_185;
        }
        flag3 = ((String)((JSONObject)jsonarray.getJSONObject(0).get("options")).get("item")).equalsIgnoreCase("days");
        j = 0;
        if (flag3)
        {
            j = 0 + 10;
        }
        l = j + k;
        if (l != 1) goto _L2; else goto _L1
_L1:
        namesMap = Calendar.getInstance().getDisplayNames(2, 1, Locale.getDefault());
_L3:
        for (iterator = namesMap.keySet().iterator(); iterator.hasNext(); arraylist.add((String)iterator.next())) { }
        break MISSING_BLOCK_LABEL_333;
_L2:
        if (l != 10)
        {
            break MISSING_BLOCK_LABEL_293;
        }
        namesMap = Calendar.getInstance().getDisplayNames(7, 2, Locale.getDefault());
          goto _L3
        if (l != 11)
        {
            break MISSING_BLOCK_LABEL_317;
        }
        namesMap = Calendar.getInstance().getDisplayNames(7, 1, Locale.getDefault());
          goto _L3
        namesMap = Calendar.getInstance().getDisplayNames(2, 2, Locale.getDefault());
          goto _L3
        Collections.sort(arraylist, new Comparator() {

            final Globalization this$0;
            final Map val$namesMap;

            public volatile int compare(Object obj, Object obj1)
            {
                return compare((String)obj, (String)obj1);
            }

            public int compare(String s, String s1)
            {
                return ((Integer)namesMap.get(s)).compareTo((Integer)namesMap.get(s1));
            }

            
            {
                this$0 = Globalization.this;
                namesMap = map;
                super();
            }
        });
        int i1 = 0;
_L5:
        if (i1 >= arraylist.size())
        {
            break; /* Loop/switch isn't completed */
        }
        jsonarray1.put(arraylist.get(i1));
        i1++;
        if (true) goto _L5; else goto _L4
_L4:
        JSONObject jsonobject1 = jsonobject.put("value", jsonarray1);
        return jsonobject1;
    }

    private JSONObject getDatePattern(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        SimpleDateFormat simpledateformat;
        SimpleDateFormat simpledateformat1;
        String s;
        simpledateformat = (SimpleDateFormat)DateFormat.getDateFormat(cordova.getActivity());
        simpledateformat1 = (SimpleDateFormat)DateFormat.getTimeFormat(cordova.getActivity());
        s = (new StringBuilder()).append(simpledateformat.toLocalizedPattern()).append(" ").append(simpledateformat1.toLocalizedPattern()).toString();
        jsonarray.getJSONObject(0).has("options");
        if (!jsonarray.getJSONObject(0).has("options")) goto _L2; else goto _L1
_L1:
        JSONObject jsonobject1 = jsonarray.getJSONObject(0).getJSONObject("options");
        if (jsonobject1.isNull("formatLength")) goto _L4; else goto _L3
_L3:
        String s3 = jsonobject1.getString("formatLength");
        if (!s3.equalsIgnoreCase("medium")) goto _L6; else goto _L5
_L5:
        simpledateformat = (SimpleDateFormat)DateFormat.getMediumDateFormat(cordova.getActivity());
_L4:
        s = (new StringBuilder()).append(simpledateformat.toLocalizedPattern()).append(" ").append(simpledateformat1.toLocalizedPattern()).toString();
        if (jsonobject1.isNull("selector")) goto _L2; else goto _L7
_L7:
        String s1 = jsonobject1.getString("selector");
        if (!s1.equalsIgnoreCase("date")) goto _L9; else goto _L8
_L8:
        s = simpledateformat.toLocalizedPattern();
_L2:
        TimeZone timezone = TimeZone.getTimeZone(Time.getCurrentTimezone());
        jsonobject.put("pattern", s);
        jsonobject.put("timezone", timezone.getDisplayName(timezone.inDaylightTime(Calendar.getInstance().getTime()), 0));
        jsonobject.put("utc_offset", timezone.getRawOffset() / 1000);
        jsonobject.put("dst_offset", timezone.getDSTSavings() / 1000);
        return jsonobject;
_L6:
        if (!s3.equalsIgnoreCase("long") && !s3.equalsIgnoreCase("full")) goto _L4; else goto _L10
_L10:
        simpledateformat = (SimpleDateFormat)DateFormat.getLongDateFormat(cordova.getActivity());
          goto _L4
_L9:
        if (!s1.equalsIgnoreCase("time")) goto _L2; else goto _L11
_L11:
        String s2 = simpledateformat1.toLocalizedPattern();
        s = s2;
          goto _L2
        Exception exception;
        exception;
        throw new GlobalizationError("PATTERN_ERROR");
          goto _L4
    }

    private JSONObject getDateToString(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject1;
        try
        {
            Date date = new Date(((Long)jsonarray.getJSONObject(0).get("date")).longValue());
            jsonobject1 = jsonobject.put("value", (new SimpleDateFormat(getDatePattern(jsonarray).getString("pattern"))).format(date));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("FORMATTING_ERROR");
        }
        return jsonobject1;
    }

    private JSONObject getFirstDayOfWeek(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject1;
        try
        {
            jsonobject1 = jsonobject.put("value", Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        return jsonobject1;
    }

    private JSONObject getIsDayLightSavingsTime(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject1;
        try
        {
            Date date = new Date(((Long)jsonarray.getJSONObject(0).get("date")).longValue());
            jsonobject1 = jsonobject.put("dst", TimeZone.getTimeZone(Time.getCurrentTimezone()).inDaylightTime(date));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        return jsonobject1;
    }

    private JSONObject getLocaleName()
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("value", Locale.getDefault().toString());
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        return jsonobject;
    }

    private DecimalFormat getNumberFormatInstance(JSONArray jsonarray)
        throws JSONException
    {
        DecimalFormat decimalformat = (DecimalFormat)DecimalFormat.getInstance(Locale.getDefault());
        DecimalFormat decimalformat1;
        if (jsonarray.getJSONObject(0).length() <= 1 || ((JSONObject)jsonarray.getJSONObject(0).get("options")).isNull("type"))
        {
            break MISSING_BLOCK_LABEL_111;
        }
        String s = (String)((JSONObject)jsonarray.getJSONObject(0).get("options")).get("type");
        if (s.equalsIgnoreCase("currency"))
        {
            return (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
        }
        if (!s.equalsIgnoreCase("percent"))
        {
            break MISSING_BLOCK_LABEL_111;
        }
        decimalformat1 = (DecimalFormat)DecimalFormat.getPercentInstance(Locale.getDefault());
        return decimalformat1;
        JSONException jsonexception;
        jsonexception;
        return decimalformat;
    }

    private JSONObject getNumberPattern(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        DecimalFormat decimalformat;
        String s;
        decimalformat = (DecimalFormat)DecimalFormat.getInstance(Locale.getDefault());
        s = String.valueOf(decimalformat.getDecimalFormatSymbols().getDecimalSeparator());
        if (jsonarray.getJSONObject(0).length() <= 0 || ((JSONObject)jsonarray.getJSONObject(0).get("options")).isNull("type")) goto _L2; else goto _L1
_L1:
        String s1 = (String)((JSONObject)jsonarray.getJSONObject(0).get("options")).get("type");
        if (!s1.equalsIgnoreCase("currency")) goto _L4; else goto _L3
_L3:
        decimalformat = (DecimalFormat)DecimalFormat.getCurrencyInstance(Locale.getDefault());
        s = decimalformat.getDecimalFormatSymbols().getCurrencySymbol();
_L2:
        jsonobject.put("pattern", decimalformat.toPattern());
        jsonobject.put("symbol", s);
        jsonobject.put("fraction", decimalformat.getMinimumFractionDigits());
        jsonobject.put("rounding", new Integer(0));
        jsonobject.put("positive", decimalformat.getPositivePrefix());
        jsonobject.put("negative", decimalformat.getNegativePrefix());
        jsonobject.put("decimal", String.valueOf(decimalformat.getDecimalFormatSymbols().getDecimalSeparator()));
        jsonobject.put("grouping", String.valueOf(decimalformat.getDecimalFormatSymbols().getGroupingSeparator()));
        return jsonobject;
_L4:
        if (!s1.equalsIgnoreCase("percent")) goto _L2; else goto _L5
_L5:
        String s2;
        decimalformat = (DecimalFormat)DecimalFormat.getPercentInstance(Locale.getDefault());
        s2 = String.valueOf(decimalformat.getDecimalFormatSymbols().getPercent());
        s = s2;
          goto _L2
        Exception exception;
        exception;
        throw new GlobalizationError("PATTERN_ERROR");
    }

    private JSONObject getNumberToString(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject1;
        try
        {
            jsonobject1 = jsonobject.put("value", getNumberFormatInstance(jsonarray).format(jsonarray.getJSONObject(0).get("number")));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("FORMATTING_ERROR");
        }
        return jsonobject1;
    }

    private JSONObject getPreferredLanguage()
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("value", Locale.getDefault().getDisplayLanguage().toString());
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        return jsonobject;
    }

    private JSONObject getStringToNumber(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        JSONObject jsonobject1;
        try
        {
            jsonobject1 = jsonobject.put("value", getNumberFormatInstance(jsonarray).parse((String)jsonarray.getJSONObject(0).get("numberString")));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("PARSING_ERROR");
        }
        return jsonobject1;
    }

    private JSONObject getStringtoDate(JSONArray jsonarray)
        throws GlobalizationError
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            Date date = (new SimpleDateFormat(getDatePattern(jsonarray).getString("pattern"))).parse(jsonarray.getJSONObject(0).get("dateString").toString());
            Time time = new Time();
            time.set(date.getTime());
            jsonobject.put("year", time.year);
            jsonobject.put("month", time.month);
            jsonobject.put("day", time.monthDay);
            jsonobject.put("hour", time.hour);
            jsonobject.put("minute", time.minute);
            jsonobject.put("second", time.second);
            jsonobject.put("millisecond", new Long(0L));
        }
        catch (Exception exception)
        {
            throw new GlobalizationError("PARSING_ERROR");
        }
        return jsonobject;
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
    {
        new JSONObject();
        if (!s.equals("getLocaleName")) goto _L2; else goto _L1
_L1:
        JSONObject jsonobject1 = getLocaleName();
_L4:
        callbackcontext.success(jsonobject1);
        break MISSING_BLOCK_LABEL_305;
_L2:
        if (s.equals("getPreferredLanguage"))
        {
            jsonobject1 = getPreferredLanguage();
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("dateToString"))
        {
            jsonobject1 = getDateToString(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("stringToDate"))
        {
            jsonobject1 = getStringtoDate(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("getDatePattern"))
        {
            jsonobject1 = getDatePattern(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        GlobalizationError globalizationerror;
        if (!s.equalsIgnoreCase("getDateNames"))
        {
            break MISSING_BLOCK_LABEL_169;
        }
        if (android.os.Build.VERSION.SDK_INT < 9)
        {
            throw new GlobalizationError("UNKNOWN_ERROR");
        }
        JSONObject jsonobject;
        try
        {
            jsonobject1 = getDateNames(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        // Misplaced declaration of an exception variable
        catch (GlobalizationError globalizationerror)
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, globalizationerror.toJson()));
            break MISSING_BLOCK_LABEL_305;
        }
        catch (Exception exception)
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.JSON_EXCEPTION));
        }
        break MISSING_BLOCK_LABEL_305;
        if (s.equalsIgnoreCase("isDayLightSavingsTime"))
        {
            jsonobject1 = getIsDayLightSavingsTime(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("getFirstDayOfWeek"))
        {
            jsonobject1 = getFirstDayOfWeek(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("numberToString"))
        {
            jsonobject1 = getNumberToString(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("stringToNumber"))
        {
            jsonobject1 = getStringToNumber(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (s.equalsIgnoreCase("getNumberPattern"))
        {
            jsonobject1 = getNumberPattern(jsonarray);
            continue; /* Loop/switch isn't completed */
        }
        if (!s.equalsIgnoreCase("getCurrencyPattern"))
        {
            break; /* Loop/switch isn't completed */
        }
        jsonobject = getCurrencyPattern(jsonarray);
        jsonobject1 = jsonobject;
        if (true) goto _L4; else goto _L3
_L3:
        return false;
        return true;
    }
}
