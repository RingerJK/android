package ringerjk.com.todoisapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.activity.EnterRecordActivity;
import ringerjk.com.todoisapp.activity.MainActivity;
import ringerjk.com.todoisapp.activity.NotificationActivity;

 //Этот класс не используется!!!
public class NotificationService extends Service {
    final static String LOG_TAG = "myLogs";

    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "onCreate NotificationService");
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String titleNote = intent.getExtras().getString(NotificationActivity.KEY_TITLE);
        long idNote = intent.getExtras().getInt(MainActivity.keyNodeId);
        sendNotification(titleNote, idNote);
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendNotification(String title, long id) {
        Log.i(LOG_TAG, "sendNotification method");
        Notification notif = new Notification(R.drawable.ic_launcher, "Новое уведомление", System.currentTimeMillis());
        Intent intent = new Intent(this, EnterRecordActivity.class);
        intent.putExtra(MainActivity.keyNodeId, id);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notif.setLatestEventInfo(this, "У вас напоминание", title, pIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notif);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
