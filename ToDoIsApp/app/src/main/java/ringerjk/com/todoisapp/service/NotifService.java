package ringerjk.com.todoisapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import ringerjk.com.todoisapp.MyConst;
import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.activity.EnterRecordActivity;
import ringerjk.com.todoisapp.activity.MainActivity;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;

public class NotifService extends IntentService {
    private NotificationManager nm;

    public NotifService() {
        super("HelloIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long idNote = intent.getExtras().getInt(MyConst.KEY_NODE_ID);

        String noteTitle;
        Cursor cursor = getApplicationContext().getContentResolver().query(
                ToDoListProvider.NOTE_CONTENT_URI,
                null,
                "_id = ?",
                new String[]{String.valueOf(idNote)},
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            noteTitle = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE_NOTES));
            cursor.close();
        } else {
            return;
        }
        Intent intent1 = new Intent(this, EnterRecordActivity.class);
        intent1.putExtra(MyConst.KEY_NODE_ID, idNote);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setTicker(getString(R.string.notification__service))
                .setContentText(noteTitle)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notification = nb.build();
        nm.notify((int) idNote, notification);
    }
}
