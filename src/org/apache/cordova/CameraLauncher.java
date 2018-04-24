// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

// Referenced classes of package org.apache.cordova:
//            FileHelper, DirectoryManager, ExifHelper

public class CameraLauncher extends CordovaPlugin
    implements android.media.MediaScannerConnection.MediaScannerConnectionClient
{

    private static final int ALLMEDIA = 2;
    private static final int CAMERA = 1;
    private static final int DATA_URL = 0;
    private static final int FILE_URI = 1;
    private static final String GET_All = "Get All";
    private static final String GET_PICTURE = "Get Picture";
    private static final String GET_VIDEO = "Get Video";
    private static final int JPEG = 0;
    private static final String LOG_TAG = "CameraLauncher";
    private static final int NATIVE_URI = 2;
    private static final int PHOTOLIBRARY = 0;
    private static final int PICTURE = 0;
    private static final int PNG = 1;
    private static final int SAVEDPHOTOALBUM = 2;
    private static final int VIDEO = 1;
    public CallbackContext callbackContext;
    private MediaScannerConnection conn;
    private boolean correctOrientation;
    private int encodingType;
    private Uri imageUri;
    private int mQuality;
    private int mediaType;
    private int numPics;
    private boolean saveToPhotoAlbum;
    private Uri scanMe;
    private int targetHeight;
    private int targetWidth;

    public CameraLauncher()
    {
    }

    public static int calculateSampleSize(int i, int j, int k, int l)
    {
        if ((float)i / (float)j > (float)k / (float)l)
        {
            return i / k;
        } else
        {
            return j / l;
        }
    }

    private void checkForDuplicateImage(int i)
    {
        byte byte0 = 1;
        Uri uri = whichContentStore();
        Cursor cursor = queryImgDB(uri);
        int j = cursor.getCount();
        if (i == 1 && saveToPhotoAlbum)
        {
            byte0 = 2;
        }
        if (j - numPics == byte0)
        {
            cursor.moveToLast();
            int k = Integer.valueOf(cursor.getString(cursor.getColumnIndex("_id"))).intValue();
            if (byte0 == 2)
            {
                k--;
            }
            Uri uri1 = Uri.parse((new StringBuilder()).append(uri).append("/").append(k).toString());
            cordova.getActivity().getContentResolver().delete(uri1, null, null);
        }
    }

    private void cleanup(int i, Uri uri, Uri uri1, Bitmap bitmap)
    {
        if (bitmap != null)
        {
            bitmap.recycle();
        }
        (new File(FileHelper.stripFileProtocol(uri.toString()))).delete();
        checkForDuplicateImage(i);
        if (saveToPhotoAlbum && uri1 != null)
        {
            scanForGallery(uri1);
        }
        System.gc();
    }

    private File createCaptureFile(int i)
    {
        if (i == 0)
        {
            return new File(DirectoryManager.getTempDirectoryPath(cordova.getActivity()), ".Pic.jpg");
        }
        if (i == 1)
        {
            return new File(DirectoryManager.getTempDirectoryPath(cordova.getActivity()), ".Pic.png");
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid Encoding Type: ").append(i).toString());
        }
    }

    private int getImageOrientation(Uri uri)
    {
        String as[] = {
            "orientation"
        };
        Cursor cursor = cordova.getActivity().getContentResolver().query(uri, as, null, null, null);
        int i = 0;
        if (cursor != null)
        {
            cursor.moveToPosition(0);
            i = cursor.getInt(0);
            cursor.close();
        }
        return i;
    }

    private Bitmap getRotatedBitmap(int i, Bitmap bitmap, ExifHelper exifhelper)
    {
        Matrix matrix = new Matrix();
        Bitmap bitmap1;
        if (i == 180)
        {
            matrix.setRotate(i);
        } else
        {
            matrix.setRotate(i, (float)bitmap.getWidth() / 2.0F, (float)bitmap.getHeight() / 2.0F);
        }
        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        exifhelper.resetOrientation();
        return bitmap1;
    }

    private Bitmap getScaledBitmap(String s)
        throws IOException
    {
        Bitmap bitmap;
        if (targetWidth <= 0 && targetHeight <= 0)
        {
            bitmap = BitmapFactory.decodeStream(FileHelper.getInputStreamFromUriString(s, cordova));
        } else
        {
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(FileHelper.getInputStreamFromUriString(s, cordova), null, options);
            int i = options.outWidth;
            bitmap = null;
            if (i != 0)
            {
                int j = options.outHeight;
                bitmap = null;
                if (j != 0)
                {
                    int ai[] = calculateAspectRatio(options.outWidth, options.outHeight);
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, targetWidth, targetHeight);
                    Bitmap bitmap1 = BitmapFactory.decodeStream(FileHelper.getInputStreamFromUriString(s, cordova), null, options);
                    bitmap = null;
                    if (bitmap1 != null)
                    {
                        return Bitmap.createScaledBitmap(bitmap1, ai[0], ai[1], true);
                    }
                }
            }
        }
        return bitmap;
    }

    private Uri getUriFromMediaStore()
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("mime_type", "image/jpeg");
        Uri uri1;
        try
        {
            uri1 = cordova.getActivity().getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentvalues);
        }
        catch (UnsupportedOperationException unsupportedoperationexception)
        {
            LOG.d("CameraLauncher", "Can't write to external media storage.");
            Uri uri;
            try
            {
                uri = cordova.getActivity().getContentResolver().insert(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentvalues);
            }
            catch (UnsupportedOperationException unsupportedoperationexception1)
            {
                LOG.d("CameraLauncher", "Can't write to internal media storage.");
                return null;
            }
            return uri;
        }
        return uri1;
    }

    private Cursor queryImgDB(Uri uri)
    {
        return cordova.getActivity().getContentResolver().query(uri, new String[] {
            "_id"
        }, null, null, null);
    }

    private void scanForGallery(Uri uri)
    {
        scanMe = uri;
        if (conn != null)
        {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(cordova.getActivity().getApplicationContext(), this);
        conn.connect();
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

    private void writeUncompressedImage(Uri uri)
        throws FileNotFoundException, IOException
    {
        FileInputStream fileinputstream = new FileInputStream(FileHelper.stripFileProtocol(imageUri.toString()));
        OutputStream outputstream = cordova.getActivity().getContentResolver().openOutputStream(uri);
        byte abyte0[] = new byte[4096];
        do
        {
            int i = fileinputstream.read(abyte0);
            if (i != -1)
            {
                outputstream.write(abyte0, 0, i);
            } else
            {
                outputstream.flush();
                outputstream.close();
                fileinputstream.close();
                return;
            }
        } while (true);
    }

    public int[] calculateAspectRatio(int i, int j)
    {
        int k;
        int l;
        k = targetWidth;
        l = targetHeight;
        if (k > 0 || l > 0) goto _L2; else goto _L1
_L1:
        k = i;
        l = j;
_L4:
        return (new int[] {
            k, l
        });
_L2:
        if (k > 0 && l <= 0)
        {
            l = (k * j) / i;
        } else
        if (k <= 0 && l > 0)
        {
            k = (l * i) / j;
        } else
        {
            double d = (double)k / (double)l;
            double d1 = (double)i / (double)j;
            if (d1 > d)
            {
                l = (k * j) / i;
            } else
            if (d1 < d)
            {
                k = (l * i) / j;
            }
        }
        if (true) goto _L4; else goto _L3
_L3:
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        callbackContext = callbackcontext;
        if (!s.equals("takePicture")) goto _L2; else goto _L1
_L1:
        int i;
        int j;
        saveToPhotoAlbum = false;
        targetHeight = 0;
        targetWidth = 0;
        encodingType = 0;
        mediaType = 0;
        mQuality = 80;
        mQuality = jsonarray.getInt(0);
        i = jsonarray.getInt(1);
        j = jsonarray.getInt(2);
        targetWidth = jsonarray.getInt(3);
        targetHeight = jsonarray.getInt(4);
        encodingType = jsonarray.getInt(5);
        mediaType = jsonarray.getInt(6);
        correctOrientation = jsonarray.getBoolean(8);
        saveToPhotoAlbum = jsonarray.getBoolean(9);
        if (targetWidth < 1)
        {
            targetWidth = -1;
        }
        if (targetHeight < 1)
        {
            targetHeight = -1;
        }
        if (j != 1) goto _L4; else goto _L3
_L3:
        takePicture(i, encodingType);
_L5:
        PluginResult pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.NO_RESULT);
        pluginresult.setKeepCallback(true);
        callbackcontext.sendPluginResult(pluginresult);
        return true;
_L4:
        if (j == 0 || j == 2)
        {
            getImage(j, i);
        }
        if (true) goto _L5; else goto _L2
_L2:
        return false;
    }

    public void failPicture(String s)
    {
        callbackContext.error(s);
    }

    public void getImage(int i, int j)
    {
        Intent intent;
        String s;
        intent = new Intent();
        s = "Get Picture";
        if (mediaType != 0) goto _L2; else goto _L1
_L1:
        intent.setType("image/*");
_L4:
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (cordova != null)
        {
            cordova.startActivityForResult(this, Intent.createChooser(intent, new String(s)), 1 + (j + 16 * (i + 1)));
        }
        return;
_L2:
        if (mediaType == 1)
        {
            intent.setType("video/*");
            s = "Get Video";
        } else
        if (mediaType == 2)
        {
            intent.setType("*/*");
            s = "Get All";
        }
        if (true) goto _L4; else goto _L3
_L3:
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        int k;
        int l;
        k = -1 + i / 16;
        l = -1 + i % 16;
        if (k != 1) goto _L2; else goto _L1
_L1:
        if (j != -1) goto _L4; else goto _L3
_L3:
        ExifHelper exifhelper1 = new ExifHelper();
        int l1 = encodingType;
        int k1;
        k1 = 0;
        if (l1 != 0)
        {
            break MISSING_BLOCK_LABEL_102;
        }
        int i2;
        exifhelper1.createInFile((new StringBuilder()).append(DirectoryManager.getTempDirectoryPath(cordova.getActivity())).append("/.Pic.jpg").toString());
        exifhelper1.readExifData();
        i2 = exifhelper1.getOrientation();
        k1 = i2;
_L7:
        Bitmap bitmap2;
        Uri uri1;
        bitmap2 = null;
        uri1 = null;
        if (l != 0) goto _L6; else goto _L5
_L5:
        bitmap2 = getScaledBitmap(FileHelper.stripFileProtocol(imageUri.toString()));
        if (bitmap2 != null)
        {
            break MISSING_BLOCK_LABEL_149;
        }
        bitmap2 = (Bitmap)intent.getExtras().get("data");
        if (bitmap2 != null)
        {
            break MISSING_BLOCK_LABEL_199;
        }
        IOException ioexception2;
        try
        {
            Log.d("CameraLauncher", "I either have a null image path or bitmap");
            failPicture("Unable to create bitmap!");
            return;
        }
        catch (IOException ioexception3)
        {
            ioexception3.printStackTrace();
        }
        break MISSING_BLOCK_LABEL_191;
        ioexception2;
        ioexception2.printStackTrace();
        k1 = 0;
          goto _L7
        failPicture("Error capturing image.");
        return;
        if (k1 == 0)
        {
            break MISSING_BLOCK_LABEL_223;
        }
        if (correctOrientation)
        {
            bitmap2 = getRotatedBitmap(k1, bitmap2, exifhelper1);
        }
        processPicture(bitmap2);
        checkForDuplicateImage(0);
_L28:
        cleanup(1, imageUri, uri1, bitmap2);
        return;
_L27:
        if (!saveToPhotoAlbum) goto _L9; else goto _L8
_L8:
        uri1 = Uri.fromFile(new File(FileHelper.getRealPath(getUriFromMediaStore(), cordova)));
_L12:
        if (uri1 != null)
        {
            break MISSING_BLOCK_LABEL_290;
        }
        failPicture("Error capturing image - no media storage found.");
        if (targetHeight != -1 || targetWidth != -1 || mQuality != 100 || correctOrientation) goto _L11; else goto _L10
_L10:
        writeUncompressedImage(uri1);
        callbackContext.success(uri1.toString());
_L14:
        callbackContext.success(uri1.toString());
        break; /* Loop/switch isn't completed */
_L9:
        uri1 = Uri.fromFile(new File(DirectoryManager.getTempDirectoryPath(cordova.getActivity()), (new StringBuilder()).append(System.currentTimeMillis()).append(".jpg").toString()));
          goto _L12
_L11:
        bitmap2 = getScaledBitmap(FileHelper.stripFileProtocol(imageUri.toString()));
        if (k1 == 0)
        {
            break MISSING_BLOCK_LABEL_444;
        }
        if (correctOrientation)
        {
            bitmap2 = getRotatedBitmap(k1, bitmap2, exifhelper1);
        }
        OutputStream outputstream = cordova.getActivity().getContentResolver().openOutputStream(uri1);
        bitmap2.compress(android.graphics.Bitmap.CompressFormat.JPEG, mQuality, outputstream);
        outputstream.close();
        if (encodingType != 0) goto _L14; else goto _L13
_L13:
        if (!saveToPhotoAlbum) goto _L16; else goto _L15
_L15:
        String s5;
        CordovaInterface cordovainterface = cordova;
        s5 = FileHelper.getRealPath(uri1, cordovainterface);
_L17:
        exifhelper1.createOutFile(s5);
        exifhelper1.writeExifData();
          goto _L14
_L16:
        String s4 = uri1.getPath();
        s5 = s4;
          goto _L17
_L4:
        if (j == 0)
        {
            failPicture("Camera cancelled.");
            return;
        } else
        {
            failPicture("Did not complete!");
            return;
        }
_L2:
        if (k != 0 && k != 2)
        {
            break MISSING_BLOCK_LABEL_1111;
        }
        if (j != -1) goto _L19; else goto _L18
_L18:
        Uri uri;
        String s;
        uri = intent.getData();
        if (mediaType != 0)
        {
            callbackContext.success(uri.toString());
            return;
        }
        if (targetHeight == -1 && targetWidth == -1 && (l == 1 || l == 2) && !correctOrientation)
        {
            callbackContext.success(uri.toString());
            return;
        }
        s = uri.toString();
        String s1 = FileHelper.getMimeType(s, cordova);
        if (!"image/jpeg".equalsIgnoreCase(s1) && !"image/png".equalsIgnoreCase(s1))
        {
            Log.d("CameraLauncher", "I either have a null image path or bitmap");
            failPicture("Unable to retrieve path to picture!");
            return;
        }
        Bitmap bitmap1 = getScaledBitmap(s);
        Bitmap bitmap = bitmap1;
_L21:
        if (bitmap == null)
        {
            Log.d("CameraLauncher", "I either have a null image path or bitmap");
            failPicture("Unable to create bitmap!");
            return;
        }
        break; /* Loop/switch isn't completed */
        IOException ioexception;
        ioexception;
        ioexception.printStackTrace();
        bitmap = null;
        if (true) goto _L21; else goto _L20
_L20:
        if (correctOrientation)
        {
            int j1 = getImageOrientation(uri);
            if (j1 != 0)
            {
                Matrix matrix = new Matrix();
                matrix.setRotate(j1);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }
        if (l != 0) goto _L23; else goto _L22
_L22:
        processPicture(bitmap);
_L25:
        if (bitmap != null)
        {
            bitmap.recycle();
        }
        System.gc();
        return;
_L23:
        if (l != 1 && l != 2)
        {
            continue; /* Loop/switch isn't completed */
        }
        if (targetHeight <= 0 || targetWidth <= 0)
        {
            break MISSING_BLOCK_LABEL_1077;
        }
        String s2;
        String s3;
        ExifHelper exifhelper;
        int i1;
        FileOutputStream fileoutputstream;
        try
        {
            s2 = (new StringBuilder()).append(DirectoryManager.getTempDirectoryPath(cordova.getActivity())).append("/resize.jpg").toString();
            s3 = FileHelper.getRealPath(uri, cordova);
            exifhelper = new ExifHelper();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            failPicture("Error retrieving image.");
            continue; /* Loop/switch isn't completed */
        }
        if (s3 == null)
        {
            break MISSING_BLOCK_LABEL_952;
        }
        i1 = encodingType;
        if (i1 != 0)
        {
            break MISSING_BLOCK_LABEL_952;
        }
        exifhelper.createInFile(s3);
        exifhelper.readExifData();
        exifhelper.getOrientation();
_L24:
        fileoutputstream = new FileOutputStream(s2);
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, mQuality, fileoutputstream);
        fileoutputstream.close();
        if (s3 == null)
        {
            break MISSING_BLOCK_LABEL_1007;
        }
        if (encodingType == 0)
        {
            exifhelper.createOutFile(s2);
            exifhelper.writeExifData();
        }
        callbackContext.success((new StringBuilder()).append("file://").append(s2).append("?").append(System.currentTimeMillis()).toString());
        continue; /* Loop/switch isn't completed */
        IOException ioexception1;
        ioexception1;
        ioexception1.printStackTrace();
          goto _L24
        callbackContext.success(uri.toString());
        if (true) goto _L25; else goto _L19
_L19:
        if (j == 0)
        {
            failPicture("Selection cancelled.");
            return;
        }
        failPicture("Selection did not complete!");
        return;
_L6:
        if (l == 1) goto _L27; else goto _L26
_L26:
        bitmap2 = null;
        uri1 = null;
        if (l != 2) goto _L28; else goto _L27
    }

    public void onMediaScannerConnected()
    {
        try
        {
            conn.scanFile(scanMe.toString(), "image/*");
            return;
        }
        catch (IllegalStateException illegalstateexception)
        {
            LOG.e("CameraLauncher", "Can't scan file in MediaScanner after taking picture");
        }
    }

    public void onScanCompleted(String s, Uri uri)
    {
        conn.disconnect();
    }

    public void processPicture(Bitmap bitmap)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        if (bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, mQuality, bytearrayoutputstream))
        {
            String s = new String(Base64.encode(bytearrayoutputstream.toByteArray(), 0));
            callbackContext.success(s);
        }
_L1:
        return;
        Exception exception;
        exception;
        failPicture("Error compressing image.");
          goto _L1
    }

    public void takePicture(int i, int j)
    {
        numPics = queryImgDB(whichContentStore()).getCount();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = createCaptureFile(j);
        intent.putExtra("output", Uri.fromFile(file));
        imageUri = Uri.fromFile(file);
        if (cordova != null)
        {
            cordova.startActivityForResult(this, intent, 1 + (i + 32));
        }
    }
}
