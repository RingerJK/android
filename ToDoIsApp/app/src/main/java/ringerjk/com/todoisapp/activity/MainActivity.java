package ringerjk.com.todoisapp.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;
import ringerjk.com.todoisapp.models.Note;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    public final static String keyNodeId = "noteId";
    final static int RESULT_DELETE = 654641;

    private ListView listView;
    private SimpleCursorAdapter adapter;

    private final static int REQUEST_CODE_NEW_NOTE = 11111;
    private final static int REQUEST_CODE_MODIFIED_NOTE = 22222;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Cursor cursor = getContentResolver().query(ToDoListProvider.NOTE_CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);

        String[] from = new String[]{ToDoListProvider.KEY_TITLE};
        int[] to = new int[]{R.id.titleText};

        adapter = new SimpleCursorAdapter(this, R.layout.custom_list_item, cursor, from, to, 0);
        listView = (ListView)findViewById(R.id.lv);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EnterRecordActivity.class);
                intent.putExtra(keyNodeId, id);
                startActivityForResult(intent, REQUEST_CODE_MODIFIED_NOTE);
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {

            ArrayList<Long> arrayList = new ArrayList<Long>();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    arrayList.add(id);
                }
                if (!checked) {
                    arrayList.remove(id);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.i(LOG_TAG, "onCreateActionMode");
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_main_long_click, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.i(LOG_TAG, "onActionItemClicked");
                switch (item.getItemId()) {
                    case R.id.del_long_click:
                        Uri uri = ToDoListProvider.NOTE_CONTENT_URI;
                        String selection = "";
                        for (int i = 0; i < arrayList.size() ; i++ ) {
                            selection = selection + "_id = " + String.valueOf(arrayList.get(i));
                            if (arrayList.size()-1 != i) {
                                selection = selection + " OR ";
                            }
                        }
                        getContentResolver().delete(uri, selection, null);
                        adapter.getCursor().requery();
                        mode.finish();
                        return true;
                }
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listView.clearChoices();
                arrayList.clear();
            }
        });

    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume");
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(), EnterRecordActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_NOTE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //requestCode - индефикатор интента, resultCode - код возврата(успешность операции), data - наш интент с данными
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult");
        switch (requestCode){
            case REQUEST_CODE_MODIFIED_NOTE:
                if (resultCode == RESULT_OK && data.getExtras() != null){
                    Note note =(Note) data.getSerializableExtra(Note.KEY_NOTE);

                    ContentValues cv = new ContentValues();
                    cv.put(ToDoListProvider.KEY_TITLE, note.getTitle());
                    cv.put(ToDoListProvider.KEY_DESCRIPTION, note.getDescription());

                    Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, note.getId());
                    int cnt = getContentResolver().update(uri, cv, null, null);

                    Log.d(LOG_TAG, "update, count = " + cnt);
                } else if (resultCode == MainActivity.RESULT_DELETE && data.getExtras() != null){
                    int idNoteForDelete = data.getExtras().getInt(Note.KEY_NOTE);
                    Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteForDelete);
                    int cnt = getContentResolver().delete(uri, null, null);
                    Log.d(LOG_TAG, "delete, count = " + cnt);
                }
                break;
            case REQUEST_CODE_NEW_NOTE:
                if (resultCode == RESULT_OK && data.getExtras() != null){
                    Note note =(Note) data.getSerializableExtra(Note.KEY_NOTE);

                    ContentValues cv = new ContentValues();
                    cv.put(ToDoListProvider.KEY_TITLE, note.getTitle());
                    cv.put(ToDoListProvider.KEY_DESCRIPTION, note.getDescription());

                    Uri newUri = getContentResolver().insert(ToDoListProvider.NOTE_CONTENT_URI, cv);
                }
                break;
        }
    }
}
