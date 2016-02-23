package ringerjk.com.todoisapp.activity.activityUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ringerjk.com.todoisapp.MyConst;
import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;
import ringerjk.com.todoisapp.models.Note;
import ringerjk.com.todoisapp.models.Picture;
import ringerjk.com.todoisapp.service.NotifService;

public class UtilEnterRecordActivity {

    public void setDateOnView(Calendar date, TextView view, int requestCode) {
        switch (requestCode) {
            case MyConst.DATE:
                view.setText(view.getResources().getString(R.string.date) + " " + MyConst.dfDMY.format(date.getTime()));
                break;
            case MyConst.TIME:
                view.setText(view.getResources().getString(R.string.time) + " " + MyConst.dfHrAndMi.format(date.getTime()));
                break;
        }
    }

    public void viewWarningWriteDesc(Context context) {
        Toast.makeText(context, R.string.warning_title, Toast.LENGTH_SHORT).show();
    }

    public Calendar getCalendarDayNotificationIfExist(Context context, int id) {
        SharedPreferences sPref = context.getSharedPreferences(MyConst.LOCATE_NOTIFICATIONS, Context.MODE_PRIVATE);
        long savedDayNotification = sPref.getLong(MyConst.KEY_S_PREF + id, 0);
        Calendar dayNtf = Calendar.getInstance();
        dayNtf.setTimeInMillis(savedDayNotification);
        if (savedDayNotification != 0 && Calendar.getInstance().getTimeInMillis() > dayNtf.getTimeInMillis()) {
            saveOrRemoveNotification(context, id, null, false);
            return null;
        }
        return savedDayNotification == 0 ? null : dayNtf;
    }

    public void setAlarmManagerOnERA(Context context, int idNoteNtf, Calendar dayNotification) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = getPendingIntentForAlarmManager(context, idNoteNtf);
        am.cancel(pIntent);
        am.set(AlarmManager.RTC, dayNotification.getTimeInMillis(), pIntent);
    }

    public void deleteAlarmManagerOnERA(Context context, int id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = getPendingIntentForAlarmManager(context, id);
        am.cancel(pIntent);
    }

    public PendingIntent getPendingIntentForAlarmManager(Context context, int idNoteNtf) {
        Intent intent = new Intent(context, NotifService.class);
        intent.putExtra(MyConst.KEY_NODE_ID, idNoteNtf);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public Calendar getDefaultCalendarState() {
        Calendar defaultState = Calendar.getInstance();
        defaultState.add(Calendar.DAY_OF_MONTH, 1);
        defaultState.set(Calendar.SECOND, 0);
        return defaultState;
    }

    public void visibleNotificationView(LinearLayout layout, boolean show) {
        if (show) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    public int getIdAndInflateDataFromBundle(Context context, Bundle extras, EditText textTitle, EditText textDesc) {
        int id = (int) extras.getLong(MyConst.KEY_NODE_ID);
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = context.getContentResolver().query(
                ToDoListProvider.NOTE_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            textTitle.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE_NOTES)));
            textDesc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_NOTES)));
            cursor.close();
        }
        return id;
    }

    public int saveTitleAndDescription(Context context, int id, EditText textTitle, EditText textDesc) {
        Note note = new Note(id, textTitle.getText().toString(), textDesc.getText().toString());
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_TITLE_NOTES, note.getTitle());
        cv.put(DBHelper.KEY_DESCRIPTION_NOTES, note.getDescription());

        Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, note.getId());
        context.getContentResolver().update(uri, cv, null, null);
        id = Integer.parseInt((uri.getLastPathSegment()));
        return id;
    }

    public void saveImageOrPhoto(Context context, int id, Uri photoUri) {
        Picture picture = new Picture(photoUri.getPath(), id);
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_IMAGE_PICTURES, picture.getImageLink());
        cv.put(DBHelper.KEY_NOTE_ID_PICTURES, picture.getNote_id());
        context.getContentResolver().insert(ToDoListProvider.PICTURE_CONTENT_URI, cv);
    }

    public void saveOrRemoveNotification(Context context, int id, Calendar calendarDayNtf, boolean save) {
        SharedPreferences sPref = context.getSharedPreferences(MyConst.LOCATE_NOTIFICATIONS, Context.MODE_PRIVATE);
        if (save) {
            setAlarmManagerOnERA(context, id, calendarDayNtf);
            sPref.edit().putLong(MyConst.KEY_S_PREF + id, calendarDayNtf.getTimeInMillis()).apply();
        } else {
            deleteAlarmManagerOnERA(context, id);
            sPref.edit().remove(MyConst.KEY_S_PREF + id);
        }
    }

    public void composeEmail(Context context, EditText textTitle, EditText textDesc) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textTitle.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, textDesc.getText().toString());
        context.startActivity(emailIntent);
    }

    public Intent getIntentPhoto() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = "Select or take a new Picture";
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[]{takePhotoIntent});
        return chooserIntent;
    }

    public Uri generateFileUri(File directory, int type) {
        File file = null;
        switch (type) {
            case MyConst.TYPE_PHOTO:
                String namePh = "photo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                file = new File(directory.getPath() + "/" + namePh);
                break;
        }
        return Uri.fromFile(file);
    }


    public File createDirectory() {
        File directory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                MyConst.IMAGE_FOLDER);
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }
}
