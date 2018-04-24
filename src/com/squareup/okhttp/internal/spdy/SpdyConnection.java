// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.Util;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.squareup.okhttp.internal.spdy:
//            SpdyReader, SpdyWriter, SpdyStream, Ping, 
//            IncomingStreamHandler, Settings

public final class SpdyConnection
    implements Closeable
{
    public static class Builder
    {

        public boolean client;
        private IncomingStreamHandler handler;
        private String hostName;
        private InputStream in;
        private OutputStream out;

        public SpdyConnection build()
        {
            return new SpdyConnection(this);
        }

        public Builder handler(IncomingStreamHandler incomingstreamhandler)
        {
            handler = incomingstreamhandler;
            return this;
        }





        public Builder(String s, boolean flag, InputStream inputstream, OutputStream outputstream)
        {
            handler = IncomingStreamHandler.REFUSE_INCOMING_STREAMS;
            hostName = s;
            client = flag;
            in = inputstream;
            out = outputstream;
        }

        public Builder(String s, boolean flag, Socket socket)
            throws IOException
        {
            this(s, flag, socket.getInputStream(), socket.getOutputStream());
        }

        public Builder(boolean flag, InputStream inputstream, OutputStream outputstream)
        {
            this("", flag, inputstream, outputstream);
        }

        public Builder(boolean flag, Socket socket)
            throws IOException
        {
            this("", flag, socket.getInputStream(), socket.getOutputStream());
        }
    }

    private class Reader
        implements Runnable, SpdyReader.Handler
    {

        final SpdyConnection this$0;

        public void data(int i, int j, InputStream inputstream, int k)
            throws IOException
        {
            SpdyStream spdystream = getStream(j);
            if (spdystream == null)
            {
                writeSynResetLater(j, 2);
                Util.skipByReading(inputstream, k);
            } else
            {
                spdystream.receiveData(inputstream, k);
                if ((i & 1) != 0)
                {
                    spdystream.receiveFin();
                    return;
                }
            }
        }

        public void goAway(int i, int j, int k)
        {
            SpdyConnection spdyconnection = SpdyConnection.this;
            spdyconnection;
            JVM INSTR monitorenter ;
            shutdown = true;
            Iterator iterator = streams.entrySet().iterator();
            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                if (((Integer)entry.getKey()).intValue() > j && ((SpdyStream)entry.getValue()).isLocallyInitiated())
                {
                    ((SpdyStream)entry.getValue()).receiveRstStream(3);
                    iterator.remove();
                }
            } while (true);
            break MISSING_BLOCK_LABEL_124;
            Exception exception;
            exception;
            spdyconnection;
            JVM INSTR monitorexit ;
            throw exception;
            spdyconnection;
            JVM INSTR monitorexit ;
        }

        public void headers(int i, int j, List list)
            throws IOException
        {
            SpdyStream spdystream = getStream(j);
            if (spdystream != null)
            {
                spdystream.receiveHeaders(list);
            }
        }

        public void noop()
        {
        }

        public void ping(int i, int j)
        {
            int k = 1;
            int l = client;
            if (j % 2 != k)
            {
                k = 0;
            }
            if (l != k)
            {
                writePingLater(j, null);
            } else
            {
                Ping ping1 = removePing(j);
                if (ping1 != null)
                {
                    ping1.receive();
                    return;
                }
            }
        }

        public void rstStream(int i, int j, int k)
        {
            SpdyStream spdystream = removeStream(j);
            if (spdystream != null)
            {
                spdystream.receiveRstStream(k);
            }
        }

        public void run()
        {
            boolean flag;
            do
            {
                flag = spdyReader.nextFrame(this);
            } while (flag);
            IOException ioexception1;
            try
            {
                close(0, 5);
                return;
            }
            catch (IOException ioexception2)
            {
                return;
            }
            ioexception1;
            close(1, 1);
            return;
            Exception exception;
            exception;
            try
            {
                close(2, 6);
            }
            catch (IOException ioexception) { }
            throw exception;
        }

        public void settings(int i, Settings settings1)
        {
            SpdyConnection spdyconnection = SpdyConnection.this;
            spdyconnection;
            JVM INSTR monitorenter ;
            if (SpdyConnection.this.settings != null && (i & 1) == 0) goto _L2; else goto _L1
_L1:
            SpdyConnection.this.settings = settings1;
_L6:
            boolean flag = streams.isEmpty();
            SpdyStream aspdystream[];
            aspdystream = null;
            if (flag)
            {
                break MISSING_BLOCK_LABEL_90;
            }
            aspdystream = (SpdyStream[])streams.values().toArray(new SpdyStream[streams.size()]);
            spdyconnection;
            JVM INSTR monitorexit ;
            if (aspdystream == null) goto _L4; else goto _L3
_L3:
            SpdyStream aspdystream1[];
            int j;
            int k;
            aspdystream1 = aspdystream;
            j = aspdystream1.length;
            k = 0;
_L5:
            if (k >= j)
            {
                break; /* Loop/switch isn't completed */
            }
            SpdyStream spdystream = aspdystream1[k];
            spdystream;
            JVM INSTR monitorenter ;
            this;
            JVM INSTR monitorenter ;
            spdystream.receiveSettings(SpdyConnection.this.settings);
            this;
            JVM INSTR monitorexit ;
            spdystream;
            JVM INSTR monitorexit ;
            k++;
            if (true) goto _L5; else goto _L4
_L2:
            SpdyConnection.this.settings.merge(settings1);
              goto _L6
            Exception exception;
            exception;
            spdyconnection;
            JVM INSTR monitorexit ;
            throw exception;
            Exception exception2;
            exception2;
            this;
            JVM INSTR monitorexit ;
            throw exception2;
            Exception exception1;
            exception1;
            spdystream;
            JVM INSTR monitorexit ;
            throw exception1;
_L4:
        }

        public void synReply(int i, int j, List list)
            throws IOException
        {
            SpdyStream spdystream = getStream(j);
            if (spdystream == null)
            {
                writeSynResetLater(j, 2);
            } else
            {
                spdystream.receiveReply(list);
                if ((i & 1) != 0)
                {
                    spdystream.receiveFin();
                    return;
                }
            }
        }

        public void synStream(int i, int j, int k, int l, int i1, List list)
        {
            SpdyStream spdystream;
label0:
            {
                synchronized (SpdyConnection.this)
                {
                    spdystream = new SpdyStream(j, SpdyConnection.this, i, l, i1, list, SpdyConnection.this.settings);
                    if (!shutdown)
                    {
                        break label0;
                    }
                }
                return;
            }
            SpdyStream spdystream1;
            lastGoodStreamId = j;
            spdystream1 = (SpdyStream)streams.put(Integer.valueOf(j), spdystream);
            spdyconnection;
            JVM INSTR monitorexit ;
            if (spdystream1 != null)
            {
                spdystream1.closeLater(1);
                removeStream(j);
                return;
            } else
            {
                ExecutorService executorservice = SpdyConnection.executor;
                Object aobj[] = new Object[2];
                aobj[0] = hostName;
                aobj[1] = Integer.valueOf(j);
                executorservice.submit(String.format("Callback %s stream %d", aobj). new NamedRunnable(spdystream) {

                    final Reader this$1;
                    final SpdyStream val$synStream;

                    public void execute()
                    {
                        try
                        {
                            handler.receive(synStream);
                            return;
                        }
                        catch (IOException ioexception)
                        {
                            throw new RuntimeException(ioexception);
                        }
                    }

            
            {
                this$1 = final_reader;
                synStream = spdystream;
                super(String.this);
            }
                });
                return;
            }
            exception;
            spdyconnection;
            JVM INSTR monitorexit ;
            throw exception;
        }

        public void windowUpdate(int i, int j, int k)
        {
            SpdyStream spdystream = getStream(j);
            if (spdystream != null)
            {
                spdystream.receiveWindowUpdate(k);
            }
        }

        private Reader()
        {
            this$0 = SpdyConnection.this;
            super();
        }

    }


    static final boolean $assertionsDisabled = false;
    static final int FLAG_FIN = 1;
    static final int FLAG_UNIDIRECTIONAL = 2;
    static final int GOAWAY_INTERNAL_ERROR = 2;
    static final int GOAWAY_OK = 0;
    static final int GOAWAY_PROTOCOL_ERROR = 1;
    static final int TYPE_CREDENTIAL = 16;
    static final int TYPE_DATA = 0;
    static final int TYPE_GOAWAY = 7;
    static final int TYPE_HEADERS = 8;
    static final int TYPE_NOOP = 5;
    static final int TYPE_PING = 6;
    static final int TYPE_RST_STREAM = 3;
    static final int TYPE_SETTINGS = 4;
    static final int TYPE_SYN_REPLY = 2;
    static final int TYPE_SYN_STREAM = 1;
    static final int TYPE_WINDOW_UPDATE = 9;
    static final int VERSION = 3;
    private static final ExecutorService executor;
    final boolean client;
    private final IncomingStreamHandler handler;
    private final String hostName;
    private long idleStartTimeNs;
    private int lastGoodStreamId;
    private int nextPingId;
    private int nextStreamId;
    private Map pings;
    Settings settings;
    private boolean shutdown;
    private final SpdyReader spdyReader;
    private final SpdyWriter spdyWriter;
    private final Map streams;

    private SpdyConnection(Builder builder)
    {
        int i = 1;
        super();
        streams = new HashMap();
        idleStartTimeNs = System.nanoTime();
        client = builder.client;
        handler = builder.handler;
        spdyReader = new SpdyReader(builder.in);
        spdyWriter = new SpdyWriter(builder.out);
        int j;
        if (builder.client)
        {
            j = i;
        } else
        {
            j = 2;
        }
        nextStreamId = j;
        if (!builder.client)
        {
            i = 2;
        }
        nextPingId = i;
        hostName = builder.hostName;
        (new Thread(new Reader(), (new StringBuilder()).append("Spdy Reader ").append(hostName).toString())).start();
    }


    private void close(int i, int j)
        throws IOException
    {
        IOException ioexception;
        Ping aping[];
        if (!$assertionsDisabled && Thread.holdsLock(this))
        {
            throw new AssertionError();
        }
        ioexception = null;
        boolean flag;
        SpdyStream aspdystream[];
        Map map;
        SpdyStream aspdystream1[];
        int i1;
        int j1;
        SpdyStream spdystream;
        try
        {
            shutdown(i);
        }
        catch (IOException ioexception1)
        {
            ioexception = ioexception1;
        }
        this;
        JVM INSTR monitorenter ;
        flag = streams.isEmpty();
        aspdystream = null;
        if (flag)
        {
            break MISSING_BLOCK_LABEL_94;
        }
        aspdystream = (SpdyStream[])streams.values().toArray(new SpdyStream[streams.size()]);
        streams.clear();
        setIdle(false);
        map = pings;
        aping = null;
        if (map == null)
        {
            break MISSING_BLOCK_LABEL_144;
        }
        aping = (Ping[])pings.values().toArray(new Ping[pings.size()]);
        pings = null;
        this;
        JVM INSTR monitorexit ;
        if (aspdystream != null)
        {
            aspdystream1 = aspdystream;
            i1 = aspdystream1.length;
            j1 = 0;
            do
            {
                if (j1 >= i1)
                {
                    break;
                }
                spdystream = aspdystream1[j1];
                Exception exception;
                try
                {
                    spdystream.close(j);
                }
                catch (IOException ioexception4)
                {
                    if (ioexception != null)
                    {
                        ioexception = ioexception4;
                    }
                }
                j1++;
            } while (true);
        }
        break MISSING_BLOCK_LABEL_216;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        if (aping != null)
        {
            Ping aping1[] = aping;
            int k = aping1.length;
            for (int l = 0; l < k; l++)
            {
                aping1[l].cancel();
            }

        }
        try
        {
            spdyReader.close();
        }
        catch (IOException ioexception2)
        {
            ioexception = ioexception2;
        }
        try
        {
            spdyWriter.close();
        }
        catch (IOException ioexception3)
        {
            if (ioexception == null)
            {
                ioexception = ioexception3;
            }
        }
        if (ioexception != null)
        {
            throw ioexception;
        } else
        {
            return;
        }
    }

    private SpdyStream getStream(int i)
    {
        this;
        JVM INSTR monitorenter ;
        SpdyStream spdystream = (SpdyStream)streams.get(Integer.valueOf(i));
        this;
        JVM INSTR monitorexit ;
        return spdystream;
        Exception exception;
        exception;
        throw exception;
    }

    private Ping removePing(int i)
    {
        this;
        JVM INSTR monitorenter ;
        if (pings == null) goto _L2; else goto _L1
_L1:
        Ping ping1 = (Ping)pings.remove(Integer.valueOf(i));
_L4:
        this;
        JVM INSTR monitorexit ;
        return ping1;
_L2:
        ping1 = null;
        if (true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }

    private void setIdle(boolean flag)
    {
        this;
        JVM INSTR monitorenter ;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_18;
        }
        long l = System.nanoTime();
_L1:
        idleStartTimeNs = l;
        this;
        JVM INSTR monitorexit ;
        return;
        l = 0L;
          goto _L1
        Exception exception;
        exception;
        throw exception;
    }

    private void writePing(int i, Ping ping1)
        throws IOException
    {
        SpdyWriter spdywriter = spdyWriter;
        spdywriter;
        JVM INSTR monitorenter ;
        if (ping1 == null)
        {
            break MISSING_BLOCK_LABEL_15;
        }
        ping1.send();
        spdyWriter.ping(0, i);
        spdywriter;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        spdywriter;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private void writePingLater(int i, Ping ping1)
    {
        ExecutorService executorservice = executor;
        Object aobj[] = new Object[2];
        aobj[0] = hostName;
        aobj[1] = Integer.valueOf(i);
        executorservice.submit(new NamedRunnable(ping1) {

            final SpdyConnection this$0;
            final Ping val$ping;
            final int val$streamId;

            public void execute()
            {
                try
                {
                    writePing(streamId, ping);
                    return;
                }
                catch (IOException ioexception)
                {
                    return;
                }
            }

            
            {
                this$0 = SpdyConnection.this;
                streamId = i;
                ping = ping1;
                super(final_s);
            }
        });
    }

    public void close()
        throws IOException
    {
        close(0, 5);
    }

    public void flush()
        throws IOException
    {
        synchronized (spdyWriter)
        {
            spdyWriter.out.flush();
        }
        return;
        exception;
        spdywriter;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public long getIdleStartTimeNs()
    {
        this;
        JVM INSTR monitorenter ;
        long l = idleStartTimeNs;
        this;
        JVM INSTR monitorexit ;
        return l;
        Exception exception;
        exception;
        throw exception;
    }

    public boolean isIdle()
    {
        this;
        JVM INSTR monitorenter ;
        long l = idleStartTimeNs;
        boolean flag;
        if (l != 0L)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        this;
        JVM INSTR monitorexit ;
        return flag;
        Exception exception;
        exception;
        throw exception;
    }

    public SpdyStream newStream(List list, boolean flag, boolean flag1)
        throws IOException
    {
        int i;
        SpdyWriter spdywriter;
        boolean flag2;
        byte byte0;
        Exception exception;
        Exception exception1;
        if (flag)
        {
            flag2 = false;
        } else
        {
            flag2 = true;
        }
        if (flag1)
        {
            byte0 = 0;
        } else
        {
            byte0 = 2;
        }
        i = flag2 | byte0;
        spdywriter = spdyWriter;
        spdywriter;
        JVM INSTR monitorenter ;
        this;
        JVM INSTR monitorenter ;
        if (shutdown)
        {
            throw new IOException("shutdown");
        }
        break MISSING_BLOCK_LABEL_77;
        exception1;
        this;
        JVM INSTR monitorexit ;
        throw exception1;
        exception;
        spdywriter;
        JVM INSTR monitorexit ;
        throw exception;
        int j;
        SpdyStream spdystream;
        j = nextStreamId;
        nextStreamId = 2 + nextStreamId;
        spdystream = new SpdyStream(j, this, i, 0, 0, list, settings);
        if (spdystream.isOpen())
        {
            streams.put(Integer.valueOf(j), spdystream);
            setIdle(false);
        }
        this;
        JVM INSTR monitorexit ;
        spdyWriter.synStream(i, j, 0, 0, 0, list);
        spdywriter;
        JVM INSTR monitorexit ;
        return spdystream;
    }

    public void noop()
        throws IOException
    {
        spdyWriter.noop();
    }

    public int openStreamCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = streams.size();
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public Ping ping()
        throws IOException
    {
        Ping ping1 = new Ping();
        this;
        JVM INSTR monitorenter ;
        if (shutdown)
        {
            throw new IOException("shutdown");
        }
        break MISSING_BLOCK_LABEL_33;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        int i;
        i = nextPingId;
        nextPingId = 2 + nextPingId;
        if (pings == null)
        {
            pings = new HashMap();
        }
        pings.put(Integer.valueOf(i), ping1);
        this;
        JVM INSTR monitorexit ;
        writePing(i, ping1);
        return ping1;
    }

    SpdyStream removeStream(int i)
    {
        this;
        JVM INSTR monitorenter ;
        SpdyStream spdystream = (SpdyStream)streams.remove(Integer.valueOf(i));
        if (spdystream == null)
        {
            break MISSING_BLOCK_LABEL_40;
        }
        if (streams.isEmpty())
        {
            setIdle(true);
        }
        this;
        JVM INSTR monitorexit ;
        return spdystream;
        Exception exception;
        exception;
        throw exception;
    }

    public void shutdown(int i)
        throws IOException
    {
        SpdyWriter spdywriter = spdyWriter;
        spdywriter;
        JVM INSTR monitorenter ;
        this;
        JVM INSTR monitorenter ;
        if (!shutdown)
        {
            break MISSING_BLOCK_LABEL_21;
        }
        this;
        JVM INSTR monitorexit ;
        spdywriter;
        JVM INSTR monitorexit ;
        return;
        int j;
        shutdown = true;
        j = lastGoodStreamId;
        this;
        JVM INSTR monitorexit ;
        spdyWriter.goAway(0, j, i);
        spdywriter;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        spdywriter;
        JVM INSTR monitorexit ;
        throw exception;
        Exception exception1;
        exception1;
        this;
        JVM INSTR monitorexit ;
        throw exception1;
    }

    void writeFrame(byte abyte0[], int i, int j)
        throws IOException
    {
        synchronized (spdyWriter)
        {
            spdyWriter.out.write(abyte0, i, j);
        }
        return;
        exception;
        spdywriter;
        JVM INSTR monitorexit ;
        throw exception;
    }

    void writeSynReply(int i, int j, List list)
        throws IOException
    {
        spdyWriter.synReply(j, i, list);
    }

    void writeSynReset(int i, int j)
        throws IOException
    {
        spdyWriter.rstStream(i, j);
    }

    void writeSynResetLater(int i, int j)
    {
        ExecutorService executorservice = executor;
        Object aobj[] = new Object[2];
        aobj[0] = hostName;
        aobj[1] = Integer.valueOf(i);
        executorservice.submit(new NamedRunnable(j) {

            final SpdyConnection this$0;
            final int val$statusCode;
            final int val$streamId;

            public void execute()
            {
                try
                {
                    writeSynReset(streamId, statusCode);
                    return;
                }
                catch (IOException ioexception)
                {
                    return;
                }
            }

            
            {
                this$0 = SpdyConnection.this;
                streamId = i;
                statusCode = j;
                super(final_s);
            }
        });
    }

    void writeWindowUpdate(int i, int j)
        throws IOException
    {
        spdyWriter.windowUpdate(i, j);
    }

    void writeWindowUpdateLater(int i, int j)
    {
        ExecutorService executorservice = executor;
        Object aobj[] = new Object[2];
        aobj[0] = hostName;
        aobj[1] = Integer.valueOf(i);
        executorservice.submit(new NamedRunnable(j) {

            final SpdyConnection this$0;
            final int val$deltaWindowSize;
            final int val$streamId;

            public void execute()
            {
                try
                {
                    writeWindowUpdate(streamId, deltaWindowSize);
                    return;
                }
                catch (IOException ioexception)
                {
                    return;
                }
            }

            
            {
                this$0 = SpdyConnection.this;
                streamId = i;
                deltaWindowSize = j;
                super(final_s);
            }
        });
    }

    static 
    {
        boolean flag;
        if (!com/squareup/okhttp/internal/spdy/SpdyConnection.desiredAssertionStatus())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        $assertionsDisabled = flag;
        executor = new ThreadPoolExecutor(0, 0x7fffffff, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Executors.defaultThreadFactory());
    }



/*
    static boolean access$1002(SpdyConnection spdyconnection, boolean flag)
    {
        spdyconnection.shutdown = flag;
        return flag;
    }

*/


/*
    static int access$1102(SpdyConnection spdyconnection, int i)
    {
        spdyconnection.lastGoodStreamId = i;
        return i;
    }

*/










}
