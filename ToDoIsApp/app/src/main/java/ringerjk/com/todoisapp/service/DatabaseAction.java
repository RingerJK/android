package ringerjk.com.todoisapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ringerjk.com.todoisapp.domain.Note;

public class DatabaseAction{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDoIs";
    public static final String TABLE_NAME = "note";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    private final Context context;
    SQLiteDatabase db;
    DBHelper dbHelper;

    public DatabaseAction(Context context) {
        this.context = context;
    }

    public void openConnection(){
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION );
        db = dbHelper.getWritableDatabase();
    }

    public void closeConnection(){
        db.close();
    }

    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        if (0 != note.getId()){
            values.put(KEY_ID, note.getId());
        }
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_DESCRIPTION, note.getDescription());
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getNote(int id){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id, null);
    }


    public ArrayList<String> getAllTitle() {
        ArrayList<String> titles = new ArrayList<String>();
        String selectQuery = "SELECT " + KEY_TITLE + " FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                titles.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }


        return titles;
    }

    public void updateNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseAction.KEY_TITLE, note.getTitle());
        cv.put(DatabaseAction.KEY_DESCRIPTION, note.getDescription());
        db.update(TABLE_NAME, cv, DatabaseAction.KEY_ID + " = " + note.getId(), null);
    }

    public void deleteNote(int id) {
        db.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    public void deleteAllNotes(){
        db.delete(TABLE_NAME, null, null);
    }
}
