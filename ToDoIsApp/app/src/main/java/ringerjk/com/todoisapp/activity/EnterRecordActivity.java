package ringerjk.com.todoisapp.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import ringerjk.com.todoisapp.MyConst;
import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.activity.activityUtils.UtilEnterRecordActivity;
import ringerjk.com.todoisapp.activity.activityUtils.UtilString;
import ringerjk.com.todoisapp.activity.dialog.DateDialog;
import ringerjk.com.todoisapp.activity.dialog.TimeDialog;
import ringerjk.com.todoisapp.adapter.CustomImageListViewAdapter;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;

public class EnterRecordActivity extends AppCompatActivity {
    final static String LOG_TAG = "myLogs";

    private int idNoteForUpdateOrDelete = 0;
    private File directory;

    private EditText textTitle;
    private EditText textDesc;
    private ListView lvImage;
    private LinearLayout hideContainerNotification;
    private ImageButton btnNotification;
    private TextView newTvTime;
    private TextView newTvDate;

    private Calendar calendarDayNotification;

    private CustomImageListViewAdapter customImageAdapter;

    private Cursor picCursor;

    private UtilEnterRecordActivity util;
    private UtilString utilString;

    private boolean notificationExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery_record);

        util = new UtilEnterRecordActivity();
        utilString = new UtilString();

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDesc = (EditText) findViewById(R.id.textDesc);
        lvImage = (ListView) findViewById(R.id.lvImage);
        hideContainerNotification = (LinearLayout) findViewById(R.id.hideContainerNotification);
        btnNotification = (ImageButton) findViewById(R.id.btnNotification);
        newTvTime = (TextView) findViewById(R.id.newTvTime);
        newTvDate = (TextView) findViewById(R.id.newTvDate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNoteForUpdateOrDelete = util.getIdAndInflateDataFromBundle(getApplicationContext(), extras, textTitle, textDesc);
        }

        calendarDayNotification = util.getCalendarDayNotificationIfExist(getApplicationContext(), idNoteForUpdateOrDelete);
        if (calendarDayNotification == null) {
            calendarDayNotification = util.getDefaultCalendarState();
            notificationExist = false;
            util.visibleNotificationView(hideContainerNotification, false);
            btnNotification.setImageResource(R.drawable.ic_add_alert_black_36dp);
        } else {
            notificationExist = true;
            util.visibleNotificationView(hideContainerNotification, true);
            btnNotification.setImageResource(R.drawable.ic_remove_circle_black_36dp);
        }
        util.setDateOnView(calendarDayNotification, newTvDate, MyConst.DATE);
        util.setDateOnView(calendarDayNotification, newTvTime, MyConst.TIME);

        setOnClickTextViewTime(newTvTime);
        setOnClickTextViewDate(newTvDate);

        setOnClickButtonNotification(btnNotification);

        picCursor = getContentResolver().query(ToDoListProvider.PICTURE_CONTENT_URI
                , null
                , DBHelper.KEY_NOTE_ID_PICTURES + " = ?"
                , new String[]{String.valueOf(idNoteForUpdateOrDelete)}
                , null);
        startManagingCursor(picCursor);

        customImageAdapter = new CustomImageListViewAdapter(this, new ArrayList<String>());
        lvImage.setAdapter(customImageAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (picCursor.moveToFirst()) {
            ArrayList<String> bitmapArrayList = new ArrayList<>();
            String imgPath;
            do {
                imgPath = picCursor.getString(picCursor.getColumnIndex(DBHelper.KEY_IMAGE_PICTURES));
                bitmapArrayList.add(imgPath);
            } while (picCursor.moveToNext());
            customImageAdapter.updateListView(bitmapArrayList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entery_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok:
                if (utilString.isEmpty(textTitle.getText().toString())) {
                    util.viewWarningWriteDesc(getApplicationContext());
                } else {
                    util.saveOrRemoveNotification(getApplicationContext(),
                            idNoteForUpdateOrDelete,
                            calendarDayNotification,
                            notificationExist ? true : false);
                    util.saveTitleAndDescription(getApplicationContext(), idNoteForUpdateOrDelete, textTitle, textDesc);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.delete:
                Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteForUpdateOrDelete);
                int cnt = getContentResolver().delete(uri, null, null);
                Log.d(LOG_TAG, "delete, count = " + cnt);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.mail:
                if (utilString.isEmpty(textTitle.getText().toString())) {
                    util.viewWarningWriteDesc(getApplicationContext());
                } else {
                    util.composeEmail(getApplicationContext(), textTitle, textDesc);
                    setResult(RESULT_OK);
                }
                break;
            case R.id.photo:
                directory = util.createDirectory();
                Intent chooserIntent = util.getIntentPhoto();
                startActivityForResult(chooserIntent, MyConst.REQUEST_CODE_PHOTO);
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult, requestCode =" + requestCode + "; data " + data + "; resultCode " + resultCode);
        switch (requestCode) {
            case MyConst.REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (null == data.getAction()) { //галерея
                        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        Uri directoryImage = Uri.parse(cursor.getString(idx));
                        util.saveImageOrPhoto(getApplicationContext(), idNoteForUpdateOrDelete, directoryImage);
                        cursor.close();
                    } else { // камера
                        Uri uri = util.generateFileUri(directory, MyConst.TYPE_PHOTO);
                        Bitmap btm = (Bitmap) data.getExtras().get("data");
                        if (null != btm) {
                            try {
                                FileOutputStream out = new FileOutputStream(uri.getPath());
                                btm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();
                                util.saveImageOrPhoto(getApplicationContext(), idNoteForUpdateOrDelete, uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    public void setOnClickButtonNotification(ImageButton buttonNotification) {
        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationExist) {
                    util.visibleNotificationView(hideContainerNotification, false);
                    btnNotification.setImageResource(R.drawable.ic_add_alert_black_36dp);
                    notificationExist = false;
                } else {
                    util.visibleNotificationView(hideContainerNotification, true);
                    btnNotification.setImageResource(R.drawable.ic_remove_circle_black_36dp);
                    notificationExist = true;
                }
            }
        });
    }

    public void setOnClickTextViewTime(TextView textViewTime) {
        final Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                int timeHourHandle = bundle.getInt(MyConst.HOUR);
                int timeMinuteHandle = bundle.getInt(MyConst.MINUTE);
                calendarDayNotification.set(Calendar.HOUR_OF_DAY, timeHourHandle);
                calendarDayNotification.set(Calendar.MINUTE, timeMinuteHandle);
                util.setDateOnView(calendarDayNotification, newTvTime, MyConst.TIME);
            }
        };

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(MyConst.HOUR, calendarDayNotification.get(Calendar.HOUR_OF_DAY));
                bundle.putInt(MyConst.MINUTE, calendarDayNotification.get(Calendar.MINUTE));
                TimeDialog timeDialog = new TimeDialog(h);
                timeDialog.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(timeDialog, "time_picker");
                ft.commit();
            }
        });
    }

    public void setOnClickTextViewDate(TextView textViewDate) {
        final Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                int timeDayHandle = bundle.getInt(MyConst.DAY);
                int timeMonthHandle = bundle.getInt(MyConst.MONTH);
                int timeYearHandle = bundle.getInt(MyConst.YEAR);
                calendarDayNotification.set(Calendar.DAY_OF_MONTH, timeDayHandle);
                calendarDayNotification.set(Calendar.MONTH, timeMonthHandle);
                calendarDayNotification.set(Calendar.YEAR, timeYearHandle);
                util.setDateOnView(calendarDayNotification, newTvDate, MyConst.DATE);
            }
        };
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(MyConst.DAY, calendarDayNotification.get(Calendar.DAY_OF_MONTH));
                bundle.putInt(MyConst.MONTH, calendarDayNotification.get(Calendar.MONTH));
                bundle.putInt(MyConst.YEAR, calendarDayNotification.get(Calendar.YEAR));
                DateDialog dateDialog = new DateDialog(h);
                dateDialog.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(dateDialog, "date_picker");
                ft.commit();
            }
        });
    }


}
