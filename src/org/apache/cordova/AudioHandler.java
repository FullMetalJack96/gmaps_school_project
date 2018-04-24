// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.media.AudioManager;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            AudioPlayer, FileHelper

public class AudioHandler extends CordovaPlugin
{

    public static String TAG = "AudioHandler";
    ArrayList pausedForPhone;
    HashMap players;

    public AudioHandler()
    {
        players = new HashMap();
        pausedForPhone = new ArrayList();
    }

    private boolean release(String s)
    {
        if (!players.containsKey(s))
        {
            return false;
        } else
        {
            AudioPlayer audioplayer = (AudioPlayer)players.get(s);
            players.remove(s);
            audioplayer.destroy();
            return true;
        }
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        org.apache.cordova.api.PluginResult.Status status = org.apache.cordova.api.PluginResult.Status.OK;
        if (!s.equals("startRecordingAudio")) goto _L2; else goto _L1
_L1:
        startRecordingAudio(jsonarray.getString(0), FileHelper.stripFileProtocol(jsonarray.getString(1)));
_L4:
        callbackcontext.sendPluginResult(new PluginResult(status, ""));
        return true;
_L2:
        if (s.equals("stopRecordingAudio"))
        {
            stopRecordingAudio(jsonarray.getString(0));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("startPlayingAudio"))
        {
            startPlayingAudio(jsonarray.getString(0), FileHelper.stripFileProtocol(jsonarray.getString(1)));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("seekToAudio"))
        {
            seekToAudio(jsonarray.getString(0), jsonarray.getInt(1));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("pausePlayingAudio"))
        {
            pausePlayingAudio(jsonarray.getString(0));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("stopPlayingAudio"))
        {
            stopPlayingAudio(jsonarray.getString(0));
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("setVolume"))
        {
            try
            {
                setVolume(jsonarray.getString(0), Float.parseFloat(jsonarray.getString(1)));
            }
            catch (NumberFormatException numberformatexception) { }
            continue; /* Loop/switch isn't completed */
        }
        if (s.equals("getCurrentPositionAudio"))
        {
            callbackcontext.sendPluginResult(new PluginResult(status, getCurrentPositionAudio(jsonarray.getString(0))));
            return true;
        }
        if (s.equals("getDurationAudio"))
        {
            callbackcontext.sendPluginResult(new PluginResult(status, getDurationAudio(jsonarray.getString(0), jsonarray.getString(1))));
            return true;
        }
        if (!s.equals("create"))
        {
            break; /* Loop/switch isn't completed */
        }
        String s1 = jsonarray.getString(0);
        AudioPlayer audioplayer = new AudioPlayer(this, s1, FileHelper.stripFileProtocol(jsonarray.getString(1)));
        players.put(s1, audioplayer);
        if (true) goto _L4; else goto _L3
_L3:
        if (s.equals("release"))
        {
            callbackcontext.sendPluginResult(new PluginResult(status, release(jsonarray.getString(0))));
            return true;
        } else
        {
            return false;
        }
    }

    public int getAudioOutputDevice()
    {
        AudioManager audiomanager = (AudioManager)cordova.getActivity().getSystemService("audio");
        if (audiomanager.getRouting(0) == 1)
        {
            return 1;
        }
        return audiomanager.getRouting(0) != 2 ? -1 : 2;
    }

    public float getCurrentPositionAudio(String s)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            return (float)audioplayer.getCurrentPosition() / 1000F;
        } else
        {
            return -1F;
        }
    }

    public float getDurationAudio(String s, String s1)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            return audioplayer.getDuration(s1);
        } else
        {
            AudioPlayer audioplayer1 = new AudioPlayer(this, s, s1);
            players.put(s, audioplayer1);
            return audioplayer1.getDuration(s1);
        }
    }

    public void onDestroy()
    {
        for (Iterator iterator = players.values().iterator(); iterator.hasNext(); ((AudioPlayer)iterator.next()).destroy()) { }
        players.clear();
    }

    public Object onMessage(String s, Object obj)
    {
        if (s.equals("telephone"))
        {
            if ("ringing".equals(obj) || "offhook".equals(obj))
            {
                Iterator iterator = players.values().iterator();
                do
                {
                    if (!iterator.hasNext())
                    {
                        break;
                    }
                    AudioPlayer audioplayer = (AudioPlayer)iterator.next();
                    if (audioplayer.getState() == AudioPlayer.STATE.MEDIA_RUNNING.ordinal())
                    {
                        pausedForPhone.add(audioplayer);
                        audioplayer.pausePlaying();
                    }
                } while (true);
            } else
            if ("idle".equals(obj))
            {
                for (Iterator iterator1 = pausedForPhone.iterator(); iterator1.hasNext(); ((AudioPlayer)iterator1.next()).startPlaying(null)) { }
                pausedForPhone.clear();
            }
        }
        return null;
    }

    public void onReset()
    {
        onDestroy();
    }

    public void pausePlayingAudio(String s)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            audioplayer.pausePlaying();
        }
    }

    public void seekToAudio(String s, int i)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            audioplayer.seekToPlaying(i);
        }
    }

    public void setAudioOutputDevice(int i)
    {
        AudioManager audiomanager = (AudioManager)cordova.getActivity().getSystemService("audio");
        if (i == 2)
        {
            audiomanager.setRouting(0, 2, -1);
            return;
        }
        if (i == 1)
        {
            audiomanager.setRouting(0, 1, -1);
            return;
        } else
        {
            System.out.println("AudioHandler.setAudioOutputDevice() Error: Unknown output device.");
            return;
        }
    }

    public void setVolume(String s, float f)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            audioplayer.setVolume(f);
            return;
        } else
        {
            System.out.println((new StringBuilder()).append("AudioHandler.setVolume() Error: Unknown Audio Player ").append(s).toString());
            return;
        }
    }

    public void startPlayingAudio(String s, String s1)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer == null)
        {
            audioplayer = new AudioPlayer(this, s, s1);
            players.put(s, audioplayer);
        }
        audioplayer.startPlaying(s1);
    }

    public void startRecordingAudio(String s, String s1)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer == null)
        {
            audioplayer = new AudioPlayer(this, s, s1);
            players.put(s, audioplayer);
        }
        audioplayer.startRecording(s1);
    }

    public void stopPlayingAudio(String s)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            audioplayer.stopPlaying();
        }
    }

    public void stopRecordingAudio(String s)
    {
        AudioPlayer audioplayer = (AudioPlayer)players.get(s);
        if (audioplayer != null)
        {
            audioplayer.stopRecording();
        }
    }

}
