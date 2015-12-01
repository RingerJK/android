package ringerjk.com.todoisapp.activity;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;
import ringerjk.com.todoisapp.service.NotifService;

public class NotificationActivity extends AppCompatActivity {
    final static String LOG_TAG = "myLogs";
    final static public String KEY_TITLE = "keyTitle";

    TextView tvTime;
    TextView tvDate;
    Calendar dayNotification;
    final int DIALOG_TIME = 10;
    final int DIALOG_DATE = 20;

    AlarmManager am;

    private static final DateFormat dfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final DateFormat dfHrAndMi = new SimpleDateFormat("HH:mm");
    private static final DateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);
        dayNotification = Calendar.getInstance();
        dayNotification.add(Calendar.DAY_OF_MONTH, 1);

        tvTime.setText(dfHrAndMi.format(dayNotification.getTime()));
        tvDate.setText(dfDMY.format(dayNotification.getTime()));

        Log.i(LOG_TAG, "DateNotiif " + dfFull.format(dayNotification.getTime()));
    }

    public void onClickAddNotification(View view) {
        Log.i(LOG_TAG, "onClickAddNotification");

        Log.i(LOG_TAG, "am time " + dfFull.format(System.currentTimeMillis() + 20000));
        int idNoteNtf = 0;
        String noteTitle = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNoteNtf = (int) extras.getInt(MainActivity.keyNodeId);
            //Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteNtf);
            Cursor cursor = getContentResolver().query(
                    ToDoListProvider.NOTE_CONTENT_URI,
                    null,
                    "_id = ?",
                    new String[]{String.valueOf(idNoteNtf)},
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                noteTitle = cursor.getString(cursor.getColumnIndex(ToDoListProvider.KEY_TITLE_NOTES));
                cursor.close();
            }
            Intent intent = new Intent(NotificationActivity.this, NotifService.class);
            intent.putExtra(MainActivity.keyNodeId, idNoteNtf);
            intent.putExtra(KEY_TITLE, noteTitle);

            PendingIntent pIntent = PendingIntent.getService(NotificationActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(pIntent);
            am.set(AlarmManager.RTC, dayNotification.getTimeInMillis(), pIntent);

            Log.i(LOG_TAG, "dayNotification = " + dfFull.format(dayNotification.get(Calendar.MILLISECOND)));
            setResult(RESULT_OK);
            finish();
        }
    }


    public void onClickCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onClickTime(View view) {
        showDialog(DIALOG_TIME);
    }

    public void onClickDate(View view) {
        showDialog(DIALOG_DATE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_TIME:
                TimePickerDialog tpd = new TimePickerDialog(
                        this,
                        myCallBackTime,
                        dayNotification.get(Calendar.HOUR_OF_DAY),
                        dayNotification.get(Calendar.MINUTE),
                        true);
                return tpd;
            case DIALOG_DATE:
                //Log.i(LOG_TAG, "DIALOG_DATE yyyy/mm/dd" + dfFull.format(dayNotification.getTime()));
                DatePickerDialog dpd = new DatePickerDialog(
                        this,
                        myCallBackDate,
                        dayNotification.get(Calendar.YEAR),
                        dayNotification.get(Calendar.MONTH),
                        dayNotification.get(Calendar.DAY_OF_MONTH));
                return dpd;
        }
        return super.onCreateDialog(id);
    }

    OnTimeSetListener myCallBackTime = new OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dayNotification.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dayNotification.set(Calendar.MINUTE, minute);
            tvTime.setText(dfHrAndMi.format(dayNotification.getTime()));
        }
    };

    OnDateSetListener myCallBackDate = new OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dayNotification.set(year, monthOfYear, dayOfMonth);
            tvDate.setText(dfDMY.format(dayNotification.getTime()));
        }
    };
}
