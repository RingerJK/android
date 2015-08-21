package ringerjk.com.todoisapp.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOIS_TABLE = "CREATE TABLE " + DatabaseAction.TABLE_NAME + " ("
                + DatabaseAction.KEY_ID + " INTEGER PRIMARY KEY, "
                + DatabaseAction.KEY_TITLE + " TEXT, "
                + DatabaseAction.KEY_DESCRIPTION + " TEXT )";
        db.execSQL(CREATE_TODOIS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
