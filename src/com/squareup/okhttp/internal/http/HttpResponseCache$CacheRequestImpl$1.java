// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.squareup.okhttp.internal.http:
//            HttpResponseCache

class val.editor extends FilterOutputStream
{

    final out this$1;
    final com.squareup.okhttp.internal.pl val$editor;
    final HttpResponseCache val$this$0;

    public void close()
        throws IOException
    {
label0:
        {
            synchronized (_fld0)
            {
                if (!cess._mth300(this._cls1.this))
                {
                    break label0;
                }
            }
            return;
        }
        cess._mth302(this._cls1.this, true);
        HttpResponseCache.access$408(_fld0);
        httpresponsecache;
        JVM INSTR monitorexit ;
        super.close();
        val$editor.editor();
        return;
        exception;
        httpresponsecache;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        out.write(abyte0, i, j);
    }

    (com.squareup.okhttp.internal.pl pl)
    {
        this$1 = final_;
        val$this$0 = HttpResponseCache.this;
        val$editor = pl;
        super(final_outputstream);
    }
}
