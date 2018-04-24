// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package org.apache.cordova:
//            FileTransfer

private static class bytesRead extends bytesRead
{

    private long bytesRead;

    private int updateBytesRead(int i)
    {
        if (i != -1)
        {
            bytesRead = bytesRead + (long)i;
        }
        return i;
    }

    public long getTotalRawBytesRead()
    {
        return bytesRead;
    }

    public int read()
        throws IOException
    {
        return updateBytesRead(super.());
    }

    public int read(byte abyte0[])
        throws IOException
    {
        return updateBytesRead(super.(abyte0));
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        return updateBytesRead(super.(abyte0, i, j));
    }

    public (InputStream inputstream)
    {
        super(inputstream);
        bytesRead = 0L;
    }
}
