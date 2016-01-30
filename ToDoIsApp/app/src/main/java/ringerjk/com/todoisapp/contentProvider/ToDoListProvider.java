package ringerjk.com.todoisapp.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


public class ToDoListProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";





    static final String AUTHORITY = "ringerjk.com.todoisapp.contentProvider.ToDoList";

    static final String NOTE_PATH = "notes";
    static final String PICTURES_PATH = "pictures";

    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);
    static final String NOTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + NOTE_PATH;
    static final String NOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + NOTE_PATH;

    public static final Uri PICTURE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PICTURES_PATH);
    static final String PICTURE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PICTURES_PATH;
    static final String PICTURE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PICTURES_PATH;

    // общий Uri
    static final int URI_NOTES = 10;
    // Uri с указанным ID
    static final int URI_NOTES_ID = 20;

    static final int URI_PICTURES = 30;
    static final int URI_PICTURES_ID = 40;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", URI_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, PICTURES_PATH, URI_PICTURES);
        uriMatcher.addURI(AUTHORITY, PICTURES_PATH + "/#", URI_PICTURES_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.i(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return (dbHelper != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query " + uri.toString());
        int uriType = uriMatcher.match(uri);
        db = dbHelper.getWritableDatabase();
        if (uriType == URI_NOTES || uriType == URI_NOTES_ID) {
            Cursor cursor = db.query(DBHelper.TABLE_NOTE, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), NOTE_CONTENT_URI);
            return cursor;
        } else if (uriType == URI_PICTURES || uriType == URI_PICTURES_ID){
            Cursor cursor = db.query(DBHelper.TABLE_PICTURES, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), PICTURE_CONTENT_URI);
            return cursor;
        } else {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)){
            case URI_NOTES:
                return NOTE_CONTENT_TYPE;
            case URI_NOTES_ID:
                return NOTE_CONTENT_ITEM_TYPE;
            case URI_PICTURES:
                return PICTURE_CONTENT_TYPE;
            case URI_PICTURES_ID:
                return PICTURE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        long rowID;
        Uri resultUri;
        switch (uriMatcher.match(uri)){
            case URI_NOTES:
                Log.d(LOG_TAG, "insert, URI_NOTES = " + URI_NOTES);
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(DBHelper.TABLE_NOTE, null, values);
                resultUri = ContentUris.withAppendedId(NOTE_CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            case URI_PICTURES:
                Log.d(LOG_TAG, "insert, URI_PICTURES = " + URI_PICTURES);
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(DBHelper.TABLE_PICTURES, null, values);
                resultUri = ContentUris.withAppendedId(PICTURE_CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            default: throw new IllegalArgumentException("Wrong URI" + uri);
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.KEY_ID_NOTES + " = " + id;
                } else {
                    selection = selection + " AND " +DBHelper.KEY_ID_NOTES + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(DBHelper.TABLE_NOTE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(LOG_TAG, "URI_NOTES");
                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_NOTES_ID " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.KEY_ID_NOTES + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.KEY_ID_NOTES + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(DBHelper.TABLE_NOTE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}







