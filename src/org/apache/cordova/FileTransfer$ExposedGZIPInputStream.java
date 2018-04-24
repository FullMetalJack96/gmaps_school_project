// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

// Referenced classes of package org.apache.cordova:
//            FileTransfer

private static class  extends GZIPInputStream
{

    public Inflater getInflater()
    {
        return inf;
    }

    public (InputStream inputstream)
        throws IOException
    {
        super(inputstream);
    }
}
