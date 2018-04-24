// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;

public class FileHelper
{

    private static final String LOG_TAG = "FileUtils";
    private static final String _DATA = "_data";

    public FileHelper()
    {
    }

    public static InputStream getInputStreamFromUriString(String s, CordovaInterface cordovainterface)
        throws IOException
    {
        if (s.startsWith("content"))
        {
            Uri uri = Uri.parse(s);
            return cordovainterface.getActivity().getContentResolver().openInputStream(uri);
        }
        if (s.startsWith("file://"))
        {
            int i = s.indexOf("?");
            if (i > -1)
            {
                s = s.substring(0, i);
            }
            if (s.startsWith("file:///android_asset/"))
            {
                String s1 = Uri.parse(s).getPath().substring(15);
                return cordovainterface.getActivity().getAssets().open(s1);
            } else
            {
                return new FileInputStream(getRealPath(s, cordovainterface));
            }
        } else
        {
            return new FileInputStream(getRealPath(s, cordovainterface));
        }
    }

    public static String getMimeType(String s, CordovaInterface cordovainterface)
    {
        Uri uri = Uri.parse(s);
        if (s.startsWith("content://"))
        {
            return cordovainterface.getActivity().getContentResolver().getType(uri);
        }
        String s1 = uri.getPath();
        int i = s1.lastIndexOf('.');
        if (i != -1)
        {
            s1 = s1.substring(i + 1);
        }
        String s2 = s1.toLowerCase();
        if (s2.equals("3ga"))
        {
            return "audio/3gpp";
        } else
        {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(s2);
        }
    }

    public static String getRealPath(Uri uri, CordovaInterface cordovainterface)
    {
        return getRealPath(uri.toString(), cordovainterface);
    }

    public static String getRealPath(String s, CordovaInterface cordovainterface)
    {
        String s1;
        if (s.startsWith("content://"))
        {
            String as[] = {
                "_data"
            };
            Cursor cursor = cordovainterface.getActivity().managedQuery(Uri.parse(s), as, null, null, null);
            int i = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            s1 = cursor.getString(i);
            if (s1 == null)
            {
                LOG.e("FileUtils", "Could get real path for URI string %s", new Object[] {
                    s
                });
            }
        } else
        if (s.startsWith("file://"))
        {
            s1 = s.substring(7);
            if (s1.startsWith("/android_asset/"))
            {
                LOG.e("FileUtils", "Cannot get real path for URI string %s because it is a file:///android_asset/ URI.", new Object[] {
                    s
                });
                return null;
            }
        } else
        {
            return s;
        }
        return s1;
    }

    public static String stripFileProtocol(String s)
    {
        if (s.startsWith("file://"))
        {
            s = s.substring(7);
        }
        return s;
    }
}
