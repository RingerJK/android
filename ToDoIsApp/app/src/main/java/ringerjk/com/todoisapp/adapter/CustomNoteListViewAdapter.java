package ringerjk.com.todoisapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class CustomNoteListViewAdapter extends SimpleCursorAdapter {
    public CustomNoteListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
