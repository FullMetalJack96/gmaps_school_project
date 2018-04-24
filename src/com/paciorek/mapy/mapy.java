// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.paciorek.mapy;

import android.os.Bundle;
import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;

public class mapy extends DroidGap
{

    public mapy()
    {
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        super.loadUrl(Config.getStartUrl());
    }
}
