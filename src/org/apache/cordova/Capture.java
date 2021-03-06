// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            DirectoryManager, FileHelper

public class Capture extends CordovaPlugin
{

    private static final String AUDIO_3GPP = "audio/3gpp";
    private static final int CAPTURE_AUDIO = 0;
    private static final int CAPTURE_IMAGE = 1;
    private static final int CAPTURE_INTERNAL_ERR = 0;
    private static final int CAPTURE_NO_MEDIA_FILES = 3;
    private static final int CAPTURE_VIDEO = 2;
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String LOG_TAG = "Capture";
    private static final String VIDEO_3GPP = "video/3gpp";
    private static final String VIDEO_MP4 = "video/mp4";
    private CallbackContext callbackContext;
    private double duration;
    private long limit;
    private int numPics;
    private JSONArray results;

    public Capture()
    {
    }

    private void captureAudio()
    {
        Intent intent = new Intent("android.provider.MediaStore.RECORD_SOUND");
        cordova.startActivityForResult(this, intent, 0);
    }

    private void captureImage()
    {
        numPics = queryImgDB(whichContentStore()).getCount();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(new File(DirectoryManager.getTempDirectoryPath(cordova.getActivity()), "Capture.jpg")));
        cordova.startActivityForResult(this, intent, 1);
    }

    private void captureVideo(double d)
    {
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        if (android.os.Build.VERSION.SDK_INT > 8)
        {
            intent.putExtra("android.intent.extra.durationLimit", d);
        }
        cordova.startActivityForResult(this, intent, 2);
    }

    private void checkForDuplicateImage()
    {
        Uri uri = whichContentStore();
        Cursor cursor = queryImgDB(uri);
        if (cursor.getCount() - numPics == 2)
        {
            cursor.moveToLast();
            int i = -1 + Integer.valueOf(cursor.getString(cursor.getColumnIndex("_id"))).intValue();
            Uri uri1 = Uri.parse((new StringBuilder()).append(uri).append("/").append(i).toString());
            cordova.getActivity().getContentResolver().delete(uri1, null, null);
        }
    }

    private JSONObject createErrorObject(int i, String s)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("code", i);
            jsonobject.put("message", s);
        }
        catch (JSONException jsonexception)
        {
            return jsonobject;
        }
        return jsonobject;
    }

    private JSONObject createMediaFile(Uri uri)
    {
        File file;
        JSONObject jsonobject;
        file = new File(FileHelper.getRealPath(uri, cordova));
        jsonobject = new JSONObject();
        jsonobject.put("name", file.getName());
        jsonobject.put("fullPath", (new StringBuilder()).append("file://").append(file.getAbsolutePath()).toString());
        if (!file.getAbsoluteFile().toString().endsWith(".3gp") && !file.getAbsoluteFile().toString().endsWith(".3gpp"))
        {
            break MISSING_BLOCK_LABEL_161;
        }
        if (!uri.toString().contains("/audio/")) goto _L2; else goto _L1
_L1:
        jsonobject.put("type", "audio/3gpp");
_L3:
        jsonobject.put("lastModifiedDate", file.lastModified());
        jsonobject.put("size", file.length());
        return jsonobject;
_L2:
        try
        {
            jsonobject.put("type", "video/3gpp");
        }
        catch (JSONException jsonexception)
        {
            jsonexception.printStackTrace();
            return jsonobject;
        }
          goto _L3
        jsonobject.put("type", FileHelper.getMimeType(file.getAbsolutePath(), cordova));
          goto _L3
    }

    private JSONObject getAudioVideoData(String s, JSONObject jsonobject, boolean flag)
        throws JSONException
    {
        MediaPlayer mediaplayer = new MediaPlayer();
        try
        {
            mediaplayer.setDataSource(s);
            mediaplayer.prepare();
            jsonobject.put("duration", mediaplayer.getDuration() / 1000);
        }
        catch (IOException ioexception)
        {
            Log.d("Capture", "Error: loading video file");
            return jsonobject;
        }
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_67;
        }
        jsonobject.put("height", mediaplayer.getVideoHeight());
        jsonobject.put("width", mediaplayer.getVideoWidth());
        return jsonobject;
    }

    private JSONObject getFormatData(String s, String s1)
        throws JSONException
    {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("height", 0);
        jsonobject.put("width", 0);
        jsonobject.put("bitrate", 0);
        jsonobject.put("duration", 0);
        jsonobject.put("codecs", "");
        if (s1 == null || s1.equals("") || "null".equals(s1))
        {
            s1 = FileHelper.getMimeType(s, cordova);
        }
        Log.d("Capture", (new StringBuilder()).append("Mime type = ").append(s1).toString());
        if (s1.equals("image/jpeg") || s.endsWith(".jpg"))
        {
            jsonobject = getImageData(s, jsonobject);
        } else
        {
            if (s1.endsWith("audio/3gpp"))
            {
                return getAudioVideoData(s, jsonobject, false);
            }
            if (s1.equals("video/3gpp") || s1.equals("video/mp4"))
            {
                return getAudioVideoData(s, jsonobject, true);
            }
        }
        return jsonobject;
    }

    private JSONObject getImageData(String s, JSONObject jsonobject)
        throws JSONException
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(FileHelper.stripFileProtocol(s), options);
        jsonobject.put("height", options.outHeight);
        jsonobject.put("width", options.outWidth);
        return jsonobject;
    }

    private Cursor queryImgDB(Uri uri)
    {
        return cordova.getActivity().getContentResolver().query(uri, new String[] {
            "_id"
        }, null, null, null);
    }

    private Uri whichContentStore()
    {
        if (Environment.getExternalStorageState().equals("mounted"))
        {
            return android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else
        {
            return android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        callbackContext = callbackcontext;
        limit = 1L;
        duration = 0.0D;
        results = new JSONArray();
        JSONObject jsonobject = jsonarray.optJSONObject(0);
        if (jsonobject != null)
        {
            limit = jsonobject.optLong("limit", 1L);
            duration = jsonobject.optDouble("duration", 0.0D);
        }
        if (s.equals("getFormatData"))
        {
            callbackcontext.success(getFormatData(jsonarray.getString(0), jsonarray.getString(1)));
            return true;
        }
        if (s.equals("captureAudio"))
        {
            captureAudio();
            return true;
        }
        if (s.equals("captureImage"))
        {
            captureImage();
            return true;
        }
        if (s.equals("captureVideo"))
        {
            captureVideo(duration);
            return true;
        } else
        {
            return false;
        }
    }

    public void fail(JSONObject jsonobject)
    {
        callbackContext.error(jsonobject);
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        if (j != -1)
        {
            break MISSING_BLOCK_LABEL_446;
        }
        if (i != 0) goto _L2; else goto _L1
_L1:
        Uri uri4 = intent.getData();
        results.put(createMediaFile(uri4));
        if ((long)results.length() < limit) goto _L4; else goto _L3
_L3:
        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, results));
_L8:
        return;
_L4:
        captureAudio();
        return;
_L2:
        if (i != 1)
        {
            continue; /* Loop/switch isn't completed */
        }
        ContentValues contentvalues;
        contentvalues = new ContentValues();
        contentvalues.put("mime_type", "image/jpeg");
        Uri uri3 = cordova.getActivity().getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentvalues);
        Uri uri2 = uri3;
_L6:
        FileInputStream fileinputstream;
        OutputStream outputstream;
        byte abyte0[];
        fileinputstream = new FileInputStream((new StringBuilder()).append(DirectoryManager.getTempDirectoryPath(cordova.getActivity())).append("/Capture.jpg").toString());
        outputstream = cordova.getActivity().getContentResolver().openOutputStream(uri2);
        abyte0 = new byte[4096];
_L5:
        int k = fileinputstream.read(abyte0);
        if (k == -1)
        {
            break MISSING_BLOCK_LABEL_298;
        }
        try
        {
            outputstream.write(abyte0, 0, k);
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            fail(createErrorObject(0, "Error capturing image."));
            return;
        }
          goto _L5
        UnsupportedOperationException unsupportedoperationexception;
        unsupportedoperationexception;
        LOG.d("Capture", "Can't write to external media storage.");
        Uri uri1 = cordova.getActivity().getContentResolver().insert(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentvalues);
        uri2 = uri1;
          goto _L6
        UnsupportedOperationException unsupportedoperationexception1;
        unsupportedoperationexception1;
        LOG.d("Capture", "Can't write to internal media storage.");
        fail(createErrorObject(0, "Error capturing image - no media storage found."));
        return;
        outputstream.flush();
        outputstream.close();
        fileinputstream.close();
        results.put(createMediaFile(uri2));
        checkForDuplicateImage();
        if ((long)results.length() >= limit)
        {
            callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, results));
            return;
        }
        captureImage();
        return;
        if (i != 2) goto _L8; else goto _L7
_L7:
        Uri uri = intent.getData();
        results.put(createMediaFile(uri));
        if ((long)results.length() >= limit)
        {
            callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, results));
            return;
        } else
        {
            captureVideo(duration);
            return;
        }
        if (j == 0)
        {
            if (results.length() > 0)
            {
                callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, results));
                return;
            } else
            {
                fail(createErrorObject(3, "Canceled."));
                return;
            }
        }
        if (results.length() > 0)
        {
            callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, results));
            return;
        } else
        {
            fail(createErrorObject(3, "Did not complete!"));
            return;
        }
    }
}
