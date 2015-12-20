package ringerjk.com.todoisapp.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "toDoListDB";
    static final int DB_VERSION = 1;

    static final String TABLE_NOTE = "notes";
    public static final String KEY_ID_NOTES = "_id";
    public static final String KEY_TITLE_NOTES = "title";
    public static final String KEY_DESCRIPTION_NOTES = "description";

    public static String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTE + " ("
            + KEY_ID_NOTES + " INTEGER PRIMARY KEY, "
            + KEY_TITLE_NOTES + " TEXT, "
            + KEY_DESCRIPTION_NOTES + " TEXT ); ";


    static final String TABLE_PICTURES = "pictures";
    public static final String KEY_ID_PICTURES = "_id";
    public static final String KEY_IMAGE_PICTURES = "image";
    public static final String KEY_NOTE_ID_PICTURES = "note_id";

    public static String CREATE_PICTURES_TABLE = " CREATE TABLE " + TABLE_PICTURES + " ("
            + KEY_ID_PICTURES +  " INTEGER PRIMARY KEY, "
            + KEY_IMAGE_PICTURES + " VARCHAR, "
            + KEY_NOTE_ID_PICTURES + " INTEGER, "
            + " FOREIGN KEY ("+ KEY_NOTE_ID_PICTURES + ") REFERENCES " + TABLE_NOTE + " (" + KEY_ID_NOTES + ") ON DELETE CASCADE ); ";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_PICTURES_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
