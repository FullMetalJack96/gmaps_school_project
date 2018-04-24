// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.file.EncodingException;
import org.apache.cordova.file.FileExistsException;
import org.apache.cordova.file.InvalidModificationException;
import org.apache.cordova.file.NoModificationAllowedException;
import org.apache.cordova.file.TypeMismatchException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            FileHelper, DirectoryManager

public class FileUtils extends CordovaPlugin
{

    public static int ABORT_ERR = 0;
    public static int APPLICATION = 0;
    public static int ENCODING_ERR = 0;
    public static int INVALID_MODIFICATION_ERR = 0;
    public static int INVALID_STATE_ERR = 0;
    private static final String LOG_TAG = "FileUtils";
    public static int NOT_FOUND_ERR = 1;
    public static int NOT_READABLE_ERR = 4;
    public static int NO_MODIFICATION_ALLOWED_ERR = 6;
    public static int PATH_EXISTS_ERR = 12;
    public static int PERSISTENT = 1;
    public static int QUOTA_EXCEEDED_ERR = 10;
    public static int RESOURCE = 2;
    public static int SECURITY_ERR = 2;
    public static int SYNTAX_ERR = 8;
    public static int TEMPORARY = 0;
    public static int TYPE_MISMATCH_ERR = 11;

    public FileUtils()
    {
    }

    private boolean atRootDirectory(String s)
    {
        String s1 = FileHelper.getRealPath(s, cordova);
        return s1.equals((new StringBuilder()).append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/data/").append(cordova.getActivity().getPackageName()).append("/cache").toString()) || s1.equals(Environment.getExternalStorageDirectory().getAbsolutePath()) || s1.equals((new StringBuilder()).append("/data/data/").append(cordova.getActivity().getPackageName()).toString());
    }

    private void copyAction(File file, File file1)
        throws FileNotFoundException, IOException
    {
        FileInputStream fileinputstream;
        FileOutputStream fileoutputstream;
        FileChannel filechannel;
        FileChannel filechannel1;
        fileinputstream = new FileInputStream(file);
        fileoutputstream = new FileOutputStream(file1);
        filechannel = fileinputstream.getChannel();
        filechannel1 = fileoutputstream.getChannel();
        filechannel.transferTo(0L, filechannel.size(), filechannel1);
        fileinputstream.close();
        fileoutputstream.close();
        filechannel.close();
        filechannel1.close();
        return;
        Exception exception;
        exception;
        fileinputstream.close();
        fileoutputstream.close();
        filechannel.close();
        filechannel1.close();
        throw exception;
    }

    private JSONObject copyDirectory(File file, File file1)
        throws JSONException, IOException, NoModificationAllowedException, InvalidModificationException
    {
        if (file1.exists() && file1.isFile())
        {
            throw new InvalidModificationException("Can't rename a file to a directory");
        }
        if (isCopyOnItself(file.getAbsolutePath(), file1.getAbsolutePath()))
        {
            throw new InvalidModificationException("Can't copy itself into itself");
        }
        if (!file1.exists() && !file1.mkdir())
        {
            throw new NoModificationAllowedException("Couldn't create the destination directory");
        }
        File afile[] = file.listFiles();
        int i = afile.length;
        int j = 0;
        while (j < i) 
        {
            File file2 = afile[j];
            if (file2.isDirectory())
            {
                copyDirectory(file2, file1);
            } else
            {
                copyFile(file2, new File((new StringBuilder()).append(file1.getAbsoluteFile()).append(File.separator).append(file2.getName()).toString()));
            }
            j++;
        }
        return getEntry(file1);
    }

    private JSONObject copyFile(File file, File file1)
        throws IOException, InvalidModificationException, JSONException
    {
        if (file1.exists() && file1.isDirectory())
        {
            throw new InvalidModificationException("Can't rename a file to a directory");
        } else
        {
            copyAction(file, file1);
            return getEntry(file1);
        }
    }

    private File createDestination(String s, File file, File file1)
    {
        if ("null".equals(s) || "".equals(s))
        {
            s = null;
        }
        if (s != null)
        {
            return new File((new StringBuilder()).append(file1.getAbsolutePath()).append(File.separator).append(s).toString());
        } else
        {
            return new File((new StringBuilder()).append(file1.getAbsolutePath()).append(File.separator).append(file.getName()).toString());
        }
    }

    private File createFileObject(String s)
    {
        return new File(FileHelper.getRealPath(s, cordova));
    }

    private File createFileObject(String s, String s1)
    {
        if (s1.startsWith("/"))
        {
            return new File(s1);
        } else
        {
            String s2 = FileHelper.getRealPath(s, cordova);
            return new File((new StringBuilder()).append(s2).append(File.separator).append(s1).toString());
        }
    }

    public static JSONObject getEntry(File file)
        throws JSONException
    {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("isFile", file.isFile());
        jsonobject.put("isDirectory", file.isDirectory());
        jsonobject.put("name", file.getName());
        jsonobject.put("fullPath", (new StringBuilder()).append("file://").append(file.getAbsolutePath()).toString());
        return jsonobject;
    }

    private JSONObject getEntry(String s)
        throws JSONException
    {
        return getEntry(new File(s));
    }

    private JSONObject getFile(String s, String s1, JSONObject jsonobject, boolean flag)
        throws FileExistsException, IOException, TypeMismatchException, EncodingException, JSONException
    {
        boolean flag1 = false;
        boolean flag2 = false;
        if (jsonobject != null)
        {
            flag1 = jsonobject.optBoolean("create");
            flag2 = false;
            if (flag1)
            {
                flag2 = jsonobject.optBoolean("exclusive");
            }
        }
        if (s1.contains(":"))
        {
            throw new EncodingException("This file has a : in it's name");
        }
        File file = createFileObject(s, s1);
        if (flag1)
        {
            if (flag2 && file.exists())
            {
                throw new FileExistsException("create/exclusive fails");
            }
            if (flag)
            {
                file.mkdir();
            } else
            {
                file.createNewFile();
            }
            if (!file.exists())
            {
                throw new FileExistsException("create fails");
            }
        } else
        {
            if (!file.exists())
            {
                throw new FileNotFoundException("path does not exist");
            }
            if (flag)
            {
                if (file.isFile())
                {
                    throw new TypeMismatchException("path doesn't exist or is file");
                }
            } else
            if (file.isDirectory())
            {
                throw new TypeMismatchException("path doesn't exist or is directory");
            }
        }
        return getEntry(file);
    }

    private JSONObject getFileMetadata(String s)
        throws FileNotFoundException, JSONException
    {
        File file = createFileObject(s);
        if (!file.exists())
        {
            throw new FileNotFoundException((new StringBuilder()).append("File: ").append(s).append(" does not exist.").toString());
        } else
        {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("size", file.length());
            jsonobject.put("type", FileHelper.getMimeType(s, cordova));
            jsonobject.put("name", file.getName());
            jsonobject.put("fullPath", s);
            jsonobject.put("lastModifiedDate", file.lastModified());
            return jsonobject;
        }
    }

    private long getMetadata(String s)
        throws FileNotFoundException
    {
        File file = createFileObject(s);
        if (!file.exists())
        {
            throw new FileNotFoundException("Failed to find file in getMetadata");
        } else
        {
            return file.lastModified();
        }
    }

    private JSONObject getParent(String s)
        throws JSONException
    {
        String s1 = FileHelper.getRealPath(s, cordova);
        if (atRootDirectory(s1))
        {
            return getEntry(s1);
        } else
        {
            return getEntry((new File(s1)).getParent());
        }
    }

    private boolean isCopyOnItself(String s, String s1)
    {
        return s1.startsWith(s) && s1.indexOf(File.separator, -1 + s.length()) != -1;
    }

    private JSONObject moveDirectory(File file, File file1)
        throws IOException, JSONException, InvalidModificationException, NoModificationAllowedException, FileExistsException
    {
label0:
        {
            if (file1.exists() && file1.isFile())
            {
                throw new InvalidModificationException("Can't rename a file to a directory");
            }
            if (isCopyOnItself(file.getAbsolutePath(), file1.getAbsolutePath()))
            {
                throw new InvalidModificationException("Can't move itself into itself");
            }
            if (file1.exists() && file1.list().length > 0)
            {
                throw new InvalidModificationException("directory is not empty");
            }
            if (!file.renameTo(file1))
            {
                copyDirectory(file, file1);
                if (!file1.exists())
                {
                    break label0;
                }
                removeDirRecursively(file);
            }
            return getEntry(file1);
        }
        throw new IOException("moved failed");
    }

    private JSONObject moveFile(File file, File file1)
        throws IOException, JSONException, InvalidModificationException
    {
label0:
        {
            if (file1.exists() && file1.isDirectory())
            {
                throw new InvalidModificationException("Can't rename a file to a directory");
            }
            if (!file.renameTo(file1))
            {
                copyAction(file, file1);
                if (!file1.exists())
                {
                    break label0;
                }
                file.delete();
            }
            return getEntry(file1);
        }
        throw new IOException("moved failed");
    }

    private void notifyDelete(String s)
    {
        String s1 = FileHelper.getRealPath(s, cordova);
        try
        {
            cordova.getActivity().getContentResolver().delete(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data = ?", new String[] {
                s1
            });
            return;
        }
        catch (UnsupportedOperationException unsupportedoperationexception)
        {
            return;
        }
    }

    private byte[] readAsBinaryHelper(String s, int i, int j)
        throws IOException
    {
        int k = j - i;
        byte abyte0[] = new byte[k];
        InputStream inputstream = FileHelper.getInputStreamFromUriString(s, cordova);
        int l = 0;
        if (i > 0)
        {
            inputstream.skip(i);
        }
        do
        {
            if (k <= 0)
            {
                break;
            }
            l = inputstream.read(abyte0, l, k);
            if (l < 0)
            {
                break;
            }
            k -= l;
        } while (true);
        return abyte0;
    }

    private JSONArray readEntries(String s)
        throws FileNotFoundException, JSONException
    {
        File file = createFileObject(s);
        if (!file.exists())
        {
            throw new FileNotFoundException();
        }
        JSONArray jsonarray = new JSONArray();
        if (file.isDirectory())
        {
            File afile[] = file.listFiles();
            for (int i = 0; i < afile.length; i++)
            {
                if (afile[i].canRead())
                {
                    jsonarray.put(getEntry(afile[i]));
                }
            }

        }
        return jsonarray;
    }

    private boolean remove(String s)
        throws NoModificationAllowedException, InvalidModificationException
    {
        File file = createFileObject(s);
        if (atRootDirectory(s))
        {
            throw new NoModificationAllowedException("You can't delete the root directory");
        }
        if (file.isDirectory() && file.list().length > 0)
        {
            throw new InvalidModificationException("You can't delete a directory that is not empty.");
        } else
        {
            return file.delete();
        }
    }

    private boolean removeDirRecursively(File file)
        throws FileExistsException
    {
        if (file.isDirectory())
        {
            File afile[] = file.listFiles();
            int i = afile.length;
            for (int j = 0; j < i; j++)
            {
                removeDirRecursively(afile[j]);
            }

        }
        if (!file.delete())
        {
            throw new FileExistsException((new StringBuilder()).append("could not delete: ").append(file.getName()).toString());
        } else
        {
            return true;
        }
    }

    private boolean removeRecursively(String s)
        throws FileExistsException
    {
        File file = createFileObject(s);
        if (atRootDirectory(s))
        {
            return false;
        } else
        {
            return removeDirRecursively(file);
        }
    }

    private JSONObject requestFileSystem(int i)
        throws IOException, JSONException
    {
        JSONObject jsonobject = new JSONObject();
        if (i == TEMPORARY)
        {
            jsonobject.put("name", "temporary");
            if (Environment.getExternalStorageState().equals("mounted"))
            {
                (new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/data/").append(cordova.getActivity().getPackageName()).append("/cache/").toString())).mkdirs();
                jsonobject.put("root", getEntry((new StringBuilder()).append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/data/").append(cordova.getActivity().getPackageName()).append("/cache/").toString()));
                return jsonobject;
            } else
            {
                (new File((new StringBuilder()).append("/data/data/").append(cordova.getActivity().getPackageName()).append("/cache/").toString())).mkdirs();
                jsonobject.put("root", getEntry((new StringBuilder()).append("/data/data/").append(cordova.getActivity().getPackageName()).append("/cache/").toString()));
                return jsonobject;
            }
        }
        if (i == PERSISTENT)
        {
            jsonobject.put("name", "persistent");
            if (Environment.getExternalStorageState().equals("mounted"))
            {
                jsonobject.put("root", getEntry(Environment.getExternalStorageDirectory()));
                return jsonobject;
            } else
            {
                jsonobject.put("root", getEntry((new StringBuilder()).append("/data/data/").append(cordova.getActivity().getPackageName()).toString()));
                return jsonobject;
            }
        } else
        {
            throw new IOException("No filesystem of type requested");
        }
    }

    private JSONObject resolveLocalFileSystemURI(String s)
        throws IOException, JSONException
    {
        String s1 = URLDecoder.decode(s, "UTF-8");
        File file;
        if (s1.startsWith("content:"))
        {
            Cursor cursor = cordova.getActivity().managedQuery(Uri.parse(s1), new String[] {
                "_data"
            }, null, null, null);
            int j = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            file = new File(cursor.getString(j));
        } else
        {
            new URL(s1);
            if (s1.startsWith("file://"))
            {
                int i = s1.indexOf("?");
                if (i < 0)
                {
                    file = new File(s1.substring(7, s1.length()));
                } else
                {
                    file = new File(s1.substring(7, i));
                }
            } else
            {
                file = new File(s1);
            }
        }
        if (!file.exists())
        {
            throw new FileNotFoundException();
        }
        if (!file.canRead())
        {
            throw new IOException();
        } else
        {
            return getEntry(file);
        }
    }

    private JSONObject transferTo(String s, String s1, String s2, boolean flag)
        throws JSONException, NoModificationAllowedException, IOException, InvalidModificationException, EncodingException, FileExistsException
    {
        File file;
        File file2;
        String s3 = FileHelper.getRealPath(s, cordova);
        String s4 = FileHelper.getRealPath(s1, cordova);
        if (s2 != null && s2.contains(":"))
        {
            throw new EncodingException("Bad file name");
        }
        file = new File(s3);
        if (!file.exists())
        {
            throw new FileNotFoundException("The source does not exist");
        }
        File file1 = new File(s4);
        if (!file1.exists())
        {
            throw new FileNotFoundException("The source does not exist");
        }
        file2 = createDestination(s2, file, file1);
        if (file.getAbsolutePath().equals(file2.getAbsolutePath()))
        {
            throw new InvalidModificationException("Can't copy a file onto itself");
        }
        if (!file.isDirectory()) goto _L2; else goto _L1
_L1:
        if (!flag) goto _L4; else goto _L3
_L3:
        JSONObject jsonobject = moveDirectory(file, file2);
_L6:
        return jsonobject;
_L4:
        return copyDirectory(file, file2);
_L2:
        if (flag)
        {
            jsonobject = moveFile(file, file2);
            if (s.startsWith("content://"))
            {
                notifyDelete(s);
                return jsonobject;
            }
        } else
        {
            return copyFile(file, file2);
        }
        if (true) goto _L6; else goto _L5
_L5:
    }

    private long truncateFile(String s, long l)
        throws FileNotFoundException, IOException, NoModificationAllowedException
    {
        RandomAccessFile randomaccessfile;
        if (s.startsWith("content://"))
        {
            throw new NoModificationAllowedException("Couldn't truncate file given its content URI");
        }
        randomaccessfile = new RandomAccessFile(FileHelper.getRealPath(s, cordova), "rw");
        if (randomaccessfile.length() < l) goto _L2; else goto _L1
_L1:
        randomaccessfile.getChannel().truncate(l);
_L4:
        randomaccessfile.close();
        return l;
_L2:
        long l1 = randomaccessfile.length();
        l = l1;
        if (true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
        randomaccessfile.close();
        throw exception;
    }

    public boolean execute(String s, JSONArray jsonarray, CallbackContext callbackcontext)
        throws JSONException
    {
        if (s.equals("testSaveLocationExists"))
        {
            boolean flag2 = DirectoryManager.testSaveLocationExists();
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, flag2));
            break MISSING_BLOCK_LABEL_980;
        }
        FileNotFoundException filenotfoundexception;
        if (s.equals("getFreeDiskSpace"))
        {
            long l3 = DirectoryManager.getFreeDiskSpace(false);
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, l3));
            break MISSING_BLOCK_LABEL_980;
        }
        FileExistsException fileexistsexception;
        if (s.equals("testFileExists"))
        {
            boolean flag1 = DirectoryManager.testFileExists(jsonarray.getString(0));
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, flag1));
            break MISSING_BLOCK_LABEL_980;
        }
        NoModificationAllowedException nomodificationallowedexception;
        if (s.equals("testDirectoryExists"))
        {
            boolean flag = DirectoryManager.testFileExists(jsonarray.getString(0));
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, flag));
            break MISSING_BLOCK_LABEL_980;
        }
        InvalidModificationException invalidmodificationexception;
        if (s.equals("readAsText"))
        {
            String s1 = jsonarray.getString(1);
            int i2 = jsonarray.getInt(2);
            int j2 = jsonarray.getInt(3);
            readFileAs(jsonarray.getString(0), i2, j2, callbackcontext, s1, 1);
            break MISSING_BLOCK_LABEL_980;
        }
        MalformedURLException malformedurlexception;
        if (s.equals("readAsDataURL"))
        {
            int j1 = jsonarray.getInt(1);
            int k1 = jsonarray.getInt(2);
            readFileAs(jsonarray.getString(0), j1, k1, callbackcontext, null, -1);
            break MISSING_BLOCK_LABEL_980;
        }
        IOException ioexception;
        if (s.equals("readAsArrayBuffer"))
        {
            int k = jsonarray.getInt(1);
            int i1 = jsonarray.getInt(2);
            readFileAs(jsonarray.getString(0), k, i1, callbackcontext, null, 6);
            break MISSING_BLOCK_LABEL_980;
        }
        EncodingException encodingexception;
        if (s.equals("readAsBinaryString"))
        {
            int i = jsonarray.getInt(1);
            int j = jsonarray.getInt(2);
            readFileAs(jsonarray.getString(0), i, j, callbackcontext, null, 7);
            break MISSING_BLOCK_LABEL_980;
        }
        TypeMismatchException typemismatchexception;
        if (s.equals("write"))
        {
            long l2 = write(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getInt(2), jsonarray.getBoolean(3));
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, l2));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("truncate"))
        {
            long l1 = truncateFile(jsonarray.getString(0), jsonarray.getLong(1));
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, l1));
            break MISSING_BLOCK_LABEL_980;
        }
        long l;
        if (!s.equals("requestFileSystem"))
        {
            break MISSING_BLOCK_LABEL_602;
        }
        l = jsonarray.optLong(1);
        if (l == 0L)
        {
            break MISSING_BLOCK_LABEL_586;
        }
        if (l > 1024L * DirectoryManager.getFreeDiskSpace(true))
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, QUOTA_EXCEEDED_ERR));
            break MISSING_BLOCK_LABEL_980;
        }
        try
        {
            callbackcontext.success(requestFileSystem(jsonarray.getInt(0)));
        }
        // Misplaced declaration of an exception variable
        catch (FileNotFoundException filenotfoundexception)
        {
            callbackcontext.error(NOT_FOUND_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (FileExistsException fileexistsexception)
        {
            callbackcontext.error(PATH_EXISTS_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (NoModificationAllowedException nomodificationallowedexception)
        {
            callbackcontext.error(NO_MODIFICATION_ALLOWED_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (InvalidModificationException invalidmodificationexception)
        {
            callbackcontext.error(INVALID_MODIFICATION_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (MalformedURLException malformedurlexception)
        {
            callbackcontext.error(ENCODING_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (IOException ioexception)
        {
            callbackcontext.error(INVALID_MODIFICATION_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (EncodingException encodingexception)
        {
            callbackcontext.error(ENCODING_ERR);
        }
        // Misplaced declaration of an exception variable
        catch (TypeMismatchException typemismatchexception)
        {
            callbackcontext.error(TYPE_MISMATCH_ERR);
        }
        break MISSING_BLOCK_LABEL_980;
        if (s.equals("resolveLocalFileSystemURI"))
        {
            callbackcontext.success(resolveLocalFileSystemURI(jsonarray.getString(0)));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("getMetadata"))
        {
            callbackcontext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, getMetadata(jsonarray.getString(0))));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("getFileMetadata"))
        {
            callbackcontext.success(getFileMetadata(jsonarray.getString(0)));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("getParent"))
        {
            callbackcontext.success(getParent(jsonarray.getString(0)));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("getDirectory"))
        {
            callbackcontext.success(getFile(jsonarray.getString(0), jsonarray.getString(1), jsonarray.optJSONObject(2), true));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("getFile"))
        {
            callbackcontext.success(getFile(jsonarray.getString(0), jsonarray.getString(1), jsonarray.optJSONObject(2), false));
            break MISSING_BLOCK_LABEL_980;
        }
        if (!s.equals("remove"))
        {
            break MISSING_BLOCK_LABEL_839;
        }
        if (remove(jsonarray.getString(0)))
        {
            notifyDelete(jsonarray.getString(0));
            callbackcontext.success();
            break MISSING_BLOCK_LABEL_980;
        }
        callbackcontext.error(NO_MODIFICATION_ALLOWED_ERR);
        break MISSING_BLOCK_LABEL_980;
        if (!s.equals("removeRecursively"))
        {
            break MISSING_BLOCK_LABEL_878;
        }
        if (removeRecursively(jsonarray.getString(0)))
        {
            callbackcontext.success();
            break MISSING_BLOCK_LABEL_980;
        }
        callbackcontext.error(NO_MODIFICATION_ALLOWED_ERR);
        break MISSING_BLOCK_LABEL_980;
        if (s.equals("moveTo"))
        {
            callbackcontext.success(transferTo(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getString(2), true));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("copyTo"))
        {
            callbackcontext.success(transferTo(jsonarray.getString(0), jsonarray.getString(1), jsonarray.getString(2), false));
            break MISSING_BLOCK_LABEL_980;
        }
        if (s.equals("readEntries"))
        {
            callbackcontext.success(readEntries(jsonarray.getString(0)));
            break MISSING_BLOCK_LABEL_980;
        } else
        {
            return false;
        }
        return true;
    }

    public void readFileAs(final String filename, final int start, final int end, final CallbackContext callbackContext, final String encoding, final int resultType)
    {
        cordova.getThreadPool().execute(new Runnable() {

            final FileUtils this$0;
            final CallbackContext val$callbackContext;
            final String val$encoding;
            final int val$end;
            final String val$filename;
            final int val$resultType;
            final int val$start;

            public void run()
            {
                byte abyte0[] = readAsBinaryHelper(filename, start, end);
                resultType;
                JVM INSTR lookupswitch 3: default 60
            //                           1: 149
            //                           6: 177
            //                           7: 194;
                   goto _L1 _L2 _L3 _L4
_L4:
                break MISSING_BLOCK_LABEL_194;
_L1:
                PluginResult pluginresult;
                String s = FileHelper.getMimeType(filename, cordova);
                byte abyte1[] = Base64.encode(abyte0, 0);
                String s1 = (new StringBuilder()).append("data:").append(s).append(";base64,").append(new String(abyte1, "US-ASCII")).toString();
                pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, s1);
_L5:
                callbackContext.sendPluginResult(pluginresult);
                return;
_L2:
                try
                {
                    pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, new String(abyte0, encoding));
                }
                catch (FileNotFoundException filenotfoundexception)
                {
                    callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, FileUtils.NOT_FOUND_ERR));
                    return;
                }
                catch (IOException ioexception)
                {
                    Log.d("FileUtils", ioexception.getLocalizedMessage());
                    callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.IO_EXCEPTION, FileUtils.NOT_READABLE_ERR));
                    return;
                }
                  goto _L5
_L3:
                pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, abyte0);
                  goto _L5
                pluginresult = new PluginResult(org.apache.cordova.api.PluginResult.Status.OK, abyte0, true);
                  goto _L5
            }

            
            {
                this$0 = FileUtils.this;
                filename = s;
                start = i;
                end = j;
                resultType = k;
                encoding = s1;
                callbackContext = callbackcontext;
                super();
            }
        });
    }

    public long write(String s, String s1, int i, boolean flag)
        throws FileNotFoundException, IOException, NoModificationAllowedException
    {
        if (s.startsWith("content://"))
        {
            throw new NoModificationAllowedException("Couldn't write to file given its content URI");
        }
        String s2 = FileHelper.getRealPath(s, cordova);
        boolean flag1 = false;
        if (i > 0)
        {
            truncateFile(s2, i);
            flag1 = true;
        }
        byte abyte0[];
        ByteArrayInputStream bytearrayinputstream;
        FileOutputStream fileoutputstream;
        byte abyte1[];
        if (flag)
        {
            abyte0 = Base64.decode(s1, 0);
        } else
        {
            abyte0 = s1.getBytes();
        }
        bytearrayinputstream = new ByteArrayInputStream(abyte0);
        fileoutputstream = new FileOutputStream(s2, flag1);
        abyte1 = new byte[abyte0.length];
        bytearrayinputstream.read(abyte1, 0, abyte1.length);
        fileoutputstream.write(abyte1, 0, abyte0.length);
        fileoutputstream.flush();
        fileoutputstream.close();
        return (long)abyte0.length;
    }

    static 
    {
        ABORT_ERR = 3;
        ENCODING_ERR = 5;
        INVALID_STATE_ERR = 7;
        INVALID_MODIFICATION_ERR = 9;
        APPLICATION = 3;
    }

}
