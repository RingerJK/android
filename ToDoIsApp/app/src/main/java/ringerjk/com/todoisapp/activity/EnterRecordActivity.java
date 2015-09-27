package ringerjk.com.todoisapp.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;

public class EnterRecordActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    EditText textTitle;
    EditText textDesc;
    int idNoteForUpdateOrDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery_record);

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDesc = (EditText) findViewById(R.id.textDesc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNoteForUpdateOrDelete = (int) extras.getLong(MainActivity.keyNodeId);
            String selection = "_id = ?";
            String[] selectionArgs = new String[] {String.valueOf(idNoteForUpdateOrDelete)};
            Cursor cursor = getContentResolver().query(ToDoListProvider.NOTE_CONTENT_URI, null, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                textTitle.setText(cursor.getString(cursor.getColumnIndex(ToDoListProvider.KEY_TITLE)));
                textDesc.setText(cursor.getString(cursor.getColumnIndex(ToDoListProvider.KEY_DESCRIPTION)));
                cursor.close();
            }
        }

    }

    public void updateNote() {
        ContentValues cv = new ContentValues();
        cv.put(ToDoListProvider.KEY_TITLE, textTitle.getText().toString());
        cv.put(ToDoListProvider.KEY_DESCRIPTION, textDesc.getText().toString());
        Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteForUpdateOrDelete);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void deleteNote() {
        Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteForUpdateOrDelete);
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void addNote() {
        ContentValues cv = new ContentValues();
        cv.put(ToDoListProvider.KEY_TITLE, textTitle.getText().toString());
        cv.put(ToDoListProvider.KEY_DESCRIPTION, textDesc.getText().toString());
        Uri newUri = getContentResolver().insert(ToDoListProvider.NOTE_CONTENT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entery_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.ok:
                if (textTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Заполните поле Title", Toast.LENGTH_SHORT).show();
                } else if (idNoteForUpdateOrDelete != 0) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    updateNote();
                    idNoteForUpdateOrDelete = 0;
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    addNote();
                    startActivity(intent);
                }
                break;
            case R.id.delete:
                if (idNoteForUpdateOrDelete != 0) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    deleteNote();
                    idNoteForUpdateOrDelete = 0;
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
