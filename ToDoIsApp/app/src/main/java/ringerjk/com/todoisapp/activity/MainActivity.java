package ringerjk.com.todoisapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.activity.supportPackageActivity.EditorListener;
import ringerjk.com.todoisapp.activity.supportPackageActivity.ListViewListener;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    public final static String keyNodeId = "noteId";
    final static int RESULT_DELETE = 654641;

    public static ListView listView;
    public static SimpleCursorAdapter adapter;
    private EditText editTextDone;

    private final static int REQUEST_CODE_NEW_NOTE = 11111;
    private final static int REQUEST_CODE_MODIFIED_NOTE = 22222;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDone = (EditText) findViewById(R.id.edittext_done);
        EditorListener editListener = new EditorListener(this);
        editTextDone.setOnEditorActionListener(editListener);

        final Cursor cursor = getContentResolver().query(ToDoListProvider.NOTE_CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);

        String[] from = new String[]{DBHelper.KEY_TITLE_NOTES};
        int[] to = new int[]{R.id.titleText};

        adapter = new SimpleCursorAdapter(this, R.layout.custom_list_item, cursor, from, to, 0);
        listView = (ListView) findViewById(R.id.lv);
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

        listView.setMultiChoiceModeListener(new ListViewListener(this, adapter, listView));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        adapter.notifyDataSetChanged();
    }

    //requestCode - индефикатор интента, resultCode - код возврата(успешность операции), data - наш интент с данными
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult");
    }

    public void addNote(View view) {
        Intent intent = new Intent(getApplicationContext(), EnterRecordActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_NOTE);
    }
}
