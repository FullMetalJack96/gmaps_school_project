// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.util.Log;
import java.util.concurrent.ExecutorService;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            ContactAccessorSdk5, ContactAccessor

public class ContactManager extends CordovaPlugin
{

    public static final int INVALID_ARGUMENT_ERROR = 1;
    public static final int IO_ERROR = 4;
    private static final String LOG_TAG = "Contact Query";
    public static final int NOT_SUPPORTED_ERROR = 5;
    public static final int PENDING_OPERATION_ERROR = 3;
    public static final int PERMISSION_DENIED_ERROR = 20;
    public static final int TIMEOUT_ERROR = 2;
    public static final int UNKNOWN_ERROR;
    private ContactAccessor contactAccessor;

    public ContactManager()
    {
    }

    public boolean execute(String s, JSONArray jsonarray, final CallbackContext callbackContext)
        throws JSONException
    {
        if (android.os.Build.VERSION.RELEASE.startsWith("1."))
        {
            callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, 5));
            return true;
        }
        if (contactAccessor == null)
        {
            contactAccessor = new ContactAccessorSdk5(webView, cordova);
        }
        if (s.equals("search"))
        {
            final JSONArray filter = jsonarray.getJSONArray(0);
            final JSONObject options = jsonarray.getJSONObject(1);
            cordova.getThreadPool().execute(new Runnable() {

                final ContactManager this$0;
                final CallbackContext val$callbackContext;
                final JSONArray val$filter;
                final JSONObject val$options;

                public void run()
                {
                    JSONArray jsonarray1 = contactAccessor.search(filter, options);
                    callbackContext.success(jsonarray1);
                }

            
            {
                this$0 = ContactManager.this;
                filter = jsonarray;
                options = jsonobject;
                callbackContext = callbackcontext;
                super();
            }
            });
            return true;
        }
        if (s.equals("save"))
        {
            final JSONObject contact = jsonarray.getJSONObject(0);
            cordova.getThreadPool().execute(new Runnable() {

                final ContactManager this$0;
                final CallbackContext val$callbackContext;
                final JSONObject val$contact;

                public void run()
                {
                    String s1;
                    JSONObject jsonobject;
                    s1 = contactAccessor.save(contact);
                    jsonobject = null;
                    if (s1 == null)
                    {
                        break MISSING_BLOCK_LABEL_37;
                    }
                    JSONObject jsonobject1 = contactAccessor.getContactById(s1);
                    jsonobject = jsonobject1;
_L1:
                    JSONException jsonexception;
                    if (jsonobject != null)
                    {
                        callbackContext.success(jsonobject);
                        return;
                    } else
                    {
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, 0));
                        return;
                    }
                    jsonexception;
                    Log.e("Contact Query", "JSON fail.", jsonexception);
                    jsonobject = null;
                      goto _L1
                }

            
            {
                this$0 = ContactManager.this;
                contact = jsonobject;
                callbackContext = callbackcontext;
                super();
            }
            });
            return true;
        }
        if (s.equals("remove"))
        {
            final String contactId = jsonarray.getString(0);
            cordova.getThreadPool().execute(new Runnable() {

                final ContactManager this$0;
                final CallbackContext val$callbackContext;
                final String val$contactId;

                public void run()
                {
                    if (contactAccessor.remove(contactId))
                    {
                        callbackContext.success();
                        return;
                    } else
                    {
                        callbackContext.sendPluginResult(new PluginResult(org.apache.cordova.api.PluginResult.Status.ERROR, 0));
                        return;
                    }
                }

            
            {
                this$0 = ContactManager.this;
                contactId = s;
                callbackContext = callbackcontext;
                super();
            }
            });
            return true;
        } else
        {
            return false;
        }
    }

}
