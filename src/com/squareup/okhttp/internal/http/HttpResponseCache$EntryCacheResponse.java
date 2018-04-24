// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import java.io.InputStream;
import java.net.CacheResponse;
import java.util.Map;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpResponseCache, RawHeaders

static class in extends CacheResponse
{

    private final entry entry;
    private final InputStream in;
    private final com.squareup.okhttp.internal.onse.entry snapshot;

    public InputStream getBody()
    {
        return in;
    }

    public Map getHeaders()
    {
        return in(entry).toMultimap(true);
    }


    public ( , com.squareup.okhttp.internal.onse onse)
    {
        entry = ;
        snapshot = onse;
        in = HttpResponseCache.access$600(onse);
    }
}
