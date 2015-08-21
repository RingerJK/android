package ringerjk.com.todoisapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.domain.Note;
import ringerjk.com.todoisapp.service.DatabaseAction;

public class EnterRecordActivity extends AppCompatActivity {

    EditText textTitle;
    EditText textDesc;
    DatabaseAction dba;
    int idNoteForUpdateOrDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery_record);

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDesc = (EditText) findViewById(R.id.textDesc);
        dba = new DatabaseAction(this);
        dba.openConnection();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNoteForUpdateOrDelete = (int) extras.getLong(MainActivity.keyNodeId);
            Cursor cursor = dba.getNote(idNoteForUpdateOrDelete);

            if (cursor != null && cursor.moveToFirst()) {
                textTitle.setText(cursor.getString(cursor.getColumnIndex(DatabaseAction.KEY_TITLE)));
                textDesc.setText(cursor.getString(cursor.getColumnIndex(DatabaseAction.KEY_DESCRIPTION)));
                cursor.close();
            }
        }

    }

    public void updateNote() {
        dba.updateNote(new Note(idNoteForUpdateOrDelete, textTitle.getText().toString(), textDesc.getText().toString()));
    }


    public void addNote() {
        dba.addNote(new Note(textTitle.getText().toString(), textDesc.getText().toString()));
    }

    public void deleteNote() {
        dba.deleteNote(idNoteForUpdateOrDelete);
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
