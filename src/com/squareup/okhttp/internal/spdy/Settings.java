// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;


final class Settings
{

    static final int CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
    static final int COUNT = 9;
    static final int CURRENT_CWND = 5;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 0x10000;
    static final int DOWNLOAD_BANDWIDTH = 2;
    static final int DOWNLOAD_RETRANS_RATE = 6;
    static final int FLAG_CLEAR_PREVIOUSLY_PERSISTED_SETTINGS = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int PERSISTED = 2;
    static final int PERSIST_VALUE = 1;
    static final int ROUND_TRIP_TIME = 3;
    static final int UPLOAD_BANDWIDTH = 1;
    private int persistValue;
    private int persisted;
    private int set;
    private final int values[] = new int[9];

    Settings()
    {
    }

    int flags(int i)
    {
        boolean flag = isPersisted(i);
        int j = 0;
        if (flag)
        {
            j = 0 | 2;
        }
        if (persistValue(i))
        {
            j |= 1;
        }
        return j;
    }

    int get(int i)
    {
        return values[i];
    }

    int getClientCertificateVectorSize(int i)
    {
        if ((0x100 & set) != 0)
        {
            i = values[8];
        }
        return i;
    }

    int getCurrentCwnd(int i)
    {
        if ((0x20 & set) != 0)
        {
            i = values[5];
        }
        return i;
    }

    int getDownloadBandwidth(int i)
    {
        if ((4 & set) != 0)
        {
            i = values[2];
        }
        return i;
    }

    int getDownloadRetransRate(int i)
    {
        if ((0x40 & set) != 0)
        {
            i = values[6];
        }
        return i;
    }

    int getInitialWindowSize(int i)
    {
        if ((0x80 & set) != 0)
        {
            i = values[7];
        }
        return i;
    }

    int getMaxConcurrentStreams(int i)
    {
        if ((0x10 & set) != 0)
        {
            i = values[4];
        }
        return i;
    }

    int getRoundTripTime(int i)
    {
        if ((8 & set) != 0)
        {
            i = values[3];
        }
        return i;
    }

    int getUploadBandwidth(int i)
    {
        if ((2 & set) != 0)
        {
            i = values[1];
        }
        return i;
    }

    boolean isPersisted(int i)
    {
        return (1 << i & persisted) != 0;
    }

    boolean isSet(int i)
    {
        return (1 << i & set) != 0;
    }

    void merge(Settings settings)
    {
        int i = 0;
        while (i < 9) 
        {
            if (settings.isSet(i))
            {
                set(i, settings.flags(i), settings.get(i));
            }
            i++;
        }
    }

    boolean persistValue(int i)
    {
        return (1 << i & persistValue) != 0;
    }

    void set(int i, int j, int k)
    {
        if (i >= values.length)
        {
            return;
        }
        int l = 1 << i;
        set = l | set;
        if ((j & 1) != 0)
        {
            persistValue = l | persistValue;
        } else
        {
            persistValue = persistValue & ~l;
        }
        if ((j & 2) != 0)
        {
            persisted = l | persisted;
        } else
        {
            persisted = persisted & ~l;
        }
        values[i] = k;
    }

    int size()
    {
        return Integer.bitCount(set);
    }
}
