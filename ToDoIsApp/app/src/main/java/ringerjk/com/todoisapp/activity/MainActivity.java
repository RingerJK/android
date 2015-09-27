package ringerjk.com.todoisapp.activity;

import android.app.Activity;
import android.content.ContentUris;
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

import java.util.ArrayList;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    public final static String keyNodeId = "noteId";
    private ListView listView;
    private SimpleCursorAdapter adapter;

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
                startActivity(intent);
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
                Log.i("TAG", "onCreateActionMode");
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
                Log.i("TAG", "onActionItemClicked");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(), EnterRecordActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
