package ringerjk.com.todoisapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.service.MyCursorLoader;
import ringerjk.com.todoisapp.service.DatabaseAction;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String keyNodeId = "noteId";

    SimpleCursorAdapter scAdapter;
    ListView listView;
    DatabaseAction dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv);

        dba = new DatabaseAction(this);
        dba.openConnection();

        String[] from = new String[]{DatabaseAction.KEY_TITLE};
        int[] to = new int[]{R.id.titleText};

        scAdapter = new SimpleCursorAdapter(this, R.layout.custom_list_item, null, from, to, 0);

        listView.setAdapter(scAdapter);

        listView.isItemChecked(3);

        registerForContextMenu(listView);
        getSupportLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EnterRecordActivity.class);
                intent.putExtra(keyNodeId, id);
                startActivity(intent);
                Log.i("Tag", "LongClick");
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Tag", "LongClick");
                view.setSelected(true);
                return true;
            }
        });


        listView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {

            ArrayList<Long> arrayList = new ArrayList<Long>();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked){
                    arrayList.add(id);
                } if (!checked){
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
                        deleteSelectItems(arrayList);
                        onLoaderReset(null); // можно ли так????
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
    protected void onDestroy() {
        super.onDestroy();
        dba.closeConnection();
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

    private void deleteSelectItems(ArrayList<Long> idArrayList) {
        for (Long id : idArrayList){
            dba.deleteNote((int)(long)id);
        }
//        scAdapter.req
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dba);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(dba.getAllData()); // можно ли так???
    }


}
