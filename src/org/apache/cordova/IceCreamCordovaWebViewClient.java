// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginManager;

// Referenced classes of package org.apache.cordova:
//            CordovaWebViewClient, FileHelper, Config, CordovaWebView

public class IceCreamCordovaWebViewClient extends CordovaWebViewClient
{

    public IceCreamCordovaWebViewClient(CordovaInterface cordovainterface)
    {
        super(cordovainterface);
    }

    public IceCreamCordovaWebViewClient(CordovaInterface cordovainterface, CordovaWebView cordovawebview)
    {
        super(cordovainterface, cordovawebview);
    }

    private WebResourceResponse generateWebResourceResponse(String s)
    {
        String s1;
        if (!s.startsWith("file:///android_asset/"))
        {
            break MISSING_BLOCK_LABEL_53;
        }
        s1 = FileHelper.getMimeType(s, cordova);
        WebResourceResponse webresourceresponse = new WebResourceResponse(s1, "UTF-8", FileHelper.getInputStreamFromUriString(s, cordova));
        return webresourceresponse;
        IOException ioexception;
        ioexception;
        LOG.e("generateWebResourceResponse", ioexception.getMessage(), ioexception);
        return null;
    }

    private WebResourceResponse getWhitelistResponse()
    {
        return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
    }

    private static boolean needsIceCreamSpecialsInAssetUrlFix(String s)
    {
        if (!s.contains("%20"))
        {
            return false;
        }
        switch (android.os.Build.VERSION.SDK_INT)
        {
        default:
            return false;

        case 14: // '\016'
        case 15: // '\017'
            return true;
        }
    }

    public WebResourceResponse shouldInterceptRequest(WebView webview, String s)
    {
        WebResourceResponse webresourceresponse = super.shouldInterceptRequest(webview, s);
        if (!Config.isUrlWhiteListed(s) && (s.startsWith("http://") || s.startsWith("https://")))
        {
            webresourceresponse = getWhitelistResponse();
        } else
        {
            if (webresourceresponse == null && (s.contains("?") || s.contains("#") || needsIceCreamSpecialsInAssetUrlFix(s)))
            {
                return generateWebResourceResponse(s);
            }
            if (webresourceresponse == null && appView.pluginManager != null)
            {
                return appView.pluginManager.shouldInterceptRequest(s);
            }
        }
        return webresourceresponse;
    }
}
