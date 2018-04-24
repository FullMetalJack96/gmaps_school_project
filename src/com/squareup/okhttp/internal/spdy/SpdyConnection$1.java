// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.NamedRunnable;
import java.io.IOException;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyConnection

class val.statusCode extends NamedRunnable
{

    final SpdyConnection this$0;
    final int val$statusCode;
    final int val$streamId;

    public void execute()
    {
        try
        {
            writeSynReset(val$streamId, val$statusCode);
            return;
        }
        catch (IOException ioexception)
        {
            return;
        }
    }

    (int j)
    {
        this$0 = final_spdyconnection;
        val$streamId = I.this;
        val$statusCode = j;
        super(final_s);
    }
}
