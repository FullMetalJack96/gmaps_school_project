// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils
{

    public JSONUtils()
    {
    }

    public static List toStringList(JSONArray jsonarray)
        throws JSONException
    {
        Object obj;
        if (jsonarray == null)
        {
            obj = null;
        } else
        {
            obj = new ArrayList();
            int i = 0;
            while (i < jsonarray.length()) 
            {
                ((List) (obj)).add(jsonarray.get(i).toString());
                i++;
            }
        }
        return ((List) (obj));
    }
}
