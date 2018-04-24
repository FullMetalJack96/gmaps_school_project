// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.io.File;
import java.io.PrintStream;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView

public class Storage extends CordovaPlugin
{

    private static final String ALTER = "alter";
    private static final String CREATE = "create";
    private static final String DROP = "drop";
    private static final String TRUNCATE = "truncate";
    String dbName;
    SQLiteDatabase myDb;
    String path;

    public Storage()
    {
        myDb = null;
        path = null;
        dbName = null;
    }

    private boolean isDDL(String s)
    {
        String s1 = s.toLowerCase();
        return s1.startsWith("drop") || s1.startsWith("create") || s1.startsWith("alter") || s1.startsWith("truncate");
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (!s.equals("openDatabase")) goto _L2; else goto _L1
_L1:
        openDatabase(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getString(2), jsonarray.getLong(3));
_L6:
        boolean flag1;
        callbackcontext.success();
        flag1 = true;
_L4:
        return flag1;
_L2:
        boolean flag;
        flag = s.equals("executeSql");
        flag1 = false;
        if (!flag) goto _L4; else goto _L3
_L3:
        String as[];
        if (!jsonarray.isNull(1))
        {
            break; /* Loop/switch isn't completed */
        }
        as = new String[0];
_L7:
        executeSql(jsonarray.getString(0), as, jsonarray.getString(2));
        if (true) goto _L6; else goto _L5
_L5:
        JSONArray jsonarray1 = jsonarray.getJSONArray(1);
        int i = jsonarray1.length();
        as = new String[i];
        int j = 0;
        while (j < i) 
        {
            as[j] = jsonarray1.getString(j);
            j++;
        }
          goto _L7
        if (true) goto _L6; else goto _L8
_L8:
    }

    public void executeSql(String s, String as[], String s1)
    {
        Cursor cursor;
        try
        {
            if (isDDL(s))
            {
                myDb.execSQL(s);
                webView.sendJavascript((new StringBuilder()).append("cordova.require('cordova/plugin/android/storage').completeQuery('").append(s1).append("', '');").toString());
                return;
            }
        }
        catch (SQLiteException sqliteexception)
        {
            sqliteexception.printStackTrace();
            System.out.println((new StringBuilder()).append("Storage.executeSql(): Error=").append(sqliteexception.getMessage()).toString());
            webView.sendJavascript((new StringBuilder()).append("cordova.require('cordova/plugin/android/storage').failQuery('").append(sqliteexception.getMessage()).append("','").append(s1).append("');").toString());
            return;
        }
        cursor = myDb.rawQuery(s, as);
        processResults(cursor, s1);
        cursor.close();
        return;
    }

    public void onDestroy()
    {
        if (myDb != null)
        {
            myDb.close();
            myDb = null;
        }
    }

    public void onReset()
    {
        onDestroy();
    }

    public void openDatabase(String s, String s1, String s2, long l)
    {
        if (myDb != null)
        {
            myDb.close();
        }
        if (path == null)
        {
            path = cordova.getActivity().getApplicationContext().getDir("database", 0).getPath();
        }
        dbName = (new StringBuilder()).append(path).append(File.separator).append(s).append(".db").toString();
        File file = new File((new StringBuilder()).append(path).append(File.pathSeparator).append(s).append(".db").toString());
        if (file.exists())
        {
            File file1 = new File(path);
            File file2 = new File(dbName);
            file1.mkdirs();
            file.renameTo(file2);
        }
        myDb = SQLiteDatabase.openOrCreateDatabase(dbName, null);
    }

    public void processResults(Cursor cursor, String s)
    {
        String s1;
        JSONArray jsonarray;
        int i;
        s1 = "[]";
        if (!cursor.moveToFirst())
        {
            break MISSING_BLOCK_LABEL_99;
        }
        jsonarray = new JSONArray();
        i = cursor.getColumnCount();
_L4:
        JSONObject jsonobject;
        int j;
        jsonobject = new JSONObject();
        j = 0;
_L2:
        if (j >= i)
        {
            break; /* Loop/switch isn't completed */
        }
        jsonobject.put(cursor.getColumnName(j), cursor.getString(j));
        j++;
        if (true) goto _L2; else goto _L1
_L1:
        try
        {
            jsonarray.put(jsonobject);
        }
        catch (JSONException jsonexception)
        {
            jsonexception.printStackTrace();
        }
        if (cursor.moveToNext()) goto _L4; else goto _L3
_L3:
        s1 = jsonarray.toString();
        webView.sendJavascript((new StringBuilder()).append("cordova.require('cordova/plugin/android/storage').completeQuery('").append(s).append("', ").append(s1).append(");").toString());
        return;
    }
}
