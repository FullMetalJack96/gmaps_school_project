// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Dialog;

// Referenced classes of package org.apache.cordova:
//            InAppBrowser

class this._cls0
    implements Runnable
{

    final InAppBrowser this$0;

    public void run()
    {
        InAppBrowser.access$000(InAppBrowser.this).show();
    }

    ()
    {
        this$0 = InAppBrowser.this;
        super();
    }
}
