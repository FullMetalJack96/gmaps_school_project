// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;


// Referenced classes of package org.apache.cordova:
//            AudioPlayer

public static final class  extends Enum
{

    private static final .VALUES $VALUES[];
    public static final .VALUES MEDIA_LOADING;
    public static final .VALUES MEDIA_NONE;
    public static final .VALUES MEDIA_PAUSED;
    public static final .VALUES MEDIA_RUNNING;
    public static final .VALUES MEDIA_STARTING;
    public static final .VALUES MEDIA_STOPPED;

    public static  valueOf(String s)
    {
        return ()Enum.valueOf(org/apache/cordova/AudioPlayer$STATE, s);
    }

    public static [] values()
    {
        return ([])$VALUES.clone();
    }

    static 
    {
        MEDIA_NONE = new <init>("MEDIA_NONE", 0);
        MEDIA_STARTING = new <init>("MEDIA_STARTING", 1);
        MEDIA_RUNNING = new <init>("MEDIA_RUNNING", 2);
        MEDIA_PAUSED = new <init>("MEDIA_PAUSED", 3);
        MEDIA_STOPPED = new <init>("MEDIA_STOPPED", 4);
        MEDIA_LOADING = new <init>("MEDIA_LOADING", 5);
        E_3B_.clone aclone[] = new <init>[6];
        aclone[0] = MEDIA_NONE;
        aclone[1] = MEDIA_STARTING;
        aclone[2] = MEDIA_RUNNING;
        aclone[3] = MEDIA_PAUSED;
        aclone[4] = MEDIA_STOPPED;
        aclone[5] = MEDIA_LOADING;
        $VALUES = aclone;
    }

    private (String s, int i)
    {
        super(s, i);
    }
}
