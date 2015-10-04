package ringerjk.com.todoisapp.activity;

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
import ringerjk.com.todoisapp.models.Note;

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

    public void composeEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textTitle.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, textDesc.getText().toString());
        startActivity(emailIntent);
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
                    //updateNote();
                    intent = new Intent();
                    intent.putExtra(Note.KEY_NOTE, new Note(idNoteForUpdateOrDelete,
                                                            textTitle.getText().toString(),
                                                            textDesc.getText().toString()));
                    setResult(RESULT_OK, intent);
                    idNoteForUpdateOrDelete = 0;
                    finish();
                } else {
                    intent = new Intent();
                    intent.putExtra(Note.KEY_NOTE, new Note(textTitle.getText().toString(),
                                                            textDesc.getText().toString()));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.delete:
                if (idNoteForUpdateOrDelete != 0) {
                    //deleteNote();
                    intent = new Intent();
                    intent.putExtra(Note.KEY_NOTE, idNoteForUpdateOrDelete);
                    setResult(MainActivity.RESULT_DELETE, intent);
                    idNoteForUpdateOrDelete = 0;
                    finish();
                } else {
                    intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.mail:
                if (textTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Заполните поле Title", Toast.LENGTH_SHORT).show();
                } else {
                    composeEmail();
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
