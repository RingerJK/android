package ringerjk.com.todoisapp.activity.supportPackageActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import ringerjk.com.todoisapp.activity.MainActivity;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;
import ringerjk.com.todoisapp.models.Note;

public class EditorListener extends View implements TextView.OnEditorActionListener {
    final String LOG_TAG = "myLogs";

    Context context;

    public EditorListener(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean result = false;
//        Log.i(LOG_TAG, "onEditorAction actionId = "+actionId+"; event = "+event.getKeyCode());
        if ((event != null
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN
                && v.getText().toString().trim().length() != 0)
                || (actionId == EditorInfo.IME_ACTION_DONE
                && v.getText().toString().trim().length() != 0)) {
            Log.i(LOG_TAG, "v.toString() = " + v.getText().toString());
            saveTitleInDb(v);
            MainActivity.adapter.getCursor().requery();
            v.setText("");
            Activity activity = (Activity) context;
            InputMethodManager inputManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            result = true;
        }
        return result;
    }

    private void saveTitleInDb(TextView v) {
        Note note = new Note(v.getText().toString(), null);

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_TITLE_NOTES, note.getTitle());

        Uri uri = context.getContentResolver().insert(ToDoListProvider.NOTE_CONTENT_URI, cv);
    }
}
