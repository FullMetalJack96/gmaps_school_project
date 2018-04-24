// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyReader, Settings

public static interface 
{

    public abstract void data(int i, int j, InputStream inputstream, int k)
        throws IOException;

    public abstract void goAway(int i, int j, int k);

    public abstract void headers(int i, int j, List list)
        throws IOException;

    public abstract void noop();

    public abstract void ping(int i, int j);

    public abstract void rstStream(int i, int j, int k);

    public abstract void settings(int i, Settings settings1);

    public abstract void synReply(int i, int j, List list)
        throws IOException;

    public abstract void synStream(int i, int j, int k, int l, int i1, List list);

    public abstract void windowUpdate(int i, int j, int k);
}
