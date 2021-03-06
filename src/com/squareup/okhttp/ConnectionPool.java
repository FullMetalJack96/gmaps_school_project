// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.squareup.okhttp;

import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.squareup.okhttp:
//            Connection, Route, Address

public class ConnectionPool
{

    private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 0x493e0L;
    private static final int MAX_CONNECTIONS_TO_CLEANUP = 2;
    private static final ConnectionPool systemDefault;
    private final LinkedList connections = new LinkedList();
    private final Callable connectionsCleanupCallable = new Callable() {

        final ConnectionPool this$0;

        public volatile Object call()
            throws Exception
        {
            return call();
        }

        public Void call()
            throws Exception
        {
            ArrayList arraylist;
            int j;
            arraylist = new ArrayList(2);
            j = 0;
            ConnectionPool connectionpool = ConnectionPool.this;
            connectionpool;
            JVM INSTR monitorenter ;
            ListIterator listiterator = connections.listIterator(connections.size());
_L5:
            if (!listiterator.hasPrevious()) goto _L2; else goto _L1
_L1:
            Connection connection1 = (Connection)listiterator.previous();
            if (connection1.isAlive() && !connection1.isExpired(keepAliveDurationNs)) goto _L4; else goto _L3
_L3:
            listiterator.remove();
            arraylist.add(connection1);
            if (arraylist.size() != 2) goto _L5; else goto _L2
_L2:
            ListIterator listiterator1 = connections.listIterator(connections.size());
_L6:
            Connection connection;
            do
            {
                if (!listiterator1.hasPrevious() || j <= maxIdleConnections)
                {
                    break MISSING_BLOCK_LABEL_210;
                }
                connection = (Connection)listiterator1.previous();
            } while (!connection.isIdle());
            arraylist.add(connection);
            listiterator1.remove();
            j--;
              goto _L6
_L4:
            if (!connection1.isIdle()) goto _L5; else goto _L7
_L7:
            j++;
              goto _L5
            connectionpool;
            JVM INSTR monitorexit ;
            for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); Util.closeQuietly((Connection)iterator.next())) { }
            break MISSING_BLOCK_LABEL_253;
            Exception exception;
            exception;
            connectionpool;
            JVM INSTR monitorexit ;
            throw exception;
            return null;
        }

            
            {
                this$0 = ConnectionPool.this;
                super();
            }
    };
    private final ExecutorService executorService;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;

    public ConnectionPool(int i, long l)
    {
        executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        maxIdleConnections = i;
        keepAliveDurationNs = 1000L * (l * 1000L);
    }

    public static ConnectionPool getDefault()
    {
        return systemDefault;
    }

    private void waitForCleanupCallableToRun()
    {
        try
        {
            executorService.submit(new Runnable() {

                final ConnectionPool this$0;

                public void run()
                {
                }

            
            {
                this$0 = ConnectionPool.this;
                super();
            }
            }).get();
            return;
        }
        catch (Exception exception)
        {
            throw new AssertionError();
        }
    }

    public void evictAll()
    {
        this;
        JVM INSTR monitorenter ;
        ArrayList arraylist;
        arraylist = new ArrayList(connections);
        connections.clear();
        this;
        JVM INSTR monitorexit ;
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); Util.closeQuietly((Connection)iterator.next())) { }
        break MISSING_BLOCK_LABEL_59;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public Connection get(Address address)
    {
        this;
        JVM INSTR monitorenter ;
        ListIterator listiterator = connections.listIterator(connections.size());
_L2:
        boolean flag = listiterator.hasPrevious();
        Connection connection;
        connection = null;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_117;
        }
        Connection connection1 = (Connection)listiterator.previous();
        if (!connection1.getRoute().getAddress().equals(address) || !connection1.isAlive() || System.nanoTime() - connection1.getIdleStartTimeNs() >= keepAliveDurationNs) goto _L2; else goto _L1
_L1:
        boolean flag1;
        listiterator.remove();
        flag1 = connection1.isSpdy();
        if (flag1)
        {
            break MISSING_BLOCK_LABEL_113;
        }
        Platform.get().tagSocket(connection1.getSocket());
        connection = connection1;
        if (connection == null)
        {
            break MISSING_BLOCK_LABEL_139;
        }
        if (connection.isSpdy())
        {
            connections.addFirst(connection);
        }
        executorService.submit(connectionsCleanupCallable);
        this;
        JVM INSTR monitorexit ;
        return connection;
        SocketException socketexception;
        socketexception;
        Util.closeQuietly(connection1);
        Platform.get().logW((new StringBuilder()).append("Unable to tagSocket(): ").append(socketexception).toString());
          goto _L2
        Exception exception;
        exception;
        throw exception;
    }

    public int getConnectionCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = connections.size();
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    List getConnections()
    {
        waitForCleanupCallableToRun();
        this;
        JVM INSTR monitorenter ;
        ArrayList arraylist = new ArrayList(connections);
        this;
        JVM INSTR monitorexit ;
        return arraylist;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public int getHttpConnectionCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = 0;
        Iterator iterator = connections.iterator();
_L1:
        boolean flag;
        if (!iterator.hasNext())
        {
            break MISSING_BLOCK_LABEL_46;
        }
        flag = ((Connection)iterator.next()).isSpdy();
        if (!flag)
        {
            i++;
        }
          goto _L1
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public int getSpdyConnectionCount()
    {
        this;
        JVM INSTR monitorenter ;
        int i = 0;
        Iterator iterator = connections.iterator();
_L1:
        boolean flag;
        if (!iterator.hasNext())
        {
            break MISSING_BLOCK_LABEL_46;
        }
        flag = ((Connection)iterator.next()).isSpdy();
        if (flag)
        {
            i++;
        }
          goto _L1
        this;
        JVM INSTR monitorexit ;
        return i;
        Exception exception;
        exception;
        throw exception;
    }

    public void maybeShare(Connection connection)
    {
        executorService.submit(connectionsCleanupCallable);
        while (!connection.isSpdy() || !connection.isAlive()) 
        {
            return;
        }
        this;
        JVM INSTR monitorenter ;
        connections.addFirst(connection);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void recycle(Connection connection)
    {
        executorService.submit(connectionsCleanupCallable);
        if (connection.isSpdy())
        {
            return;
        }
        if (!connection.isAlive())
        {
            Util.closeQuietly(connection);
            return;
        }
        Exception exception;
        try
        {
            Platform.get().untagSocket(connection.getSocket());
        }
        catch (SocketException socketexception)
        {
            Platform.get().logW((new StringBuilder()).append("Unable to untagSocket(): ").append(socketexception).toString());
            Util.closeQuietly(connection);
            return;
        }
        this;
        JVM INSTR monitorenter ;
        connections.addFirst(connection);
        connection.resetIdleStartTime();
        this;
        JVM INSTR monitorexit ;
        return;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    static 
    {
        String s = System.getProperty("http.keepAlive");
        String s1 = System.getProperty("http.keepAliveDuration");
        String s2 = System.getProperty("http.maxConnections");
        long l;
        if (s1 != null)
        {
            l = Long.parseLong(s1);
        } else
        {
            l = 0x493e0L;
        }
        if (s != null && !Boolean.parseBoolean(s))
        {
            systemDefault = new ConnectionPool(0, l);
        } else
        if (s2 != null)
        {
            systemDefault = new ConnectionPool(Integer.parseInt(s2), l);
        } else
        {
            systemDefault = new ConnectionPool(5, l);
        }
    }



}
