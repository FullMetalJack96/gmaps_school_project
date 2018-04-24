// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;


// Referenced classes of package org.apache.cordova:
//            AudioPlayer

public static final class  extends Enum
{

    private static final .VALUES $VALUES[];
    public static final .VALUES NONE;
    public static final .VALUES PLAY;
    public static final .VALUES RECORD;

    public static  valueOf(String s)
    {
        return ()Enum.valueOf(org/apache/cordova/AudioPlayer$MODE, s);
    }

    public static [] values()
    {
        return ([])$VALUES.clone();
    }

    static 
    {
        NONE = new <init>("NONE", 0);
        PLAY = new <init>("PLAY", 1);
        RECORD = new <init>("RECORD", 2);
        E_3B_.clone aclone[] = new <init>[3];
        aclone[0] = NONE;
        aclone[1] = PLAY;
        aclone[2] = RECORD;
        $VALUES = aclone;
    }

    private (String s, int i)
    {
        super(s, i);
    }
}
