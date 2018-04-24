// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            GPSListener, NetworkListener, CordovaLocationListener

public class GeoBroker extends CordovaPlugin
{

    private GPSListener gpsListener;
    private LocationManager locationManager;
    private NetworkListener networkListener;

    public GeoBroker()
    {
    }

    private void addWatch(String s, CallbackContext callbackcontext, boolean flag)
    {
        if (flag)
        {
            gpsListener.addWatch(s, callbackcontext);
            return;
        } else
        {
            networkListener.addWatch(s, callbackcontext);
            return;
        }
    }

    private void clearWatch(String s)
    {
        gpsListener.clearWatch(s);
        networkListener.clearWatch(s);
    }

    private void getCurrentLocation(CallbackContext callbackcontext, boolean flag, int i)
    {
        if (flag)
        {
            gpsListener.addCallback(callbackcontext, i);
            return;
        } else
        {
            networkListener.addCallback(callbackcontext, i);
            return;
        }
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager)cordova.getActivity().getSystemService("location");
            networkListener = new NetworkListener(locationManager, this);
            gpsListener = new GPSListener(locationManager, this);
        }
        if (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network"))
        {
            if (s.equals("getLocation"))
            {
                boolean flag = jsonarray.getBoolean(0);
                int i = jsonarray.getInt(1);
                LocationManager locationmanager = locationManager;
                String s1;
                Location location;
                if (flag)
                {
                    s1 = "gps";
                } else
                {
                    s1 = "network";
                }
                location = locationmanager.getLastKnownLocation(s1);
                if (location != null && System.currentTimeMillis() - location.getTime() <= (long)i)
                {
                    callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, returnLocationJSON(location)));
                } else
                {
                    getCurrentLocation(callbackcontext, flag, jsonarray.optInt(2, 60000));
                }
            } else
            if (s.equals("addWatch"))
            {
                addWatch(jsonarray.getString(0), callbackcontext, jsonarray.getBoolean(1));
            } else
            if (s.equals("clearWatch"))
            {
                clearWatch(jsonarray.getString(0));
            } else
            {
                return false;
            }
        } else
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.NO_RESULT, "Location API is not available for this device."));
        }
        return true;
    }

    public void fail(int i, String s, CallbackContext callbackcontext, boolean flag)
    {
        JSONObject jsonobject = new JSONObject();
        String s1 = null;
        PluginResult pluginresult;
        try
        {
            jsonobject.put("code", i);
            jsonobject.put("message", s);
        }
        catch (JSONException jsonexception)
        {
            s1 = (new StringBuilder()).append("{'code':").append(i).append(",'message':'").append(s.replaceAll("'", "'")).append("'}").toString();
            jsonobject = null;
        }
        if (jsonobject != null)
        {
            pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, jsonobject);
        } else
        {
            pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, s1);
        }
        pluginresult.setKeepCallback(flag);
        callbackcontext.sendPluginResult(pluginresult);
    }

    public boolean isGlobalListener(CordovaLocationListener cordovalocationlistener)
    {
        boolean flag;
label0:
        {
            GPSListener gpslistener = gpsListener;
            flag = false;
            if (gpslistener == null)
            {
                break label0;
            }
            NetworkListener networklistener = networkListener;
            flag = false;
            if (networklistener == null)
            {
                break label0;
            }
            if (!gpsListener.equals(cordovalocationlistener))
            {
                boolean flag1 = networkListener.equals(cordovalocationlistener);
                flag = false;
                if (!flag1)
                {
                    break label0;
                }
            }
            flag = true;
        }
        return flag;
    }

    public void onDestroy()
    {
        if (networkListener != null)
        {
            networkListener.destroy();
            networkListener = null;
        }
        if (gpsListener != null)
        {
            gpsListener.destroy();
            gpsListener = null;
        }
    }

    public void onReset()
    {
        onDestroy();
    }

    public JSONObject returnLocationJSON(Location location)
    {
        JSONObject jsonobject = new JSONObject();
        Double double1;
        jsonobject.put("latitude", location.getLatitude());
        jsonobject.put("longitude", location.getLongitude());
        if (!location.hasAltitude())
        {
            break MISSING_BLOCK_LABEL_141;
        }
        double1 = Double.valueOf(location.getAltitude());
_L1:
        boolean flag;
        jsonobject.put("altitude", double1);
        jsonobject.put("accuracy", location.getAccuracy());
        flag = location.hasBearing();
        Float float1 = null;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_104;
        }
        boolean flag1;
        try
        {
            flag1 = location.hasSpeed();
        }
        catch (JSONException jsonexception)
        {
            jsonexception.printStackTrace();
            return jsonobject;
        }
        float1 = null;
        if (!flag1)
        {
            break MISSING_BLOCK_LABEL_104;
        }
        float1 = Float.valueOf(location.getBearing());
        jsonobject.put("heading", float1);
        jsonobject.put("velocity", location.getSpeed());
        jsonobject.put("timestamp", location.getTime());
        return jsonobject;
        double1 = null;
          goto _L1
    }

    public void win(Location location, CallbackContext callbackcontext, boolean flag)
    {
        PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, returnLocationJSON(location));
        pluginresult.setKeepCallback(flag);
        callbackcontext.sendPluginResult(pluginresult);
    }
}
