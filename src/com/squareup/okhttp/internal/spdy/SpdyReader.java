// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.Util;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            Settings

final class SpdyReader
    implements Closeable
{
    public static interface Handler
    {

        public abstract void data(int i, int j, InputStream inputstream, int k)
            throws IOException;

        public abstract void goAway(int i, int j, int k);

        public abstract void headers(int i, int j, List list)
            throws IOException;

        public abstract void noop();

        public abstract void ping(int i, int j);

        public abstract void rstStream(int i, int j, int k);

        public abstract void settings(int i, Settings settings1);

        public abstract void synReply(int i, int j, List list)
            throws IOException;

        public abstract void synStream(int i, int j, int k, int l, int i1, List list);

        public abstract void windowUpdate(int i, int j, int k);
    }


    static final byte DICTIONARY[];
    private int compressedLimit;
    private final DataInputStream in;
    private final DataInputStream nameValueBlockIn = newNameValueBlockStream();

    SpdyReader(InputStream inputstream)
    {
        in = new DataInputStream(inputstream);
    }

    private static transient IOException ioException(String s, Object aobj[])
        throws IOException
    {
        throw new IOException(String.format(s, aobj));
    }

    private DataInputStream newNameValueBlockStream()
    {
        return new DataInputStream(new InflaterInputStream(new InputStream() , new Inflater() {

            final SpdyReader this$0;

            public int inflate(byte abyte0[], int i, int j)
                throws DataFormatException
            {
                int k = inflate(abyte0, i, j);
                if (k == 0 && needsDictionary())
                {
                    setDictionary(SpdyReader.DICTIONARY);
                    k = inflate(abyte0, i, j);
                }
                return k;
            }

            
            {
                this$0 = SpdyReader.this;
                super();
            }
        }));
    }

    private void readGoAway(Handler handler, int i, int j)
        throws IOException
    {
        if (j != 8)
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(j);
            throw ioException("TYPE_GOAWAY length: %d != 8", aobj);
        } else
        {
            handler.goAway(i, 0x7fffffff & in.readInt(), in.readInt());
            return;
        }
    }

    private void readHeaders(Handler handler, int i, int j)
        throws IOException
    {
        handler.headers(i, 0x7fffffff & in.readInt(), readNameValueBlock(j - 4));
    }

    private List readNameValueBlock(int i)
        throws IOException
    {
        int j;
        compressedLimit = i + compressedLimit;
        try
        {
            j = nameValueBlockIn.readInt();
        }
        catch (DataFormatException dataformatexception)
        {
            throw new IOException(dataformatexception.getMessage());
        }
        if (j >= 0)
        {
            break MISSING_BLOCK_LABEL_77;
        }
        Logger.getLogger(getClass().getName()).warning((new StringBuilder()).append("numberOfPairs < 0: ").append(j).toString());
        throw ioException("numberOfPairs < 0", new Object[0]);
        ArrayList arraylist = new ArrayList(j * 2);
        int k = 0;
_L2:
        if (k >= j)
        {
            break; /* Loop/switch isn't completed */
        }
        String s = readString();
        String s1 = readString();
        if (s.length() == 0)
        {
            throw ioException("name.length == 0", new Object[0]);
        }
        if (s1.length() == 0)
        {
            throw ioException("values.length == 0", new Object[0]);
        }
        arraylist.add(s);
        arraylist.add(s1);
        k++;
        if (true) goto _L2; else goto _L1
_L1:
        if (compressedLimit != 0)
        {
            Logger.getLogger(getClass().getName()).warning((new StringBuilder()).append("compressedLimit > 0: ").append(compressedLimit).toString());
        }
        return arraylist;
    }

    private void readPing(Handler handler, int i, int j)
        throws IOException
    {
        if (j != 4)
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(j);
            throw ioException("TYPE_PING length: %d != 4", aobj);
        } else
        {
            handler.ping(i, in.readInt());
            return;
        }
    }

    private void readRstStream(Handler handler, int i, int j)
        throws IOException
    {
        if (j != 8)
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(j);
            throw ioException("TYPE_RST_STREAM length: %d != 8", aobj);
        } else
        {
            handler.rstStream(i, 0x7fffffff & in.readInt(), in.readInt());
            return;
        }
    }

    private void readSettings(Handler handler, int i, int j)
        throws IOException
    {
        int k = in.readInt();
        if (j != 4 + k * 8)
        {
            Object aobj[] = new Object[2];
            aobj[0] = Integer.valueOf(j);
            aobj[1] = Integer.valueOf(k);
            throw ioException("TYPE_SETTINGS length: %d != 4 + 8 * %d", aobj);
        }
        Settings settings = new Settings();
        for (int l = 0; l < k; l++)
        {
            int i1 = in.readInt();
            int j1 = in.readInt();
            int k1 = (0xff000000 & i1) >>> 24;
            settings.set(i1 & 0xffffff, k1, j1);
        }

        handler.settings(i, settings);
    }

    private String readString()
        throws DataFormatException, IOException
    {
        int i = nameValueBlockIn.readInt();
        byte abyte0[] = new byte[i];
        Util.readFully(nameValueBlockIn, abyte0);
        return new String(abyte0, 0, i, "UTF-8");
    }

    private void readSynReply(Handler handler, int i, int j)
        throws IOException
    {
        handler.synReply(i, 0x7fffffff & in.readInt(), readNameValueBlock(j - 4));
    }

    private void readSynStream(Handler handler, int i, int j)
        throws IOException
    {
        int k = in.readInt();
        int l = in.readInt();
        short word0 = in.readShort();
        handler.synStream(i, k & 0x7fffffff, l & 0x7fffffff, (0xe000 & word0) >>> 13, word0 & 0xff, readNameValueBlock(j - 10));
    }

    private void readWindowUpdate(Handler handler, int i, int j)
        throws IOException
    {
        if (j != 8)
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(j);
            throw ioException("TYPE_WINDOW_UPDATE length: %d != 8", aobj);
        } else
        {
            int k = in.readInt();
            int l = in.readInt();
            handler.windowUpdate(i, k & 0x7fffffff, l & 0x7fffffff);
            return;
        }
    }

    public void close()
        throws IOException
    {
        Util.closeAll(in, nameValueBlockIn);
    }

    public boolean nextFrame(Handler handler)
        throws IOException
    {
        int i;
        int j;
        boolean flag;
        int k;
        int l;
        try
        {
            i = in.readInt();
        }
        catch (IOException ioexception)
        {
            return false;
        }
        j = in.readInt();
        if ((0x80000000 & i) != 0)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        k = (0xff000000 & j) >>> 24;
        l = j & 0xffffff;
        if (flag)
        {
            int i1 = (0x7fff0000 & i) >>> 16;
            int j1 = i & 0xffff;
            if (i1 != 3)
            {
                throw new ProtocolException((new StringBuilder()).append("version != 3: ").append(i1).toString());
            }
            switch (j1)
            {
            case 10: // '\n'
            case 11: // '\013'
            case 12: // '\f'
            case 13: // '\r'
            case 14: // '\016'
            case 15: // '\017'
            default:
                throw new IOException("Unexpected frame");

            case 1: // '\001'
                readSynStream(handler, k, l);
                return true;

            case 2: // '\002'
                readSynReply(handler, k, l);
                return true;

            case 3: // '\003'
                readRstStream(handler, k, l);
                return true;

            case 4: // '\004'
                readSettings(handler, k, l);
                return true;

            case 5: // '\005'
                if (l != 0)
                {
                    Object aobj[] = new Object[1];
                    aobj[0] = Integer.valueOf(l);
                    throw ioException("TYPE_NOOP length: %d != 0", aobj);
                } else
                {
                    handler.noop();
                    return true;
                }

            case 6: // '\006'
                readPing(handler, k, l);
                return true;

            case 7: // '\007'
                readGoAway(handler, k, l);
                return true;

            case 8: // '\b'
                readHeaders(handler, k, l);
                return true;

            case 9: // '\t'
                readWindowUpdate(handler, k, l);
                return true;

            case 16: // '\020'
                Util.skipByReading(in, l);
                throw new UnsupportedOperationException("TODO");
            }
        } else
        {
            handler.data(k, i & 0x7fffffff, in, l);
            return true;
        }
    }

    static 
    {
        try
        {
            DICTIONARY = "\000\000\000\007options\000\000\000\004head\000\000\000\004post\000\000\000\003put\000\000\000\006delete\000\000\000\005trace\000\000\000\006accept\000\000\000\016accept-charset\000\000\000\017accept-encoding\000\000\000\017accept-language\000\000\000\raccept-ranges\000\000\000\003age\000\000\000\005allow\000\000\000\rauthorization\000\000\000\rcache-control\000\000\000\nconnection\000\000\000\fcontent-base\000\000\000\020content-encoding\000\000\000\020content-language\000\000\000\016content-length\000\000\000\020content-location\000\000\000\013content-md5\000\000\000\rcontent-range\000\000\000\fcontent-type\000\000\000\004date\000\000\000\004etag\000\000\000\006expect\000\000\000\007expires\000\000\000\004from\000\000\000\004host\000\000\000\bif-match\000\000\000\021if-modified-since\000\000\000\rif-none-match\000\000\000\bif-range\000\000\000\023if-unmodified-since\000\000\000\rlast-modified\000\000\000\blocation\000\000\000\fmax-forwards\000\000\000\006pragma\000\000\000\022proxy-authenticate\000\000\000\023proxy-authorization\000\000\000\005range\000\000\000\007referer\000\000\000\013retry-after\000\000\000\006server\000\000\000\002te\000\000\000\007trailer\000\000\000\021transfer-encoding\000\000\000\007upgrade\000\000\000\nuser-agent\000\000\000\004vary\000\000\000\003via\000\000\000\007warning\000\000\000\020www-authenticate\000\000\000\006method\000\000\000\003get\000\000\000\006status\000\000\000\006200 OK\000\000\000\007version\000\000\000\bHTTP/1.1\000\000\000\003url\000\000\000\006public\000\000\000\nset-cookie\000\000\000\nkeep-alive\000\000\000\006origin100101201202205206300302303304305306307402405406407408409410411412413414415416417502504505203 Non-Authoritative Information204 No Content301 Moved Permanently400 Bad Request401 Unauthorized403 Forbidden404 Not Found500 Internal Server Error501 Not Implemented503 Service UnavailableJan Feb Mar Apr May Jun Jul Aug Sept Oct Nov Dec 00:00:00 Mon, Tue, Wed, Thu, Fri, Sat, Sun, GMTchunked,text/html,image/png,image/jpg,image/gif,application/xml,application/xhtml+xml,text/plain,text/javascript,publicprivatemax-age=gzip,deflate,sdchcharset=utf-8charset=iso-8859-1,utf-,*,enq=0.".getBytes(Util.UTF_8.name());
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new AssertionError();
        }
    }



/*
    static int access$020(SpdyReader spdyreader, int i)
    {
        int j = spdyreader.compressedLimit - i;
        spdyreader.compressedLimit = j;
        return j;
    }

*/

}
