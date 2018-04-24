// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpHandler
{

    public HttpHandler()
    {
    }

    private HttpEntity getHttpEntity(String s)
    {
        HttpEntity httpentity;
        try
        {
            httpentity = (new DefaultHttpClient()).execute(new HttpGet(s)).getEntity();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
        return httpentity;
    }

    private void writeToDisk(HttpEntity httpentity, String s)
        throws IllegalStateException, IOException
    {
        String s1 = (new StringBuilder()).append("/sdcard/").append(s).toString();
        InputStream inputstream = httpentity.getContent();
        byte abyte0[] = new byte[1024];
        FileOutputStream fileoutputstream = new FileOutputStream(s1);
        do
        {
            int i = inputstream.read(abyte0);
            if (i <= 0)
            {
                fileoutputstream.flush();
                fileoutputstream.close();
                return;
            }
            fileoutputstream.write(abyte0, 0, i);
        } while (true);
    }

    protected Boolean get(String s, String s1)
    {
        HttpEntity httpentity = getHttpEntity(s);
        try
        {
            writeToDisk(httpentity, s1);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return Boolean.valueOf(false);
        }
        try
        {
            httpentity.consumeContent();
        }
        catch (Exception exception1)
        {
            exception1.printStackTrace();
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }
}
