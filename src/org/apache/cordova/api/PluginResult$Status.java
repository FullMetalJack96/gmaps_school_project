// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova.api;


// Referenced classes of package org.apache.cordova.api:
//            PluginResult

public static final class  extends Enum
{

    private static final .VALUES $VALUES[];
    public static final .VALUES CLASS_NOT_FOUND_EXCEPTION;
    public static final .VALUES ERROR;
    public static final .VALUES ILLEGAL_ACCESS_EXCEPTION;
    public static final .VALUES INSTANTIATION_EXCEPTION;
    public static final .VALUES INVALID_ACTION;
    public static final .VALUES IO_EXCEPTION;
    public static final .VALUES JSON_EXCEPTION;
    public static final .VALUES MALFORMED_URL_EXCEPTION;
    public static final .VALUES NO_RESULT;
    public static final .VALUES OK;

    public static  valueOf(String s)
    {
        return ()Enum.valueOf(org/apache/cordova/api/PluginResult$Status, s);
    }

    public static [] values()
    {
        return ([])$VALUES.clone();
    }

    static 
    {
        NO_RESULT = new <init>("NO_RESULT", 0);
        OK = new <init>("OK", 1);
        CLASS_NOT_FOUND_EXCEPTION = new <init>("CLASS_NOT_FOUND_EXCEPTION", 2);
        ILLEGAL_ACCESS_EXCEPTION = new <init>("ILLEGAL_ACCESS_EXCEPTION", 3);
        INSTANTIATION_EXCEPTION = new <init>("INSTANTIATION_EXCEPTION", 4);
        MALFORMED_URL_EXCEPTION = new <init>("MALFORMED_URL_EXCEPTION", 5);
        IO_EXCEPTION = new <init>("IO_EXCEPTION", 6);
        INVALID_ACTION = new <init>("INVALID_ACTION", 7);
        JSON_EXCEPTION = new <init>("JSON_EXCEPTION", 8);
        ERROR = new <init>("ERROR", 9);
        s_3B_.clone aclone[] = new <init>[10];
        aclone[0] = NO_RESULT;
        aclone[1] = OK;
        aclone[2] = CLASS_NOT_FOUND_EXCEPTION;
        aclone[3] = ILLEGAL_ACCESS_EXCEPTION;
        aclone[4] = INSTANTIATION_EXCEPTION;
        aclone[5] = MALFORMED_URL_EXCEPTION;
        aclone[6] = IO_EXCEPTION;
        aclone[7] = INVALID_ACTION;
        aclone[8] = JSON_EXCEPTION;
        aclone[9] = ERROR;
        $VALUES = aclone;
    }

    private (String s, int i)
    {
        super(s, i);
    }
}
