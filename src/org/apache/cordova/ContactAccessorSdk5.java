// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import android.webkit.WebView;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            ContactAccessor

public class ContactAccessorSdk5 extends ContactAccessor
{

    private static final String EMAIL_REGEXP = ".+@.+\\.+.+";
    private static final long MAX_PHOTO_SIZE = 0x100000L;
    private static final Map dbMap;

    public ContactAccessorSdk5(WebView webview, CordovaInterface cordovainterface)
    {
        mApp = cordovainterface;
        mView = webview;
    }

    private JSONObject addressQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("type", getAddressType(cursor.getInt(cursor.getColumnIndex("data2"))));
            jsonobject.put("formatted", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("streetAddress", cursor.getString(cursor.getColumnIndex("data4")));
            jsonobject.put("locality", cursor.getString(cursor.getColumnIndex("data7")));
            jsonobject.put("region", cursor.getString(cursor.getColumnIndex("data8")));
            jsonobject.put("postalCode", cursor.getString(cursor.getColumnIndex("data9")));
            jsonobject.put("country", cursor.getString(cursor.getColumnIndex("data10")));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private ContactAccessor.WhereOptions buildIdClause(Set set, String s)
    {
        ContactAccessor.WhereOptions whereoptions = new ContactAccessor.WhereOptions(this);
        if (s.equals("%"))
        {
            whereoptions.setWhere("(contact_id LIKE ? )");
            whereoptions.setWhereArgs(new String[] {
                s
            });
            return whereoptions;
        }
        Iterator iterator = set.iterator();
        StringBuffer stringbuffer = new StringBuffer("(");
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            stringbuffer.append((new StringBuilder()).append("'").append((String)iterator.next()).append("'").toString());
            if (iterator.hasNext())
            {
                stringbuffer.append(",");
            }
        } while (true);
        stringbuffer.append(")");
        whereoptions.setWhere((new StringBuilder()).append("contact_id IN ").append(stringbuffer.toString()).toString());
        whereoptions.setWhereArgs(null);
        return whereoptions;
    }

    private ContactAccessor.WhereOptions buildWhereClause(JSONArray jsonarray, String s)
    {
        ArrayList arraylist;
        ArrayList arraylist1;
        ContactAccessor.WhereOptions whereoptions;
        int i;
        arraylist = new ArrayList();
        arraylist1 = new ArrayList();
        whereoptions = new ContactAccessor.WhereOptions(this);
        if (isWildCardSearch(jsonarray))
        {
            if ("%".equals(s))
            {
                whereoptions.setWhere("(display_name LIKE ? )");
                whereoptions.setWhereArgs(new String[] {
                    s
                });
                return whereoptions;
            }
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("displayName")).append(" LIKE ? )").toString());
            arraylist1.add(s);
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("name")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/name");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("nickname")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/nickname");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("phoneNumbers")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/phone_v2");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("emails")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/email_v2");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("addresses")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/postal-address_v2");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("ims")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/im");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("organizations")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/organization");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("note")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/note");
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get("urls")).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/website");
        }
        if ("%".equals(s))
        {
            whereoptions.setWhere("(display_name LIKE ? )");
            whereoptions.setWhereArgs(new String[] {
                s
            });
            return whereoptions;
        }
        i = 0;
_L6:
        if (i >= jsonarray.length()) goto _L2; else goto _L1
_L1:
        String s1;
        s1 = jsonarray.getString(i);
        if (s1.equals("id"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" = ? )").toString());
            arraylist1.add(s.substring(1, -1 + s.length()));
            break MISSING_BLOCK_LABEL_1822;
        }
        JSONException jsonexception;
        if (s1.startsWith("displayName"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? )").toString());
            arraylist1.add(s);
            break MISSING_BLOCK_LABEL_1822;
        }
          goto _L3
_L2:
        StringBuffer stringbuffer;
        stringbuffer = new StringBuffer();
        for (int j = 0; j < arraylist.size(); j++)
        {
            stringbuffer.append((String)arraylist.get(j));
            if (j != -1 + arraylist.size())
            {
                stringbuffer.append(" OR ");
            }
        }

        break; /* Loop/switch isn't completed */
_L3:
        if (s1.startsWith("name"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/name");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("nickname"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/nickname");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("phoneNumbers"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/phone_v2");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("emails"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/email_v2");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("addresses"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/postal-address_v2");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("ims"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/im");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("organizations"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/organization");
            break MISSING_BLOCK_LABEL_1822;
        }
        if (s1.startsWith("note"))
        {
            arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
            arraylist1.add(s);
            arraylist1.add("vnd.android.cursor.item/note");
            break MISSING_BLOCK_LABEL_1822;
        }
        try
        {
            if (s1.startsWith("urls"))
            {
                arraylist.add((new StringBuilder()).append("(").append((String)dbMap.get(s1)).append(" LIKE ? AND ").append("mimetype").append(" = ? )").toString());
                arraylist1.add(s);
                arraylist1.add("vnd.android.cursor.item/website");
            }
            break MISSING_BLOCK_LABEL_1822;
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
        }
        if (true) goto _L2; else goto _L4
_L4:
        whereoptions.setWhere(stringbuffer.toString());
        String as[] = new String[arraylist1.size()];
        for (int k = 0; k < arraylist1.size(); k++)
        {
            as[k] = (String)arraylist1.get(k);
        }

        whereoptions.setWhereArgs(as);
        return whereoptions;
        i++;
        if (true) goto _L6; else goto _L5
_L5:
    }

    private String createNewContact(JSONObject jsonobject, String s, String s1)
    {
        ArrayList arraylist;
        arraylist = new ArrayList();
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", s).withValue("account_name", s1).build());
        JSONObject jsonobject1;
        String s7;
        jsonobject1 = jsonobject.optJSONObject("name");
        s7 = jsonobject.getString("displayName");
        JSONArray jsonarray6;
        int l1;
        if (s7 != null || jsonobject1 != null)
        {
            try
            {
                arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", s7).withValue("data3", getJsonString(jsonobject1, "familyName")).withValue("data5", getJsonString(jsonobject1, "middleName")).withValue("data2", getJsonString(jsonobject1, "givenName")).withValue("data4", getJsonString(jsonobject1, "honorificPrefix")).withValue("data6", getJsonString(jsonobject1, "honorificSuffix")).build());
            }
            catch (JSONException jsonexception)
            {
                Log.d("ContactsAccessor", "Could not get name object");
            }
        }
        jsonarray6 = jsonobject.getJSONArray("phoneNumbers");
        if (jsonarray6 == null) goto _L2; else goto _L1
_L1:
        l1 = 0;
_L3:
        if (l1 >= jsonarray6.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertPhone(arraylist, (JSONObject)jsonarray6.get(l1));
        l1++;
        if (true) goto _L3; else goto _L2
        JSONException jsonexception1;
        jsonexception1;
        Log.d("ContactsAccessor", "Could not get phone numbers");
_L2:
        JSONArray jsonarray5 = jsonobject.getJSONArray("emails");
        if (jsonarray5 == null) goto _L5; else goto _L4
_L4:
        int k1 = 0;
_L6:
        if (k1 >= jsonarray5.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertEmail(arraylist, (JSONObject)jsonarray5.get(k1));
        k1++;
        if (true) goto _L6; else goto _L5
        JSONException jsonexception2;
        jsonexception2;
        Log.d("ContactsAccessor", "Could not get emails");
_L5:
        JSONArray jsonarray4 = jsonobject.getJSONArray("addresses");
        if (jsonarray4 == null) goto _L8; else goto _L7
_L7:
        int j1 = 0;
_L9:
        if (j1 >= jsonarray4.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertAddress(arraylist, (JSONObject)jsonarray4.get(j1));
        j1++;
        if (true) goto _L9; else goto _L8
        JSONException jsonexception3;
        jsonexception3;
        Log.d("ContactsAccessor", "Could not get addresses");
_L8:
        JSONArray jsonarray3 = jsonobject.getJSONArray("organizations");
        if (jsonarray3 == null) goto _L11; else goto _L10
_L10:
        int i1 = 0;
_L12:
        if (i1 >= jsonarray3.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertOrganization(arraylist, (JSONObject)jsonarray3.get(i1));
        i1++;
        if (true) goto _L12; else goto _L11
        JSONException jsonexception4;
        jsonexception4;
        Log.d("ContactsAccessor", "Could not get organizations");
_L11:
        JSONArray jsonarray2 = jsonobject.getJSONArray("ims");
        if (jsonarray2 == null) goto _L14; else goto _L13
_L13:
        int l = 0;
_L15:
        if (l >= jsonarray2.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertIm(arraylist, (JSONObject)jsonarray2.get(l));
        l++;
        if (true) goto _L15; else goto _L14
        JSONException jsonexception5;
        jsonexception5;
        Log.d("ContactsAccessor", "Could not get emails");
_L14:
        String s2 = getJsonString(jsonobject, "note");
        if (s2 != null)
        {
            arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/note").withValue("data1", s2).build());
        }
        String s3 = getJsonString(jsonobject, "nickname");
        if (s3 != null)
        {
            arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/nickname").withValue("data1", s3).build());
        }
        JSONArray jsonarray1 = jsonobject.getJSONArray("urls");
        if (jsonarray1 == null) goto _L17; else goto _L16
_L16:
        int k = 0;
_L18:
        if (k >= jsonarray1.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertWebsite(arraylist, (JSONObject)jsonarray1.get(k));
        k++;
        if (true) goto _L18; else goto _L17
        JSONException jsonexception6;
        jsonexception6;
        Log.d("ContactsAccessor", "Could not get websites");
_L17:
        String s4 = getJsonString(jsonobject, "birthday");
        if (s4 != null)
        {
            arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/contact_event").withValue("data2", Integer.valueOf(3)).withValue("data1", s4).build());
        }
        JSONArray jsonarray = jsonobject.getJSONArray("photos");
        if (jsonarray == null) goto _L20; else goto _L19
_L19:
        int j = 0;
_L21:
        if (j >= jsonarray.length())
        {
            break; /* Loop/switch isn't completed */
        }
        insertPhoto(arraylist, (JSONObject)jsonarray.get(j));
        j++;
        if (true) goto _L21; else goto _L20
        JSONException jsonexception7;
        jsonexception7;
        Log.d("ContactsAccessor", "Could not get photos");
_L20:
        ContentProviderResult acontentproviderresult[];
        int i;
        String s5;
        String s6;
        try
        {
            acontentproviderresult = mApp.getActivity().getContentResolver().applyBatch("com.android.contacts", arraylist);
            i = acontentproviderresult.length;
        }
        catch (RemoteException remoteexception)
        {
            Log.e("ContactsAccessor", remoteexception.getMessage(), remoteexception);
            return null;
        }
        catch (OperationApplicationException operationapplicationexception)
        {
            Log.e("ContactsAccessor", operationapplicationexception.getMessage(), operationapplicationexception);
            return null;
        }
        s5 = null;
        if (i < 0)
        {
            break MISSING_BLOCK_LABEL_815;
        }
        s6 = acontentproviderresult[0].uri.getLastPathSegment();
        s5 = s6;
        return s5;
    }

    private JSONObject emailQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("value", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("type", getContactType(cursor.getInt(cursor.getColumnIndex("data2"))));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private int getAddressType(String s)
    {
        byte byte0 = 3;
        if (s != null)
        {
            if ("work".equals(s.toLowerCase()))
            {
                byte0 = 2;
            } else
            {
                if ("other".equals(s.toLowerCase()))
                {
                    return 3;
                }
                if ("home".equals(s.toLowerCase()))
                {
                    return 1;
                }
            }
        }
        return byte0;
    }

    private String getAddressType(int i)
    {
        switch (i)
        {
        default:
            return "other";

        case 1: // '\001'
            return "home";

        case 2: // '\002'
            return "work";
        }
    }

    private int getContactType(String s)
    {
        byte byte0 = 3;
        if (s != null)
        {
            if ("home".equals(s.toLowerCase()))
            {
                byte0 = 1;
            } else
            {
                if ("work".equals(s.toLowerCase()))
                {
                    return 2;
                }
                if ("other".equals(s.toLowerCase()))
                {
                    return 3;
                }
                if ("mobile".equals(s.toLowerCase()))
                {
                    return 4;
                }
                if ("custom".equals(s.toLowerCase()))
                {
                    return 0;
                }
            }
        }
        return byte0;
    }

    private String getContactType(int i)
    {
        switch (i)
        {
        case 3: // '\003'
        default:
            return "other";

        case 0: // '\0'
            return "custom";

        case 1: // '\001'
            return "home";

        case 2: // '\002'
            return "work";

        case 4: // '\004'
            return "mobile";
        }
    }

    private int getImType(String s)
    {
        byte byte0 = -1;
        if (s != null)
        {
            if ("aim".equals(s.toLowerCase()))
            {
                byte0 = 0;
            } else
            {
                if ("google talk".equals(s.toLowerCase()))
                {
                    return 5;
                }
                if ("icq".equals(s.toLowerCase()))
                {
                    return 6;
                }
                if ("jabber".equals(s.toLowerCase()))
                {
                    return 7;
                }
                if ("msn".equals(s.toLowerCase()))
                {
                    return 1;
                }
                if ("netmeeting".equals(s.toLowerCase()))
                {
                    return 8;
                }
                if ("qq".equals(s.toLowerCase()))
                {
                    return 4;
                }
                if ("skype".equals(s.toLowerCase()))
                {
                    return 3;
                }
                if ("yahoo".equals(s.toLowerCase()))
                {
                    return 2;
                }
            }
        }
        return byte0;
    }

    private String getImType(int i)
    {
        switch (i)
        {
        default:
            return "custom";

        case 0: // '\0'
            return "AIM";

        case 5: // '\005'
            return "Google Talk";

        case 6: // '\006'
            return "ICQ";

        case 7: // '\007'
            return "Jabber";

        case 1: // '\001'
            return "MSN";

        case 8: // '\b'
            return "NetMeeting";

        case 4: // '\004'
            return "QQ";

        case 3: // '\003'
            return "Skype";

        case 2: // '\002'
            return "Yahoo";
        }
    }

    private int getOrgType(String s)
    {
        byte byte0 = 2;
        if (s != null)
        {
            if ("work".equals(s.toLowerCase()))
            {
                byte0 = 1;
            } else
            {
                if ("other".equals(s.toLowerCase()))
                {
                    return 2;
                }
                if ("custom".equals(s.toLowerCase()))
                {
                    return 0;
                }
            }
        }
        return byte0;
    }

    private String getOrgType(int i)
    {
        switch (i)
        {
        default:
            return "other";

        case 0: // '\0'
            return "custom";

        case 1: // '\001'
            return "work";
        }
    }

    private InputStream getPathFromUri(String s)
        throws IOException
    {
        if (s.startsWith("content:"))
        {
            Uri uri = Uri.parse(s);
            return mApp.getActivity().getContentResolver().openInputStream(uri);
        }
        if (s.startsWith("http:") || s.startsWith("https:") || s.startsWith("file:"))
        {
            return (new URL(s)).openStream();
        } else
        {
            return new FileInputStream(s);
        }
    }

    private int getPhoneType(String s)
    {
        byte byte0 = 7;
        if (s != null)
        {
            if ("home".equals(s.toLowerCase()))
            {
                byte0 = 1;
            } else
            {
                if ("mobile".equals(s.toLowerCase()))
                {
                    return 2;
                }
                if ("work".equals(s.toLowerCase()))
                {
                    return 3;
                }
                if ("work fax".equals(s.toLowerCase()))
                {
                    return 4;
                }
                if ("home fax".equals(s.toLowerCase()))
                {
                    return 5;
                }
                if ("fax".equals(s.toLowerCase()))
                {
                    return 4;
                }
                if ("pager".equals(s.toLowerCase()))
                {
                    return 6;
                }
                if ("other".equals(s.toLowerCase()))
                {
                    return 7;
                }
                if ("car".equals(s.toLowerCase()))
                {
                    return 9;
                }
                if ("company main".equals(s.toLowerCase()))
                {
                    return 10;
                }
                if ("isdn".equals(s.toLowerCase()))
                {
                    return 11;
                }
                if ("main".equals(s.toLowerCase()))
                {
                    return 12;
                }
                if ("other fax".equals(s.toLowerCase()))
                {
                    return 13;
                }
                if ("radio".equals(s.toLowerCase()))
                {
                    return 14;
                }
                if ("telex".equals(s.toLowerCase()))
                {
                    return 15;
                }
                if ("work mobile".equals(s.toLowerCase()))
                {
                    return 17;
                }
                if ("work pager".equals(s.toLowerCase()))
                {
                    return 18;
                }
                if ("assistant".equals(s.toLowerCase()))
                {
                    return 19;
                }
                if ("mms".equals(s.toLowerCase()))
                {
                    return 20;
                }
                if ("callback".equals(s.toLowerCase()))
                {
                    return 8;
                }
                if ("tty ttd".equals(s.toLowerCase()))
                {
                    return 16;
                }
                if ("custom".equals(s.toLowerCase()))
                {
                    return 0;
                }
            }
        }
        return byte0;
    }

    private String getPhoneType(int i)
    {
        switch (i)
        {
        case 7: // '\007'
        case 12: // '\f'
        default:
            return "other";

        case 0: // '\0'
            return "custom";

        case 5: // '\005'
            return "home fax";

        case 4: // '\004'
            return "work fax";

        case 1: // '\001'
            return "home";

        case 2: // '\002'
            return "mobile";

        case 6: // '\006'
            return "pager";

        case 3: // '\003'
            return "work";

        case 8: // '\b'
            return "callback";

        case 9: // '\t'
            return "car";

        case 10: // '\n'
            return "company main";

        case 13: // '\r'
            return "other fax";

        case 14: // '\016'
            return "radio";

        case 15: // '\017'
            return "telex";

        case 16: // '\020'
            return "tty tdd";

        case 17: // '\021'
            return "work mobile";

        case 18: // '\022'
            return "work pager";

        case 19: // '\023'
            return "assistant";

        case 20: // '\024'
            return "mms";

        case 11: // '\013'
            return "isdn";
        }
    }

    private byte[] getPhotoBytes(String s)
    {
        ByteArrayOutputStream bytearrayoutputstream;
        long l;
        bytearrayoutputstream = new ByteArrayOutputStream();
        l = 0L;
        byte abyte0[];
        InputStream inputstream;
        abyte0 = new byte[8192];
        inputstream = getPathFromUri(s);
_L1:
        int i = inputstream.read(abyte0, 0, abyte0.length);
        if (i == -1 || l > 0x100000L)
        {
            break MISSING_BLOCK_LABEL_69;
        }
        bytearrayoutputstream.write(abyte0, 0, i);
        l += i;
          goto _L1
        try
        {
            inputstream.close();
            bytearrayoutputstream.flush();
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            Log.e("ContactsAccessor", filenotfoundexception.getMessage(), filenotfoundexception);
        }
        catch (IOException ioexception)
        {
            Log.e("ContactsAccessor", ioexception.getMessage(), ioexception);
        }
        return bytearrayoutputstream.toByteArray();
    }

    private JSONObject imQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("value", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("type", getImType(cursor.getString(cursor.getColumnIndex("data5"))));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private void insertAddress(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/postal-address_v2").withValue("data2", Integer.valueOf(getAddressType(getJsonString(jsonobject, "type")))).withValue("data1", getJsonString(jsonobject, "formatted")).withValue("data4", getJsonString(jsonobject, "streetAddress")).withValue("data7", getJsonString(jsonobject, "locality")).withValue("data8", getJsonString(jsonobject, "region")).withValue("data9", getJsonString(jsonobject, "postalCode")).withValue("data10", getJsonString(jsonobject, "country")).build());
    }

    private void insertEmail(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/email_v2").withValue("data1", getJsonString(jsonobject, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(jsonobject, "type")))).build());
    }

    private void insertIm(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/im").withValue("data1", getJsonString(jsonobject, "value")).withValue("data2", Integer.valueOf(getImType(getJsonString(jsonobject, "type")))).build());
    }

    private void insertOrganization(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/organization").withValue("data2", Integer.valueOf(getOrgType(getJsonString(jsonobject, "type")))).withValue("data5", getJsonString(jsonobject, "department")).withValue("data1", getJsonString(jsonobject, "name")).withValue("data4", getJsonString(jsonobject, "title")).build());
    }

    private void insertPhone(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", getJsonString(jsonobject, "value")).withValue("data2", Integer.valueOf(getPhoneType(getJsonString(jsonobject, "type")))).build());
    }

    private void insertPhoto(ArrayList arraylist, JSONObject jsonobject)
    {
        byte abyte0[] = getPhotoBytes(getJsonString(jsonobject, "value"));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("is_super_primary", Integer.valueOf(1)).withValue("mimetype", "vnd.android.cursor.item/photo").withValue("data15", abyte0).build());
    }

    private void insertWebsite(ArrayList arraylist, JSONObject jsonobject)
    {
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/website").withValue("data1", getJsonString(jsonobject, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(jsonobject, "type")))).build());
    }

    private boolean isWildCardSearch(JSONArray jsonarray)
    {
        if (jsonarray.length() == 1)
        {
            boolean flag;
            try
            {
                flag = "*".equals(jsonarray.getString(0));
            }
            catch (JSONException jsonexception)
            {
                return false;
            }
            if (flag)
            {
                return true;
            }
        }
        return false;
    }

    private String modifyContact(String s, JSONObject jsonobject, String s1, String s2)
    {
        int i;
        ArrayList arraylist;
        i = (new Integer(getJsonString(jsonobject, "rawId"))).intValue();
        arraylist = new ArrayList();
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", s1).withValue("account_name", s2).build());
        String s13;
        JSONObject jsonobject8;
        s13 = getJsonString(jsonobject, "displayName");
        jsonobject8 = jsonobject.getJSONObject("name");
        if (s13 == null && jsonobject8 == null)
        {
            break MISSING_BLOCK_LABEL_270;
        }
        android.content.ContentProviderOperation.Builder builder8 = ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("contact_id=? AND mimetype=?", new String[] {
            s, "vnd.android.cursor.item/name"
        });
        if (s13 == null)
        {
            break MISSING_BLOCK_LABEL_129;
        }
        builder8.withValue("data1", s13);
        String s14 = getJsonString(jsonobject8, "familyName");
        if (s14 == null)
        {
            break MISSING_BLOCK_LABEL_155;
        }
        builder8.withValue("data3", s14);
        String s15 = getJsonString(jsonobject8, "middleName");
        if (s15 == null)
        {
            break MISSING_BLOCK_LABEL_181;
        }
        builder8.withValue("data5", s15);
        String s16 = getJsonString(jsonobject8, "givenName");
        if (s16 == null)
        {
            break MISSING_BLOCK_LABEL_207;
        }
        builder8.withValue("data2", s16);
        String s17 = getJsonString(jsonobject8, "honorificPrefix");
        if (s17 == null)
        {
            break MISSING_BLOCK_LABEL_233;
        }
        builder8.withValue("data4", s17);
        String s18 = getJsonString(jsonobject8, "honorificSuffix");
        if (s18 == null)
        {
            break MISSING_BLOCK_LABEL_259;
        }
        builder8.withValue("data6", s18);
        arraylist.add(builder8.build());
_L29:
        JSONArray jsonarray6 = jsonobject.getJSONArray("phoneNumbers");
        if (jsonarray6 == null) goto _L2; else goto _L1
_L1:
        if (jsonarray6.length() != 0) goto _L4; else goto _L3
_L3:
        android.content.ContentProviderOperation.Builder builder7 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as7[] = new String[2];
        as7[0] = (new StringBuilder()).append("").append(i).toString();
        as7[1] = "vnd.android.cursor.item/phone_v2";
        arraylist.add(builder7.withSelection("raw_contact_id=? AND mimetype=?", as7).build());
_L2:
        JSONArray jsonarray5 = jsonobject.getJSONArray("emails");
        if (jsonarray5 == null) goto _L6; else goto _L5
_L5:
        if (jsonarray5.length() != 0) goto _L8; else goto _L7
_L7:
        android.content.ContentProviderOperation.Builder builder6 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as6[] = new String[2];
        as6[0] = (new StringBuilder()).append("").append(i).toString();
        as6[1] = "vnd.android.cursor.item/email_v2";
        arraylist.add(builder6.withSelection("raw_contact_id=? AND mimetype=?", as6).build());
_L6:
        JSONArray jsonarray4 = jsonobject.getJSONArray("addresses");
        if (jsonarray4 == null) goto _L10; else goto _L9
_L9:
        if (jsonarray4.length() != 0) goto _L12; else goto _L11
_L11:
        android.content.ContentProviderOperation.Builder builder5 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as5[] = new String[2];
        as5[0] = (new StringBuilder()).append("").append(i).toString();
        as5[1] = "vnd.android.cursor.item/postal-address_v2";
        arraylist.add(builder5.withSelection("raw_contact_id=? AND mimetype=?", as5).build());
_L10:
        JSONArray jsonarray3 = jsonobject.getJSONArray("organizations");
        if (jsonarray3 == null) goto _L14; else goto _L13
_L13:
        if (jsonarray3.length() != 0) goto _L16; else goto _L15
_L15:
        android.content.ContentProviderOperation.Builder builder4 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as4[] = new String[2];
        as4[0] = (new StringBuilder()).append("").append(i).toString();
        as4[1] = "vnd.android.cursor.item/organization";
        arraylist.add(builder4.withSelection("raw_contact_id=? AND mimetype=?", as4).build());
_L14:
        JSONArray jsonarray2 = jsonobject.getJSONArray("ims");
        if (jsonarray2 == null) goto _L18; else goto _L17
_L17:
        if (jsonarray2.length() != 0) goto _L20; else goto _L19
_L19:
        android.content.ContentProviderOperation.Builder builder3 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as3[] = new String[2];
        as3[0] = (new StringBuilder()).append("").append(i).toString();
        as3[1] = "vnd.android.cursor.item/im";
        arraylist.add(builder3.withSelection("raw_contact_id=? AND mimetype=?", as3).build());
_L18:
        String s3 = getJsonString(jsonobject, "note");
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("contact_id=? AND mimetype=?", new String[] {
            s, "vnd.android.cursor.item/note"
        }).withValue("data1", s3).build());
        String s4 = getJsonString(jsonobject, "nickname");
        if (s4 != null)
        {
            arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("contact_id=? AND mimetype=?", new String[] {
                s, "vnd.android.cursor.item/nickname"
            }).withValue("data1", s4).build());
        }
        JSONArray jsonarray1 = jsonobject.getJSONArray("urls");
        if (jsonarray1 == null) goto _L22; else goto _L21
_L21:
        if (jsonarray1.length() != 0) goto _L24; else goto _L23
_L23:
        Log.d("ContactsAccessor", "This means we should be deleting all the phone numbers.");
        android.content.ContentProviderOperation.Builder builder2 = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as2[] = new String[2];
        as2[0] = (new StringBuilder()).append("").append(i).toString();
        as2[1] = "vnd.android.cursor.item/website";
        arraylist.add(builder2.withSelection("raw_contact_id=? AND mimetype=?", as2).build());
_L22:
        String s5 = getJsonString(jsonobject, "birthday");
        if (s5 != null)
        {
            android.content.ContentProviderOperation.Builder builder1 = ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI);
            String as1[] = new String[3];
            as1[0] = s;
            as1[1] = "vnd.android.cursor.item/contact_event";
            as1[2] = new String("3");
            arraylist.add(builder1.withSelection("contact_id=? AND mimetype=? AND data2=?", as1).withValue("data2", Integer.valueOf(3)).withValue("data1", s5).build());
        }
        JSONArray jsonarray = jsonobject.getJSONArray("photos");
        if (jsonarray == null) goto _L26; else goto _L25
_L25:
        if (jsonarray.length() != 0) goto _L28; else goto _L27
_L27:
        android.content.ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(android.provider.ContactsContract.Data.CONTENT_URI);
        String as[] = new String[2];
        as[0] = (new StringBuilder()).append("").append(i).toString();
        as[1] = "vnd.android.cursor.item/photo";
        arraylist.add(builder.withSelection("raw_contact_id=? AND mimetype=?", as).build());
_L26:
        int j;
        int l;
        int j1;
        int l1;
        int j2;
        int l2;
        int j3;
        boolean flag = true;
        JSONException jsonexception;
        JSONException jsonexception1;
        JSONException jsonexception2;
        JSONException jsonexception3;
        JSONException jsonexception4;
        JSONException jsonexception5;
        JSONException jsonexception6;
        JSONException jsonexception7;
        int k;
        JSONObject jsonobject1;
        String s6;
        byte abyte0[];
        ContentValues contentvalues;
        int i1;
        JSONObject jsonobject2;
        String s7;
        ContentValues contentvalues1;
        int k1;
        JSONObject jsonobject3;
        String s8;
        ContentValues contentvalues2;
        int i2;
        JSONObject jsonobject4;
        String s9;
        ContentValues contentvalues3;
        int k2;
        JSONObject jsonobject5;
        String s10;
        ContentValues contentvalues4;
        int i3;
        JSONObject jsonobject6;
        String s11;
        ContentValues contentvalues5;
        int k3;
        JSONObject jsonobject7;
        String s12;
        ContentValues contentvalues6;
        try
        {
            mApp.getActivity().getContentResolver().applyBatch("com.android.contacts", arraylist);
        }
        catch (RemoteException remoteexception)
        {
            Log.e("ContactsAccessor", remoteexception.getMessage(), remoteexception);
            Log.e("ContactsAccessor", Log.getStackTraceString(remoteexception), remoteexception);
            flag = false;
        }
        catch (OperationApplicationException operationapplicationexception)
        {
            Log.e("ContactsAccessor", operationapplicationexception.getMessage(), operationapplicationexception);
            Log.e("ContactsAccessor", Log.getStackTraceString(operationapplicationexception), operationapplicationexception);
            flag = false;
        }
        if (flag)
        {
            return s;
        } else
        {
            return null;
        }
        jsonexception;
        Log.d("ContactsAccessor", "Could not get name");
          goto _L29
_L4:
        j3 = 0;
_L55:
        k3 = jsonarray6.length();
        if (j3 >= k3) goto _L2; else goto _L30
_L30:
        jsonobject7 = (JSONObject)jsonarray6.get(j3);
        s12 = getJsonString(jsonobject7, "id");
        if (s12 != null) goto _L32; else goto _L31
_L31:
        contentvalues6 = new ContentValues();
        contentvalues6.put("raw_contact_id", Integer.valueOf(i));
        contentvalues6.put("mimetype", "vnd.android.cursor.item/phone_v2");
        contentvalues6.put("data1", getJsonString(jsonobject7, "value"));
        contentvalues6.put("data2", Integer.valueOf(getPhoneType(getJsonString(jsonobject7, "type"))));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues6).build());
          goto _L33
_L32:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s12, "vnd.android.cursor.item/phone_v2"
        }).withValue("data1", getJsonString(jsonobject7, "value")).withValue("data2", Integer.valueOf(getPhoneType(getJsonString(jsonobject7, "type")))).build());
          goto _L33
        jsonexception1;
        Log.d("ContactsAccessor", "Could not get phone numbers");
          goto _L2
_L8:
        l2 = 0;
_L56:
        i3 = jsonarray5.length();
        if (l2 >= i3) goto _L6; else goto _L34
_L34:
        jsonobject6 = (JSONObject)jsonarray5.get(l2);
        s11 = getJsonString(jsonobject6, "id");
        if (s11 != null) goto _L36; else goto _L35
_L35:
        contentvalues5 = new ContentValues();
        contentvalues5.put("raw_contact_id", Integer.valueOf(i));
        contentvalues5.put("mimetype", "vnd.android.cursor.item/email_v2");
        contentvalues5.put("data1", getJsonString(jsonobject6, "value"));
        contentvalues5.put("data2", Integer.valueOf(getContactType(getJsonString(jsonobject6, "type"))));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues5).build());
          goto _L37
_L36:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s11, "vnd.android.cursor.item/email_v2"
        }).withValue("data1", getJsonString(jsonobject6, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(jsonobject6, "type")))).build());
          goto _L37
        jsonexception2;
        Log.d("ContactsAccessor", "Could not get emails");
          goto _L6
_L12:
        j2 = 0;
_L57:
        k2 = jsonarray4.length();
        if (j2 >= k2) goto _L10; else goto _L38
_L38:
        jsonobject5 = (JSONObject)jsonarray4.get(j2);
        s10 = getJsonString(jsonobject5, "id");
        if (s10 != null) goto _L40; else goto _L39
_L39:
        contentvalues4 = new ContentValues();
        contentvalues4.put("raw_contact_id", Integer.valueOf(i));
        contentvalues4.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
        contentvalues4.put("data2", Integer.valueOf(getAddressType(getJsonString(jsonobject5, "type"))));
        contentvalues4.put("data1", getJsonString(jsonobject5, "formatted"));
        contentvalues4.put("data4", getJsonString(jsonobject5, "streetAddress"));
        contentvalues4.put("data7", getJsonString(jsonobject5, "locality"));
        contentvalues4.put("data8", getJsonString(jsonobject5, "region"));
        contentvalues4.put("data9", getJsonString(jsonobject5, "postalCode"));
        contentvalues4.put("data10", getJsonString(jsonobject5, "country"));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues4).build());
          goto _L41
_L40:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s10, "vnd.android.cursor.item/postal-address_v2"
        }).withValue("data2", Integer.valueOf(getAddressType(getJsonString(jsonobject5, "type")))).withValue("data1", getJsonString(jsonobject5, "formatted")).withValue("data4", getJsonString(jsonobject5, "streetAddress")).withValue("data7", getJsonString(jsonobject5, "locality")).withValue("data8", getJsonString(jsonobject5, "region")).withValue("data9", getJsonString(jsonobject5, "postalCode")).withValue("data10", getJsonString(jsonobject5, "country")).build());
          goto _L41
        jsonexception3;
        Log.d("ContactsAccessor", "Could not get addresses");
          goto _L10
_L16:
        l1 = 0;
_L58:
        i2 = jsonarray3.length();
        if (l1 >= i2) goto _L14; else goto _L42
_L42:
        jsonobject4 = (JSONObject)jsonarray3.get(l1);
        s9 = getJsonString(jsonobject4, "id");
        if (s9 != null) goto _L44; else goto _L43
_L43:
        contentvalues3 = new ContentValues();
        contentvalues3.put("raw_contact_id", Integer.valueOf(i));
        contentvalues3.put("mimetype", "vnd.android.cursor.item/organization");
        contentvalues3.put("data2", Integer.valueOf(getOrgType(getJsonString(jsonobject4, "type"))));
        contentvalues3.put("data5", getJsonString(jsonobject4, "department"));
        contentvalues3.put("data1", getJsonString(jsonobject4, "name"));
        contentvalues3.put("data4", getJsonString(jsonobject4, "title"));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues3).build());
          goto _L45
_L44:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s9, "vnd.android.cursor.item/organization"
        }).withValue("data2", Integer.valueOf(getOrgType(getJsonString(jsonobject4, "type")))).withValue("data5", getJsonString(jsonobject4, "department")).withValue("data1", getJsonString(jsonobject4, "name")).withValue("data4", getJsonString(jsonobject4, "title")).build());
          goto _L45
        jsonexception4;
        Log.d("ContactsAccessor", "Could not get organizations");
          goto _L14
_L20:
        j1 = 0;
_L59:
        k1 = jsonarray2.length();
        if (j1 >= k1) goto _L18; else goto _L46
_L46:
        jsonobject3 = (JSONObject)jsonarray2.get(j1);
        s8 = getJsonString(jsonobject3, "id");
        if (s8 != null) goto _L48; else goto _L47
_L47:
        contentvalues2 = new ContentValues();
        contentvalues2.put("raw_contact_id", Integer.valueOf(i));
        contentvalues2.put("mimetype", "vnd.android.cursor.item/im");
        contentvalues2.put("data1", getJsonString(jsonobject3, "value"));
        contentvalues2.put("data2", Integer.valueOf(getImType(getJsonString(jsonobject3, "type"))));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues2).build());
          goto _L49
_L48:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s8, "vnd.android.cursor.item/im"
        }).withValue("data1", getJsonString(jsonobject3, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(jsonobject3, "type")))).build());
          goto _L49
        jsonexception5;
        Log.d("ContactsAccessor", "Could not get emails");
          goto _L18
_L24:
        l = 0;
_L60:
        i1 = jsonarray1.length();
        if (l >= i1) goto _L22; else goto _L50
_L50:
        jsonobject2 = (JSONObject)jsonarray1.get(l);
        s7 = getJsonString(jsonobject2, "id");
        if (s7 != null) goto _L52; else goto _L51
_L51:
        contentvalues1 = new ContentValues();
        contentvalues1.put("raw_contact_id", Integer.valueOf(i));
        contentvalues1.put("mimetype", "vnd.android.cursor.item/website");
        contentvalues1.put("data1", getJsonString(jsonobject2, "value"));
        contentvalues1.put("data2", Integer.valueOf(getContactType(getJsonString(jsonobject2, "type"))));
        arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues1).build());
          goto _L53
_L52:
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s7, "vnd.android.cursor.item/website"
        }).withValue("data1", getJsonString(jsonobject2, "value")).withValue("data2", Integer.valueOf(getContactType(getJsonString(jsonobject2, "type")))).build());
          goto _L53
        jsonexception6;
        Log.d("ContactsAccessor", "Could not get websites");
          goto _L22
_L28:
        j = 0;
_L61:
        k = jsonarray.length();
        if (j >= k) goto _L26; else goto _L54
_L54:
        jsonobject1 = (JSONObject)jsonarray.get(j);
        s6 = getJsonString(jsonobject1, "id");
        abyte0 = getPhotoBytes(getJsonString(jsonobject1, "value"));
label0:
        {
            if (s6 != null)
            {
                break label0;
            }
            try
            {
                contentvalues = new ContentValues();
                contentvalues.put("raw_contact_id", Integer.valueOf(i));
                contentvalues.put("mimetype", "vnd.android.cursor.item/photo");
                contentvalues.put("is_super_primary", Integer.valueOf(1));
                contentvalues.put("data15", abyte0);
                arraylist.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI).withValues(contentvalues).build());
                break MISSING_BLOCK_LABEL_2979;
            }
            // Misplaced declaration of an exception variable
            catch (JSONException jsonexception7)
            {
                Log.d("ContactsAccessor", "Could not get photos");
            }
        }
          goto _L26
        arraylist.add(ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI).withSelection("_id=? AND mimetype=?", new String[] {
            s6, "vnd.android.cursor.item/photo"
        }).withValue("is_super_primary", Integer.valueOf(1)).withValue("data15", abyte0).build());
        break MISSING_BLOCK_LABEL_2979;
_L33:
        j3++;
          goto _L55
_L37:
        l2++;
          goto _L56
_L41:
        j2++;
          goto _L57
_L45:
        l1++;
          goto _L58
_L49:
        j1++;
          goto _L59
_L53:
        l++;
          goto _L60
        j++;
          goto _L61
    }

    private JSONObject nameQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        String s;
        String s1;
        String s2;
        String s3;
        String s4;
        StringBuffer stringbuffer;
        try
        {
            s = cursor.getString(cursor.getColumnIndex("data3"));
            s1 = cursor.getString(cursor.getColumnIndex("data2"));
            s2 = cursor.getString(cursor.getColumnIndex("data5"));
            s3 = cursor.getString(cursor.getColumnIndex("data4"));
            s4 = cursor.getString(cursor.getColumnIndex("data6"));
            stringbuffer = new StringBuffer("");
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        if (s3 == null)
        {
            break MISSING_BLOCK_LABEL_132;
        }
        stringbuffer.append((new StringBuilder()).append(s3).append(" ").toString());
        if (s1 == null)
        {
            break MISSING_BLOCK_LABEL_164;
        }
        stringbuffer.append((new StringBuilder()).append(s1).append(" ").toString());
        if (s2 == null)
        {
            break MISSING_BLOCK_LABEL_196;
        }
        stringbuffer.append((new StringBuilder()).append(s2).append(" ").toString());
        if (s == null)
        {
            break MISSING_BLOCK_LABEL_209;
        }
        stringbuffer.append(s);
        if (s4 == null)
        {
            break MISSING_BLOCK_LABEL_241;
        }
        stringbuffer.append((new StringBuilder()).append(" ").append(s4).toString());
        jsonobject.put("familyName", s);
        jsonobject.put("givenName", s1);
        jsonobject.put("middleName", s2);
        jsonobject.put("honorificPrefix", s3);
        jsonobject.put("honorificSuffix", s4);
        jsonobject.put("formatted", stringbuffer);
        return jsonobject;
    }

    private JSONObject organizationQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("type", getOrgType(cursor.getInt(cursor.getColumnIndex("data2"))));
            jsonobject.put("department", cursor.getString(cursor.getColumnIndex("data5")));
            jsonobject.put("name", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("title", cursor.getString(cursor.getColumnIndex("data4")));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private JSONObject phoneQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("value", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("type", getPhoneType(cursor.getInt(cursor.getColumnIndex("data2"))));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        catch (Exception exception)
        {
            Log.e("ContactsAccessor", exception.getMessage(), exception);
            return jsonobject;
        }
        return jsonobject;
    }

    private JSONObject photoQuery(Cursor cursor, String s)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("type", "url");
            jsonobject.put("value", Uri.withAppendedPath(ContentUris.withAppendedId(android.provider.ContactsContract.Contacts.CONTENT_URI, (new Long(s)).longValue()), "photo").toString());
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private JSONObject populateContact(JSONObject jsonobject, JSONArray jsonarray, JSONArray jsonarray1, JSONArray jsonarray2, JSONArray jsonarray3, JSONArray jsonarray4, JSONArray jsonarray5, 
            JSONArray jsonarray6)
    {
        try
        {
            if (jsonarray.length() > 0)
            {
                jsonobject.put("organizations", jsonarray);
            }
            if (jsonarray1.length() > 0)
            {
                jsonobject.put("addresses", jsonarray1);
            }
            if (jsonarray2.length() > 0)
            {
                jsonobject.put("phoneNumbers", jsonarray2);
            }
            if (jsonarray3.length() > 0)
            {
                jsonobject.put("emails", jsonarray3);
            }
            if (jsonarray4.length() > 0)
            {
                jsonobject.put("ims", jsonarray4);
            }
            if (jsonarray5.length() > 0)
            {
                jsonobject.put("urls", jsonarray5);
            }
            if (jsonarray6.length() > 0)
            {
                jsonobject.put("photos", jsonarray6);
            }
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    private JSONArray populateContactArray(int i, HashMap hashmap, Cursor cursor)
    {
        String s;
        String s1;
        boolean flag;
        JSONArray jsonarray;
        JSONObject jsonobject;
        JSONArray jsonarray1;
        JSONArray jsonarray2;
        JSONArray jsonarray3;
        JSONArray jsonarray4;
        JSONArray jsonarray5;
        JSONArray jsonarray6;
        JSONArray jsonarray7;
        int j;
        int k;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        s = "";
        s1 = "";
        flag = true;
        jsonarray = new JSONArray();
        jsonobject = new JSONObject();
        jsonarray1 = new JSONArray();
        jsonarray2 = new JSONArray();
        jsonarray3 = new JSONArray();
        jsonarray4 = new JSONArray();
        jsonarray5 = new JSONArray();
        jsonarray6 = new JSONArray();
        jsonarray7 = new JSONArray();
        j = cursor.getColumnIndex("contact_id");
        k = cursor.getColumnIndex("raw_contact_id");
        l = cursor.getColumnIndex("mimetype");
        i1 = cursor.getColumnIndex("data1");
        j1 = cursor.getColumnIndex("data1");
        k1 = cursor.getColumnIndex("data1");
        l1 = cursor.getColumnIndex("data1");
        i2 = cursor.getColumnIndex("data2");
        if (cursor.getCount() <= 0) goto _L2; else goto _L1
_L1:
        if (!cursor.moveToNext() || jsonarray.length() > i - 1) goto _L4; else goto _L3
_L3:
        String s2;
        s = cursor.getString(j);
        s2 = cursor.getString(k);
        if (cursor.getPosition() == 0)
        {
            s1 = s;
        }
        JSONObject jsonobject1;
        if (s1.equals(s))
        {
            break MISSING_BLOCK_LABEL_381;
        }
        jsonarray.put(populateContact(jsonobject, jsonarray1, jsonarray2, jsonarray3, jsonarray4, jsonarray5, jsonarray6, jsonarray7));
        jsonobject1 = new JSONObject();
        JSONException jsonexception;
        JSONArray jsonarray8;
        JSONArray jsonarray9;
        JSONArray jsonarray10;
        JSONArray jsonarray11;
        JSONArray jsonarray12;
        JSONArray jsonarray13;
        String s3;
        JSONArray jsonarray14;
        try
        {
            jsonarray8 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            jsonarray9 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonarray1 = jsonarray8;
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            jsonarray10 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonarray2 = jsonarray9;
            jsonarray1 = jsonarray8;
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            jsonarray11 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonarray3 = jsonarray10;
            jsonarray2 = jsonarray9;
            jsonarray1 = jsonarray8;
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            jsonarray12 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonarray4 = jsonarray11;
            jsonarray3 = jsonarray10;
            jsonarray2 = jsonarray9;
            jsonarray1 = jsonarray8;
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            jsonarray13 = new JSONArray();
        }
        // Misplaced declaration of an exception variable
        catch (JSONException jsonexception)
        {
            jsonarray5 = jsonarray12;
            jsonarray4 = jsonarray11;
            jsonarray3 = jsonarray10;
            jsonarray2 = jsonarray9;
            jsonarray1 = jsonarray8;
            jsonobject = jsonobject1;
            continue; /* Loop/switch isn't completed */
        }
        jsonarray14 = new JSONArray();
        flag = true;
        jsonarray7 = jsonarray14;
        jsonarray6 = jsonarray13;
        jsonarray5 = jsonarray12;
        jsonarray4 = jsonarray11;
        jsonarray3 = jsonarray10;
        jsonarray2 = jsonarray9;
        jsonarray1 = jsonarray8;
        jsonobject = jsonobject1;
        if (!flag)
        {
            break MISSING_BLOCK_LABEL_410;
        }
        flag = false;
        jsonobject.put("id", s);
        jsonobject.put("rawId", s2);
        s3 = cursor.getString(l);
        if (s3.equals("vnd.android.cursor.item/name"))
        {
            jsonobject.put("displayName", cursor.getString(i1));
        }
        if (!s3.equals("vnd.android.cursor.item/name") || !isRequired("name", hashmap)) goto _L6; else goto _L5
_L5:
        jsonobject.put("name", nameQuery(cursor));
          goto _L7
_L6:
        if (!s3.equals("vnd.android.cursor.item/phone_v2") || !isRequired("phoneNumbers", hashmap)) goto _L9; else goto _L8
_L8:
        jsonarray3.put(phoneQuery(cursor));
          goto _L7
        jsonexception;
_L10:
        Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
        break; /* Loop/switch isn't completed */
_L9:
        if (s3.equals("vnd.android.cursor.item/email_v2") && isRequired("emails", hashmap))
        {
            jsonarray4.put(emailQuery(cursor));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/postal-address_v2") && isRequired("addresses", hashmap))
        {
            jsonarray2.put(addressQuery(cursor));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/organization") && isRequired("organizations", hashmap))
        {
            jsonarray1.put(organizationQuery(cursor));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/im") && isRequired("ims", hashmap))
        {
            jsonarray5.put(imQuery(cursor));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/note") && isRequired("note", hashmap))
        {
            jsonobject.put("note", cursor.getString(j1));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/nickname") && isRequired("nickname", hashmap))
        {
            jsonobject.put("nickname", cursor.getString(k1));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/website") && isRequired("urls", hashmap))
        {
            jsonarray6.put(websiteQuery(cursor));
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/contact_event"))
        {
            if (isRequired("birthday", hashmap) && 3 == cursor.getInt(i2))
            {
                jsonobject.put("birthday", cursor.getString(l1));
            }
            break; /* Loop/switch isn't completed */
        }
        if (s3.equals("vnd.android.cursor.item/photo") && isRequired("photos", hashmap))
        {
            jsonarray7.put(photoQuery(cursor, s));
        }
        break; /* Loop/switch isn't completed */
_L4:
        if (jsonarray.length() < i)
        {
            jsonarray.put(populateContact(jsonobject, jsonarray1, jsonarray2, jsonarray3, jsonarray4, jsonarray5, jsonarray6, jsonarray7));
        }
_L2:
        cursor.close();
        return jsonarray;
        jsonexception;
        jsonarray6 = jsonarray13;
        jsonarray5 = jsonarray12;
        jsonarray4 = jsonarray11;
        jsonarray3 = jsonarray10;
        jsonarray2 = jsonarray9;
        jsonarray1 = jsonarray8;
        jsonobject = jsonobject1;
        if (true) goto _L10; else goto _L7
_L7:
        s1 = s;
        if (true) goto _L1; else goto _L11
_L11:
    }

    private JSONObject websiteQuery(Cursor cursor)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("id", cursor.getString(cursor.getColumnIndex("_id")));
            jsonobject.put("pref", false);
            jsonobject.put("value", cursor.getString(cursor.getColumnIndex("data1")));
            jsonobject.put("type", getContactType(cursor.getInt(cursor.getColumnIndex("data2"))));
        }
        catch (JSONException jsonexception)
        {
            Log.e("ContactsAccessor", jsonexception.getMessage(), jsonexception);
            return jsonobject;
        }
        return jsonobject;
    }

    public JSONObject getContactById(String s)
        throws JSONException
    {
        Cursor cursor = mApp.getActivity().getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, null, "contact_id = ? ", new String[] {
            s
        }, "contact_id ASC");
        JSONArray jsonarray = new JSONArray();
        jsonarray.put("*");
        JSONArray jsonarray1 = populateContactArray(1, buildPopulationSet(jsonarray), cursor);
        int i = jsonarray1.length();
        JSONObject jsonobject = null;
        if (i == 1)
        {
            jsonobject = jsonarray1.getJSONObject(0);
        }
        return jsonobject;
    }

    public boolean remove(String s)
    {
        Cursor cursor = mApp.getActivity().getContentResolver().query(android.provider.ContactsContract.Contacts.CONTENT_URI, null, "_id = ?", new String[] {
            s
        }, null);
        int i;
        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            String s1 = cursor.getString(cursor.getColumnIndex("lookup"));
            Uri uri = Uri.withAppendedPath(android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI, s1);
            i = mApp.getActivity().getContentResolver().delete(uri, null, null);
        } else
        {
            Log.d("ContactsAccessor", "Could not find contact with ID");
            i = 0;
        }
        return i > 0;
    }

    public String save(JSONObject jsonobject)
    {
        Account aaccount[] = AccountManager.get(mApp.getActivity()).getAccounts();
        if (aaccount.length != 1) goto _L2; else goto _L1
_L1:
        String s;
        String s1;
        s = aaccount[0].name;
        s1 = aaccount[0].type;
_L4:
        String s2 = getJsonString(jsonobject, "id");
        int i;
        int j;
        int k;
        int l;
        int i1;
        Account account;
        int j1;
        int k1;
        Account account1;
        Account account2;
        if (s2 == null)
        {
            return createNewContact(jsonobject, s1, s);
        } else
        {
            return modifyContact(s2, jsonobject, s1, s);
        }
_L2:
        i = aaccount.length;
        s = null;
        s1 = null;
        if (i <= 1) goto _L4; else goto _L3
_L3:
        j = aaccount.length;
        k = 0;
_L15:
        s = null;
        s1 = null;
        if (k >= j) goto _L6; else goto _L5
_L5:
        account2 = aaccount[k];
        if (!account2.type.contains("eas") || !account2.name.matches(".+@.+\\.+.+")) goto _L8; else goto _L7
_L7:
        s = account2.name;
        s1 = account2.type;
_L6:
        if (s != null) goto _L10; else goto _L9
_L9:
        j1 = aaccount.length;
        k1 = 0;
_L16:
        if (k1 >= j1) goto _L10; else goto _L11
_L11:
        account1 = aaccount[k1];
        if (!account1.type.contains("com.google") || !account1.name.matches(".+@.+\\.+.+")) goto _L13; else goto _L12
_L12:
        s = account1.name;
        s1 = account1.type;
_L10:
        if (s != null) goto _L4; else goto _L14
_L14:
        l = aaccount.length;
        i1 = 0;
_L17:
        if (i1 < l)
        {
            account = aaccount[i1];
            if (!account.name.matches(".+@.+\\.+.+"))
            {
                break MISSING_BLOCK_LABEL_276;
            }
            s = account.name;
            s1 = account.type;
        }
          goto _L4
_L8:
        k++;
          goto _L15
_L13:
        k1++;
          goto _L16
        i1++;
          goto _L17
    }

    public JSONArray search(JSONArray jsonarray, JSONObject jsonobject)
    {
        int i = 0x7fffffff;
        if (jsonobject == null) goto _L2; else goto _L1
_L1:
        String s;
        String s1 = jsonobject.optString("filter");
        ContactAccessor.WhereOptions whereoptions;
        int j;
        boolean flag;
        if (s1.length() == 0)
        {
            s = "%";
        } else
        {
            s = (new StringBuilder()).append("%").append(s1).append("%").toString();
        }
        flag = jsonobject.getBoolean("multiple");
        if (!flag)
        {
            i = 1;
        }
        break MISSING_BLOCK_LABEL_45;
_L2:
        s = "%";
_L4:
        HashMap hashmap = buildPopulationSet(jsonarray);
        whereoptions = buildWhereClause(jsonarray, s);
        Cursor cursor = mApp.getActivity().getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, new String[] {
            "contact_id"
        }, whereoptions.getWhere(), whereoptions.getWhereArgs(), "contact_id ASC");
        HashSet hashset = new HashSet();
        j = -1;
        for (; cursor.moveToNext(); hashset.add(cursor.getString(j)))
        {
            if (j < 0)
            {
                j = cursor.getColumnIndex("contact_id");
            }
        }

        cursor.close();
        ContactAccessor.WhereOptions whereoptions1 = buildIdClause(hashset, s);
        HashSet hashset1 = new HashSet();
        hashset1.add("contact_id");
        hashset1.add("raw_contact_id");
        hashset1.add("mimetype");
        if (isRequired("displayName", hashmap))
        {
            hashset1.add("data1");
        }
        if (isRequired("name", hashmap))
        {
            hashset1.add("data3");
            hashset1.add("data2");
            hashset1.add("data5");
            hashset1.add("data4");
            hashset1.add("data6");
        }
        if (isRequired("phoneNumbers", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data1");
            hashset1.add("data2");
        }
        if (isRequired("emails", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data1");
            hashset1.add("data2");
        }
        if (isRequired("addresses", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data2");
            hashset1.add("data1");
            hashset1.add("data4");
            hashset1.add("data7");
            hashset1.add("data8");
            hashset1.add("data9");
            hashset1.add("data10");
        }
        if (isRequired("organizations", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data2");
            hashset1.add("data5");
            hashset1.add("data1");
            hashset1.add("data4");
        }
        if (isRequired("ims", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data1");
            hashset1.add("data2");
        }
        if (isRequired("note", hashmap))
        {
            hashset1.add("data1");
        }
        if (isRequired("nickname", hashmap))
        {
            hashset1.add("data1");
        }
        if (isRequired("urls", hashmap))
        {
            hashset1.add("_id");
            hashset1.add("data1");
            hashset1.add("data2");
        }
        if (isRequired("birthday", hashmap))
        {
            hashset1.add("data1");
            hashset1.add("data2");
        }
        if (isRequired("photos", hashmap))
        {
            hashset1.add("_id");
        }
        Cursor cursor1 = mApp.getActivity().getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, (String[])hashset1.toArray(new String[0]), whereoptions1.getWhere(), whereoptions1.getWhereArgs(), "contact_id ASC");
        return populateContactArray(i, hashmap, cursor1);
        JSONException jsonexception;
        jsonexception;
        if (true) goto _L4; else goto _L3
_L3:
    }

    static 
    {
        dbMap = new HashMap();
        dbMap.put("id", "contact_id");
        dbMap.put("displayName", "display_name");
        dbMap.put("name", "data1");
        dbMap.put("name.formatted", "data1");
        dbMap.put("name.familyName", "data3");
        dbMap.put("name.givenName", "data2");
        dbMap.put("name.middleName", "data5");
        dbMap.put("name.honorificPrefix", "data4");
        dbMap.put("name.honorificSuffix", "data6");
        dbMap.put("nickname", "data1");
        dbMap.put("phoneNumbers", "data1");
        dbMap.put("phoneNumbers.value", "data1");
        dbMap.put("emails", "data1");
        dbMap.put("emails.value", "data1");
        dbMap.put("addresses", "data1");
        dbMap.put("addresses.formatted", "data1");
        dbMap.put("addresses.streetAddress", "data4");
        dbMap.put("addresses.locality", "data7");
        dbMap.put("addresses.region", "data8");
        dbMap.put("addresses.postalCode", "data9");
        dbMap.put("addresses.country", "data10");
        dbMap.put("ims", "data1");
        dbMap.put("ims.value", "data1");
        dbMap.put("organizations", "data1");
        dbMap.put("organizations.name", "data1");
        dbMap.put("organizations.department", "data5");
        dbMap.put("organizations.title", "data4");
        dbMap.put("birthday", "vnd.android.cursor.item/contact_event");
        dbMap.put("note", "data1");
        dbMap.put("photos.value", "vnd.android.cursor.item/photo");
        dbMap.put("urls", "data1");
        dbMap.put("urls.value", "data1");
    }
}
