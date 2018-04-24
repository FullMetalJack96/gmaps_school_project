// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal;

import java.io.UnsupportedEncodingException;

// Referenced classes of package com.squareup.okhttp.internal:
//            Util

public final class Base64
{

    private static final byte MAP[] = {
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 43, 47
    };

    private Base64()
    {
    }

    public static byte[] decode(byte abyte0[])
    {
        return decode(abyte0, abyte0.length);
    }

    public static byte[] decode(byte abyte0[], int i)
    {
        byte abyte1[];
        int k;
        int j = 3 * (i / 4);
        if (j == 0)
        {
            return Util.EMPTY_BYTE_ARRAY;
        }
        abyte1 = new byte[j];
        k = 0;
_L2:
        byte byte0;
        byte0 = abyte0[i - 1];
        if (byte0 != 10 && byte0 != 13 && byte0 != 32 && byte0 != 9)
        {
            break; /* Loop/switch isn't completed */
        }
_L3:
        i--;
        if (true) goto _L2; else goto _L1
_L1:
        if (byte0 == 61)
        {
            k++;
        } else
        {
label0:
            {
                int l = 0;
                int i1 = 0;
                int j1 = 0;
                int k1 = 0;
                while (j1 < i) 
                {
                    byte byte1 = abyte0[j1];
                    int j2;
                    if (byte1 != 10 && byte1 != 13 && byte1 != 32)
                    {
                        if (byte1 == 9)
                        {
                            j2 = k1;
                        } else
                        {
                            int k2;
                            if (byte1 >= 65 && byte1 <= 90)
                            {
                                k2 = byte1 - 65;
                            } else
                            if (byte1 >= 97 && byte1 <= 122)
                            {
                                k2 = byte1 - 71;
                            } else
                            if (byte1 >= 48 && byte1 <= 57)
                            {
                                k2 = byte1 + 4;
                            } else
                            if (byte1 == 43)
                            {
                                k2 = 62;
                            } else
                            if (byte1 == 47)
                            {
                                k2 = 63;
                            } else
                            {
                                return null;
                            }
                            i1 = i1 << 6 | (byte)k2;
                            int l1;
                            byte abyte2[];
                            int i2;
                            if (l % 4 == 3)
                            {
                                int l2 = k1 + 1;
                                abyte1[k1] = (byte)(i1 >> 16);
                                int i3 = l2 + 1;
                                abyte1[l2] = (byte)(i1 >> 8);
                                j2 = i3 + 1;
                                abyte1[i3] = (byte)i1;
                            } else
                            {
                                j2 = k1;
                            }
                            l++;
                        }
                    } else
                    {
                        j2 = k1;
                    }
                    j1++;
                    k1 = j2;
                }
                if (k > 0)
                {
                    i2 = i1 << k * 6;
                    l1 = k1 + 1;
                    abyte1[k1] = (byte)(i2 >> 16);
                    if (k != 1)
                    {
                        break label0;
                    }
                    k1 = l1 + 1;
                    abyte1[l1] = (byte)(i2 >> 8);
                }
                l1 = k1;
            }
            abyte2 = new byte[l1];
            System.arraycopy(abyte1, 0, abyte2, 0, l1);
            return abyte2;
        }
          goto _L3
        if (true) goto _L2; else goto _L4
_L4:
    }

    public static String encode(byte abyte0[])
    {
        byte abyte1[];
        int i;
        int k;
        abyte1 = new byte[(4 * (2 + abyte0.length)) / 3];
        i = abyte0.length - abyte0.length % 3;
        int j = 0;
        k = 0;
        for (; j < i; j += 3)
        {
            int l2 = k + 1;
            abyte1[k] = MAP[(0xff & abyte0[j]) >> 2];
            int i3 = l2 + 1;
            abyte1[l2] = MAP[(3 & abyte0[j]) << 4 | (0xff & abyte0[j + 1]) >> 4];
            int j3 = i3 + 1;
            abyte1[i3] = MAP[(0xf & abyte0[j + 1]) << 2 | (0xff & abyte0[j + 2]) >> 6];
            k = j3 + 1;
            abyte1[j3] = MAP[0x3f & abyte0[j + 2]];
        }

        abyte0.length % 3;
        JVM INSTR tableswitch 1 2: default 176
    //                   1 198
    //                   2 273;
           goto _L1 _L2 _L3
_L1:
        int k1 = k;
_L4:
        int l;
        int i1;
        int j1;
        String s;
        int l1;
        int i2;
        int j2;
        int k2;
        try
        {
            s = new String(abyte1, 0, k1, "US-ASCII");
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new AssertionError(unsupportedencodingexception);
        }
        return s;
_L2:
        l1 = k + 1;
        abyte1[k] = MAP[(0xff & abyte0[i]) >> 2];
        i2 = l1 + 1;
        abyte1[l1] = MAP[(3 & abyte0[i]) << 4];
        j2 = i2 + 1;
        abyte1[i2] = 61;
        k2 = j2 + 1;
        abyte1[j2] = 61;
        k1 = k2;
          goto _L4
_L3:
        l = k + 1;
        abyte1[k] = MAP[(0xff & abyte0[i]) >> 2];
        i1 = l + 1;
        abyte1[l] = MAP[(3 & abyte0[i]) << 4 | (0xff & abyte0[i + 1]) >> 4];
        j1 = i1 + 1;
        abyte1[i1] = MAP[(0xf & abyte0[i + 1]) << 2];
        k = j1 + 1;
        abyte1[j1] = 61;
        if (true) goto _L1; else goto _L5
_L5:
    }

}
