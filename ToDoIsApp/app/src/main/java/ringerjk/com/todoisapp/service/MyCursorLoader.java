package ringerjk.com.todoisapp.service;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class MyCursorLoader extends CursorLoader {

    DatabaseAction dba;

    public MyCursorLoader(Context context, DatabaseAction dba) {
        super(context);
        this.dba = dba;
    }

    @Override
    public Cursor loadInBackground() {
        return dba.getAllData();
    }

}