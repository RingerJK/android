package ringerjk.com.todoisapp.activity.activityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;


public class ListViewListener implements AbsListView.MultiChoiceModeListener { // implements ActionMode.Callback {
    final String LOG_TAG = "myLogs";

    private ArrayList<Long> arrayList = new ArrayList<Long>();
    private Context context;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    public ListViewListener(Context context, SimpleCursorAdapter adapter, ListView listView) {
        this.context = context;
        this.adapter = adapter;
        this.listView = listView;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            arrayList.add(id);
        }
        if (!checked) {
            arrayList.remove(id);
        }
        mode.setTitle(context.getString(R.string.selected_elements_main_activity)+arrayList.size());
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
                for (int i = 0; i < arrayList.size(); i++) {
                    selection = selection + "_id = " + String.valueOf(arrayList.get(i));
                    if (arrayList.size() - 1 != i) {
                        selection = selection + " OR ";
                    }
                }
                context.getContentResolver().delete(uri, selection, null);
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
}
