// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.content.Context;
import android.widget.LinearLayout;
import org.apache.cordova.api.LOG;

// Referenced classes of package org.apache.cordova:
//            CordovaActivity, CordovaWebView

public class LinearLayoutSoftKeyboardDetect extends LinearLayout
{

    private static final String TAG = "SoftKeyboardDetect";
    private CordovaActivity app;
    private int oldHeight;
    private int oldWidth;
    private int screenHeight;
    private int screenWidth;

    public LinearLayoutSoftKeyboardDetect(Context context, int i, int j)
    {
        super(context);
        oldHeight = 0;
        oldWidth = 0;
        screenWidth = 0;
        screenHeight = 0;
        app = null;
        screenWidth = i;
        screenHeight = j;
        app = (CordovaActivity)context;
    }

    protected void onMeasure(int i, int j)
    {
        int k;
        int l;
        super.onMeasure(i, j);
        LOG.v("SoftKeyboardDetect", "We are in our onMeasure method");
        k = android.view.View.MeasureSpec.getSize(j);
        l = android.view.View.MeasureSpec.getSize(i);
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(oldHeight);
        LOG.v("SoftKeyboardDetect", "Old Height = %d", aobj);
        Object aobj1[] = new Object[1];
        aobj1[0] = Integer.valueOf(k);
        LOG.v("SoftKeyboardDetect", "Height = %d", aobj1);
        Object aobj2[] = new Object[1];
        aobj2[0] = Integer.valueOf(oldWidth);
        LOG.v("SoftKeyboardDetect", "Old Width = %d", aobj2);
        Object aobj3[] = new Object[1];
        aobj3[0] = Integer.valueOf(l);
        LOG.v("SoftKeyboardDetect", "Width = %d", aobj3);
        if (oldHeight != 0 && oldHeight != k) goto _L2; else goto _L1
_L1:
        LOG.d("SoftKeyboardDetect", "Ignore this event");
_L4:
        oldHeight = k;
        oldWidth = l;
        return;
_L2:
        if (screenHeight == l)
        {
            int i1 = screenHeight;
            screenHeight = screenWidth;
            screenWidth = i1;
            LOG.v("SoftKeyboardDetect", "Orientation Change");
        } else
        if (k > oldHeight)
        {
            if (app != null)
            {
                app.appView.sendJavascript("cordova.fireDocumentEvent('hidekeyboard');");
            }
        } else
        if (k < oldHeight && app != null)
        {
            app.appView.sendJavascript("cordova.fireDocumentEvent('showkeyboard');");
        }
        if (true) goto _L4; else goto _L3
_L3:
    }
}
