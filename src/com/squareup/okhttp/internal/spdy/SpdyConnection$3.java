// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.NamedRunnable;
import java.io.IOException;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyConnection, Ping

class val.ping extends NamedRunnable
{

    final SpdyConnection this$0;
    final Ping val$ping;
    final int val$streamId;

    public void execute()
    {
        try
        {
            SpdyConnection.access$500(SpdyConnection.this, val$streamId, val$ping);
            return;
        }
        catch (IOException ioexception)
        {
            return;
        }
    }

    (Ping ping1)
    {
        this$0 = final_spdyconnection;
        val$streamId = I.this;
        val$ping = ping1;
        super(final_s);
    }
}
