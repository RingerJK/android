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

    static final String DB_NAME = "toDoListDB";
    static final int DB_VERSION = 1;
    static final String TABLE_NOTE = "notes";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    public static String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_TITLE + " TEXT, "
            + KEY_DESCRIPTION + " TEXT )";

    static final String AUTHORITY = "ringerjk.com.todoisapp.contentProvider.ToDoList";

    static final String NOTE_PATH = "notes";

    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + NOTE_PATH);

    static final String NOTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + NOTE_PATH;

    static final String NOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + NOTE_PATH;

    // общий Uri
    static final int URI_NOTES = 1;

    // Uri с указанным ID
    static final int URI_NOTES_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", URI_NOTES_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.i(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query " + uri.toString());
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NOTE_CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)){
            case URI_NOTES:
                return NOTE_CONTENT_TYPE;
            case URI_NOTES_ID:
                return NOTE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_NOTES)
            throw new IllegalArgumentException("Wrong URI" + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(TABLE_NOTE, null, values);
        Uri resultUri = ContentUris.withAppendedId(NOTE_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
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
                    selection = KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(TABLE_NOTE, selection, selectionArgs);
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
                    selection = KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(TABLE_NOTE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}







