// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class RawHeaders
{

    private static final Comparator FIELD_NAME_COMPARATOR = new Comparator() {

        public volatile int compare(Object obj, Object obj1)
        {
            return compare((String)obj, (String)obj1);
        }

        public int compare(String s, String s1)
        {
            if (s == s1)
            {
                return 0;
            }
            if (s == null)
            {
                return -1;
            }
            if (s1 == null)
            {
                return 1;
            } else
            {
                return String.CASE_INSENSITIVE_ORDER.compare(s, s1);
            }
        }

    };
    private int httpMinorVersion;
    private final List namesAndValues;
    private String requestLine;
    private int responseCode;
    private String responseMessage;
    private String statusLine;

    public RawHeaders()
    {
        namesAndValues = new ArrayList(20);
        httpMinorVersion = 1;
        responseCode = -1;
    }

    public RawHeaders(RawHeaders rawheaders)
    {
        namesAndValues = new ArrayList(20);
        httpMinorVersion = 1;
        responseCode = -1;
        namesAndValues.addAll(rawheaders.namesAndValues);
        requestLine = rawheaders.requestLine;
        statusLine = rawheaders.statusLine;
        httpMinorVersion = rawheaders.httpMinorVersion;
        responseCode = rawheaders.responseCode;
        responseMessage = rawheaders.responseMessage;
    }

    private void addLenient(String s, String s1)
    {
        namesAndValues.add(s);
        namesAndValues.add(s1.trim());
    }

    public static RawHeaders fromBytes(InputStream inputstream)
        throws IOException
    {
        RawHeaders rawheaders;
        do
        {
            rawheaders = new RawHeaders();
            rawheaders.setStatusLine(Util.readAsciiLine(inputstream));
            readHeaders(inputstream, rawheaders);
        } while (rawheaders.getResponseCode() == 100);
        return rawheaders;
    }

    public static RawHeaders fromMultimap(Map map, boolean flag)
        throws IOException
    {
        if (!flag)
        {
            throw new UnsupportedOperationException();
        }
        RawHeaders rawheaders = new RawHeaders();
        Iterator iterator = map.entrySet().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String s = (String)entry.getKey();
            List list = (List)entry.getValue();
            if (s != null)
            {
                Iterator iterator1 = list.iterator();
                while (iterator1.hasNext()) 
                {
                    rawheaders.addLenient(s, (String)iterator1.next());
                }
            } else
            if (!list.isEmpty())
            {
                rawheaders.setStatusLine((String)list.get(-1 + list.size()));
            }
        } while (true);
        return rawheaders;
    }

    public static RawHeaders fromNameValueBlock(List list)
    {
        if (list.size() % 2 != 0)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unexpected name value block: ").append(list).toString());
        }
        RawHeaders rawheaders = new RawHeaders();
        for (int i = 0; i < list.size(); i += 2)
        {
            String s = (String)list.get(i);
            String s1 = (String)list.get(i + 1);
            int k;
            for (int j = 0; j < s1.length(); j = k + 1)
            {
                k = s1.indexOf('\0', j);
                if (k == -1)
                {
                    k = s1.length();
                }
                rawheaders.namesAndValues.add(s);
                rawheaders.namesAndValues.add(s1.substring(j, k));
            }

        }

        return rawheaders;
    }

    public static void readHeaders(InputStream inputstream, RawHeaders rawheaders)
        throws IOException
    {
        do
        {
            String s = Util.readAsciiLine(inputstream);
            if (s.length() != 0)
            {
                rawheaders.addLine(s);
            } else
            {
                return;
            }
        } while (true);
    }

    public void add(String s, String s1)
    {
        if (s == null)
        {
            throw new IllegalArgumentException("fieldname == null");
        }
        if (s1 == null)
        {
            throw new IllegalArgumentException("value == null");
        }
        if (s.length() == 0 || s.indexOf('\0') != -1 || s1.indexOf('\0') != -1)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unexpected header: ").append(s).append(": ").append(s1).toString());
        } else
        {
            addLenient(s, s1);
            return;
        }
    }

    public void addAll(String s, List list)
    {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); add(s, (String)iterator.next())) { }
    }

    public void addLine(String s)
    {
        int i = s.indexOf(":");
        if (i == -1)
        {
            addLenient("", s);
            return;
        } else
        {
            addLenient(s.substring(0, i), s.substring(i + 1));
            return;
        }
    }

    public void addSpdyRequestHeaders(String s, String s1, String s2, String s3, String s4)
    {
        add(":method", s);
        add(":scheme", s4);
        add(":path", s1);
        add(":version", s2);
        add(":host", s3);
    }

    public void computeResponseStatusLineFromSpdyHeaders()
        throws IOException
    {
        String s = null;
        String s1 = null;
        int i = 0;
        while (i < namesAndValues.size()) 
        {
            String s2 = (String)namesAndValues.get(i);
            if (":status".equals(s2))
            {
                s = (String)namesAndValues.get(i + 1);
            } else
            if (":version".equals(s2))
            {
                s1 = (String)namesAndValues.get(i + 1);
            }
            i += 2;
        }
        if (s == null || s1 == null)
        {
            throw new ProtocolException("Expected ':status' and ':version' headers not present");
        } else
        {
            setStatusLine((new StringBuilder()).append(s1).append(" ").append(s).toString());
            return;
        }
    }

    public String get(String s)
    {
        for (int i = -2 + namesAndValues.size(); i >= 0; i -= 2)
        {
            if (s.equalsIgnoreCase((String)namesAndValues.get(i)))
            {
                return (String)namesAndValues.get(i + 1);
            }
        }

        return null;
    }

    public RawHeaders getAll(Set set1)
    {
        RawHeaders rawheaders = new RawHeaders();
        for (int i = 0; i < namesAndValues.size(); i += 2)
        {
            String s = (String)namesAndValues.get(i);
            if (set1.contains(s))
            {
                rawheaders.add(s, (String)namesAndValues.get(i + 1));
            }
        }

        return rawheaders;
    }

    public String getFieldName(int i)
    {
        int j = i * 2;
        if (j < 0 || j >= namesAndValues.size())
        {
            return null;
        } else
        {
            return (String)namesAndValues.get(j);
        }
    }

    public int getHttpMinorVersion()
    {
        if (httpMinorVersion != -1)
        {
            return httpMinorVersion;
        } else
        {
            return 1;
        }
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public String getStatusLine()
    {
        return statusLine;
    }

    public String getValue(int i)
    {
        int j = 1 + i * 2;
        if (j < 0 || j >= namesAndValues.size())
        {
            return null;
        } else
        {
            return (String)namesAndValues.get(j);
        }
    }

    public int length()
    {
        return namesAndValues.size() / 2;
    }

    public void removeAll(String s)
    {
        for (int i = 0; i < namesAndValues.size(); i += 2)
        {
            if (s.equalsIgnoreCase((String)namesAndValues.get(i)))
            {
                namesAndValues.remove(i);
                namesAndValues.remove(i);
            }
        }

    }

    public void set(String s, String s1)
    {
        removeAll(s);
        add(s, s1);
    }

    public void setRequestLine(String s)
    {
        requestLine = s.trim();
    }

    public void setStatusLine(String s)
        throws IOException
    {
        if (responseMessage != null)
        {
            throw new IllegalStateException("statusLine is already set");
        }
        boolean flag;
        if (s.length() > 13)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        if (!s.startsWith("HTTP/1.") || s.length() < 12 || s.charAt(8) != ' ' || flag && s.charAt(12) != ' ')
        {
            throw new ProtocolException((new StringBuilder()).append("Unexpected status line: ").append(s).toString());
        }
        int i = -48 + s.charAt(7);
        if (i < 0 || i > 9)
        {
            throw new ProtocolException((new StringBuilder()).append("Unexpected status line: ").append(s).toString());
        }
        int j;
        String s1;
        try
        {
            j = Integer.parseInt(s.substring(9, 12));
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new ProtocolException((new StringBuilder()).append("Unexpected status line: ").append(s).toString());
        }
        if (flag)
        {
            s1 = s.substring(13);
        } else
        {
            s1 = "";
        }
        responseMessage = s1;
        responseCode = j;
        statusLine = s;
        httpMinorVersion = i;
    }

    public byte[] toBytes()
        throws UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder(256);
        stringbuilder.append(requestLine).append("\r\n");
        for (int i = 0; i < namesAndValues.size(); i += 2)
        {
            stringbuilder.append((String)namesAndValues.get(i)).append(": ").append((String)namesAndValues.get(i + 1)).append("\r\n");
        }

        stringbuilder.append("\r\n");
        return stringbuilder.toString().getBytes("ISO-8859-1");
    }

    public Map toMultimap(boolean flag)
    {
        TreeMap treemap;
        treemap = new TreeMap(FIELD_NAME_COMPARATOR);
        for (int i = 0; i < namesAndValues.size(); i += 2)
        {
            String s = (String)namesAndValues.get(i);
            String s1 = (String)namesAndValues.get(i + 1);
            ArrayList arraylist = new ArrayList();
            List list = (List)treemap.get(s);
            if (list != null)
            {
                arraylist.addAll(list);
            }
            arraylist.add(s1);
            treemap.put(s, Collections.unmodifiableList(arraylist));
        }

        if (!flag || statusLine == null) goto _L2; else goto _L1
_L1:
        treemap.put(null, Collections.unmodifiableList(Collections.singletonList(statusLine)));
_L4:
        return Collections.unmodifiableMap(treemap);
_L2:
        if (requestLine != null)
        {
            treemap.put(null, Collections.unmodifiableList(Collections.singletonList(requestLine)));
        }
        if (true) goto _L4; else goto _L3
_L3:
    }

    public List toNameValueBlock()
    {
        HashSet hashset;
        ArrayList arraylist;
        int i;
        hashset = new HashSet();
        arraylist = new ArrayList();
        i = 0;
_L2:
        String s;
        String s1;
        if (i >= namesAndValues.size())
        {
            break MISSING_BLOCK_LABEL_249;
        }
        s = ((String)namesAndValues.get(i)).toLowerCase(Locale.US);
        s1 = (String)namesAndValues.get(i + 1);
        if (!s.equals("connection") && !s.equals("host") && !s.equals("keep-alive") && !s.equals("proxy-connection") && !s.equals("transfer-encoding"))
        {
            break; /* Loop/switch isn't completed */
        }
_L3:
        i += 2;
        if (true) goto _L2; else goto _L1
_L1:
label0:
        {
            if (!hashset.add(s))
            {
                break label0;
            }
            arraylist.add(s);
            arraylist.add(s1);
        }
          goto _L3
        int j = 0;
_L4:
        if (j < arraylist.size())
        {
label1:
            {
                if (!s.equals(arraylist.get(j)))
                {
                    break label1;
                }
                arraylist.set(j + 1, (new StringBuilder()).append((String)arraylist.get(j + 1)).append("\0").append(s1).toString());
            }
        }
          goto _L3
        j += 2;
          goto _L4
        return arraylist;
          goto _L3
    }

}
