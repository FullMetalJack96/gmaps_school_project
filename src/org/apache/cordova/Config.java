// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.api.LOG;
import org.xmlpull.v1.XmlPullParserException;

public class Config
{

    public static final String TAG = "Config";
    private static Config self = null;
    private String startUrl;
    private ArrayList whiteList;
    private HashMap whiteListCache;

    private Config()
    {
        whiteList = new ArrayList();
        whiteListCache = new HashMap();
    }

    private Config(Activity activity)
    {
        whiteList = new ArrayList();
        whiteListCache = new HashMap();
        if (activity != null) goto _L2; else goto _L1
_L1:
        LOG.i("CordovaLog", "There is no activity. Is this on the lock screen?");
_L6:
        return;
_L2:
        XmlResourceParser xmlresourceparser;
        int j;
        int i = activity.getResources().getIdentifier("config", "xml", activity.getPackageName());
        if (i == 0)
        {
            i = activity.getResources().getIdentifier("cordova", "xml", activity.getPackageName());
            LOG.i("CordovaLog", "config.xml missing, reverting to cordova.xml");
        }
        if (i == 0)
        {
            LOG.i("CordovaLog", "cordova.xml missing. Ignoring...");
            return;
        }
        xmlresourceparser = activity.getResources().getXml(i);
        j = -1;
_L4:
        if (j == 1)
        {
            continue; /* Loop/switch isn't completed */
        }
        if (j == 2)
        {
            String s = xmlresourceparser.getName();
            if (s.equals("access"))
            {
                String s6 = xmlresourceparser.getAttributeValue(null, "origin");
                String s7 = xmlresourceparser.getAttributeValue(null, "subdomains");
                if (s6 != null)
                {
                    int k;
                    boolean flag3;
                    if (s7 != null && s7.compareToIgnoreCase("true") == 0)
                    {
                        flag3 = true;
                    } else
                    {
                        flag3 = false;
                    }
                    _addWhiteListEntry(s6, flag3);
                }
            } else
            if (s.equals("log"))
            {
                String s5 = xmlresourceparser.getAttributeValue(null, "level");
                Log.d("Config", (new StringBuilder()).append("The <log> tags is deprecated. Use <preference name=\"loglevel\" value=\"").append(s5).append("\"/> instead.").toString());
                if (s5 != null)
                {
                    LOG.setLogLevel(s5);
                }
            } else
            if (s.equals("preference"))
            {
                String s2 = xmlresourceparser.getAttributeValue(null, "name");
                if (s2.equals("loglevel"))
                {
                    LOG.setLogLevel(xmlresourceparser.getAttributeValue(null, "value"));
                } else
                if (s2.equals("splashscreen"))
                {
                    String s4 = xmlresourceparser.getAttributeValue(null, "value");
                    if (s4 == null)
                    {
                        s4 = "splash";
                    }
                    int j1 = activity.getResources().getIdentifier(s4, "drawable", activity.getPackageName());
                    activity.getIntent().putExtra(s2, j1);
                } else
                if (s2.equals("backgroundColor"))
                {
                    int i1 = xmlresourceparser.getAttributeIntValue(null, "value", 0xff000000);
                    activity.getIntent().putExtra(s2, i1);
                } else
                if (s2.equals("loadUrlTimeoutValue"))
                {
                    int l = xmlresourceparser.getAttributeIntValue(null, "value", 20000);
                    activity.getIntent().putExtra(s2, l);
                } else
                if (s2.equals("keepRunning"))
                {
                    boolean flag2 = xmlresourceparser.getAttributeValue(null, "value").equals("true");
                    activity.getIntent().putExtra(s2, flag2);
                } else
                if (s2.equals("InAppBrowserStorageEnabled"))
                {
                    boolean flag1 = xmlresourceparser.getAttributeValue(null, "value").equals("true");
                    activity.getIntent().putExtra(s2, flag1);
                } else
                if (s2.equals("disallowOverscroll"))
                {
                    boolean flag = xmlresourceparser.getAttributeValue(null, "value").equals("true");
                    activity.getIntent().putExtra(s2, flag);
                } else
                {
                    String s3 = xmlresourceparser.getAttributeValue(null, "value");
                    activity.getIntent().putExtra(s2, s3);
                }
            } else
            if (s.equals("content"))
            {
                String s1 = xmlresourceparser.getAttributeValue(null, "src");
                LOG.i("CordovaLog", "Found start page location: %s", new Object[] {
                    s1
                });
                if (s1 != null)
                {
                    if (Pattern.compile("^[a-z-]+://").matcher(s1).find())
                    {
                        startUrl = s1;
                    } else
                    {
                        if (s1.charAt(0) == '/')
                        {
                            s1 = s1.substring(1);
                        }
                        startUrl = (new StringBuilder()).append("file:///android_asset/www/").append(s1).toString();
                    }
                }
            }
        }
        k = xmlresourceparser.next();
        j = k;
        break; /* Loop/switch isn't completed */
        XmlPullParserException xmlpullparserexception;
        xmlpullparserexception;
        xmlpullparserexception.printStackTrace();
        break; /* Loop/switch isn't completed */
        IOException ioexception;
        ioexception;
        ioexception.printStackTrace();
        if (true) goto _L4; else goto _L3
_L3:
        if (true) goto _L6; else goto _L5
_L5:
    }

    private void _addWhiteListEntry(String s, boolean flag)
    {
        Matcher matcher;
        try
        {
            if (s.compareTo("*") == 0)
            {
                LOG.d("Config", "Unlimited access to network resources");
                whiteList.add(Pattern.compile(".*"));
                return;
            }
        }
        catch (Exception exception)
        {
            LOG.d("Config", "Failed to add origin %s", new Object[] {
                s
            });
            return;
        }
        matcher = Pattern.compile("^[a-z-]+://").matcher(s);
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_194;
        }
        if (!s.startsWith("http")) goto _L2; else goto _L1
_L1:
        whiteList.add(Pattern.compile(s.replaceFirst("https?://", "^https?://(.*\\.)?")));
_L3:
        LOG.d("Config", "Origin to allow with subdomains: %s", new Object[] {
            s
        });
        return;
_L2:
label0:
        {
            if (!matcher.find())
            {
                break label0;
            }
            whiteList.add(Pattern.compile((new StringBuilder()).append("^").append(s.replaceFirst("//", "//(.*\\.)?")).toString()));
        }
          goto _L3
        whiteList.add(Pattern.compile((new StringBuilder()).append("^https?://(.*\\.)?").append(s).toString()));
          goto _L3
        if (!s.startsWith("http")) goto _L5; else goto _L4
_L4:
        whiteList.add(Pattern.compile(s.replaceFirst("https?://", "^https?://")));
_L6:
        LOG.d("Config", "Origin to allow: %s", new Object[] {
            s
        });
        return;
_L5:
label1:
        {
            if (!matcher.find())
            {
                break label1;
            }
            whiteList.add(Pattern.compile((new StringBuilder()).append("^").append(s).toString()));
        }
          goto _L6
        whiteList.add(Pattern.compile((new StringBuilder()).append("^https?://").append(s).toString()));
          goto _L6
    }

    public static void addWhiteListEntry(String s, boolean flag)
    {
        if (self == null)
        {
            return;
        } else
        {
            self._addWhiteListEntry(s, flag);
            return;
        }
    }

    public static String getStartUrl()
    {
        if (self == null || self.startUrl == null)
        {
            return "file:///android_asset/www/index.html";
        } else
        {
            return self.startUrl;
        }
    }

    public static void init()
    {
        if (self == null)
        {
            self = new Config();
        }
    }

    public static void init(Activity activity)
    {
        self = new Config(activity);
    }

    public static boolean isUrlWhiteListed(String s)
    {
        if (self != null)
        {
            if (self.whiteListCache.get(s) != null)
            {
                return true;
            }
            Iterator iterator = self.whiteList.iterator();
            while (iterator.hasNext()) 
            {
                if (((Pattern)iterator.next()).matcher(s).find())
                {
                    self.whiteListCache.put(s, Boolean.valueOf(true));
                    return true;
                }
            }
        }
        return false;
    }

}
