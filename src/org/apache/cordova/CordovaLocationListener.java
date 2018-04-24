// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.cordova.api.CallbackContext;

// Referenced classes of package org.apache.cordova:
//            GeoBroker

public class CordovaLocationListener
    implements LocationListener
{
    private class LocationTimeoutTask extends TimerTask
    {

        private CallbackContext callbackContext;
        private CordovaLocationListener listener;
        final CordovaLocationListener this$0;

        public void run()
        {
            Iterator iterator = listener.callbacks.iterator();
            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }
                CallbackContext callbackcontext = (CallbackContext)iterator.next();
                if (callbackContext != callbackcontext)
                {
                    continue;
                }
                listener.callbacks.remove(callbackcontext);
                break;
            } while (true);
            if (listener.size() == 0)
            {
                listener.stop();
            }
        }

        public LocationTimeoutTask(CallbackContext callbackcontext, CordovaLocationListener cordovalocationlistener1)
        {
            this$0 = CordovaLocationListener.this;
            super();
            callbackContext = null;
            listener = null;
            callbackContext = callbackcontext;
            listener = cordovalocationlistener1;
        }
    }


    public static int PERMISSION_DENIED = 1;
    public static int POSITION_UNAVAILABLE = 2;
    public static int TIMEOUT = 3;
    private String TAG;
    private List callbacks;
    protected LocationManager locationManager;
    private GeoBroker owner;
    protected boolean running;
    private Timer timer;
    public HashMap watches;

    public CordovaLocationListener(LocationManager locationmanager, GeoBroker geobroker, String s)
    {
        running = false;
        watches = new HashMap();
        callbacks = new ArrayList();
        timer = null;
        TAG = "[Cordova Location Listener]";
        locationManager = locationmanager;
        owner = geobroker;
        TAG = s;
    }

    private void cancelTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void stop()
    {
        cancelTimer();
        if (running)
        {
            locationManager.removeUpdates(this);
            running = false;
        }
    }

    private void win(Location location)
    {
        cancelTimer();
        CallbackContext callbackcontext;
        for (Iterator iterator = callbacks.iterator(); iterator.hasNext(); owner.win(location, callbackcontext, false))
        {
            callbackcontext = (CallbackContext)iterator.next();
        }

        if (owner.isGlobalListener(this) && watches.size() == 0)
        {
            Log.d(TAG, "Stopping global listener");
            stop();
        }
        callbacks.clear();
        for (Iterator iterator1 = watches.values().iterator(); iterator1.hasNext(); owner.win(location, (CallbackContext)iterator1.next(), true)) { }
    }

    public void addCallback(CallbackContext callbackcontext, int i)
    {
        if (timer == null)
        {
            timer = new Timer();
        }
        timer.schedule(new LocationTimeoutTask(callbackcontext, this), i);
        callbacks.add(callbackcontext);
        if (size() == 1)
        {
            start();
        }
    }

    public void addWatch(String s, CallbackContext callbackcontext)
    {
        watches.put(s, callbackcontext);
        if (size() == 1)
        {
            start();
        }
    }

    public void clearWatch(String s)
    {
        if (watches.containsKey(s))
        {
            watches.remove(s);
        }
        if (size() == 0)
        {
            stop();
        }
    }

    public void destroy()
    {
        stop();
    }

    protected void fail(int i, String s)
    {
        cancelTimer();
        CallbackContext callbackcontext;
        for (Iterator iterator = callbacks.iterator(); iterator.hasNext(); owner.fail(i, s, callbackcontext, false))
        {
            callbackcontext = (CallbackContext)iterator.next();
        }

        if (owner.isGlobalListener(this) && watches.size() == 0)
        {
            Log.d(TAG, "Stopping global listener");
            stop();
        }
        callbacks.clear();
        for (Iterator iterator1 = watches.values().iterator(); iterator1.hasNext(); owner.fail(i, s, (CallbackContext)iterator1.next(), true)) { }
    }

    public void onLocationChanged(Location location)
    {
        Log.d(TAG, "The location has been updated!");
        win(location);
    }

    public void onProviderDisabled(String s)
    {
        Log.d(TAG, (new StringBuilder()).append("Location provider '").append(s).append("' disabled.").toString());
        fail(POSITION_UNAVAILABLE, "GPS provider disabled.");
    }

    public void onProviderEnabled(String s)
    {
        Log.d(TAG, (new StringBuilder()).append("Location provider ").append(s).append(" has been enabled").toString());
    }

    public void onStatusChanged(String s, int i, Bundle bundle)
    {
        Log.d(TAG, (new StringBuilder()).append("The status of the provider ").append(s).append(" has changed").toString());
        if (i == 0)
        {
            Log.d(TAG, (new StringBuilder()).append(s).append(" is OUT OF SERVICE").toString());
            fail(POSITION_UNAVAILABLE, (new StringBuilder()).append("Provider ").append(s).append(" is out of service.").toString());
            return;
        }
        if (i == 1)
        {
            Log.d(TAG, (new StringBuilder()).append(s).append(" is TEMPORARILY_UNAVAILABLE").toString());
            return;
        } else
        {
            Log.d(TAG, (new StringBuilder()).append(s).append(" is AVAILABLE").toString());
            return;
        }
    }

    public int size()
    {
        return watches.size() + callbacks.size();
    }

    protected void start()
    {
label0:
        {
            if (!running)
            {
                if (locationManager.getProvider("network") == null)
                {
                    break label0;
                }
                running = true;
                locationManager.requestLocationUpdates("network", 60000L, 10F, this);
            }
            return;
        }
        fail(POSITION_UNAVAILABLE, "Network provider is not available.");
    }



}
