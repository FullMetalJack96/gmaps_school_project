// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyReader, Settings

final class SpdyWriter
    implements Closeable
{

    private final ByteArrayOutputStream nameValueBlockBuffer = new ByteArrayOutputStream();
    private final DataOutputStream nameValueBlockOut;
    final DataOutputStream out;

    SpdyWriter(OutputStream outputstream)
    {
        out = new DataOutputStream(outputstream);
        Deflater deflater = new Deflater();
        deflater.setDictionary(SpdyReader.DICTIONARY);
        nameValueBlockOut = new DataOutputStream(Platform.get().newDeflaterOutputStream(nameValueBlockBuffer, deflater, true));
    }

    private void writeNameValueBlockToBuffer(List list)
        throws IOException
    {
        nameValueBlockBuffer.reset();
        int i = list.size() / 2;
        nameValueBlockOut.writeInt(i);
        String s;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); nameValueBlockOut.write(s.getBytes("UTF-8")))
        {
            s = (String)iterator.next();
            nameValueBlockOut.writeInt(s.length());
        }

        nameValueBlockOut.flush();
    }

    public void close()
        throws IOException
    {
        Util.closeAll(out, nameValueBlockOut);
    }

    public void data(int i, int j, byte abyte0[])
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        int k = abyte0.length;
        out.writeInt(0x7fffffff & j);
        out.writeInt((i & 0xff) << 24 | 0xffffff & k);
        out.write(abyte0);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void goAway(int i, int j, int k)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        out.writeInt(0x80030007);
        out.writeInt(8 | (i & 0xff) << 24);
        out.writeInt(j);
        out.writeInt(k);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void headers(int i, int j, List list)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        writeNameValueBlockToBuffer(list);
        int k = 4 + nameValueBlockBuffer.size();
        out.writeInt(0x80030008);
        out.writeInt((i & 0xff) << 24 | 0xffffff & k);
        out.writeInt(0x7fffffff & j);
        nameValueBlockBuffer.writeTo(out);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void noop()
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        out.writeInt(0x80030005);
        out.writeInt(0);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void ping(int i, int j)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        out.writeInt(0x80030006);
        out.writeInt(4 | (i & 0xff) << 24);
        out.writeInt(j);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void rstStream(int i, int j)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        out.writeInt(0x80030003);
        out.writeInt(8);
        out.writeInt(0x7fffffff & i);
        out.writeInt(j);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void settings(int i, Settings settings1)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        int j = settings1.size();
        int k = 4 + j * 8;
        out.writeInt(0x80030004);
        out.writeInt((i & 0xff) << 24 | k & 0xffffff);
        out.writeInt(j);
        int l = 0;
_L2:
        if (l > 9)
        {
            break MISSING_BLOCK_LABEL_129;
        }
        if (!settings1.isSet(l))
        {
            break MISSING_BLOCK_LABEL_139;
        }
        int i1 = settings1.flags(l);
        out.writeInt((i1 & 0xff) << 24 | l & 0xffffff);
        out.writeInt(settings1.get(l));
        break MISSING_BLOCK_LABEL_139;
        Exception exception;
        exception;
        throw exception;
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        l++;
        if (true) goto _L2; else goto _L1
_L1:
    }

    public void synReply(int i, int j, List list)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        writeNameValueBlockToBuffer(list);
        int k = 4 + nameValueBlockBuffer.size();
        out.writeInt(0x80030002);
        out.writeInt((i & 0xff) << 24 | 0xffffff & k);
        out.writeInt(0x7fffffff & j);
        nameValueBlockBuffer.writeTo(out);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void synStream(int i, int j, int k, int l, int i1, List list)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        writeNameValueBlockToBuffer(list);
        int j1 = 10 + nameValueBlockBuffer.size();
        out.writeInt(0x80030001);
        out.writeInt((i & 0xff) << 24 | 0xffffff & j1);
        out.writeInt(j & 0x7fffffff);
        out.writeInt(k & 0x7fffffff);
        out.writeShort(0 | (l & 7) << 13 | i1 & 0xff);
        nameValueBlockBuffer.writeTo(out);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    public void windowUpdate(int i, int j)
        throws IOException
    {
        this;
        JVM INSTR monitorenter ;
        out.writeInt(0x80030009);
        out.writeInt(8);
        out.writeInt(i);
        out.writeInt(j);
        out.flush();
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }
}
