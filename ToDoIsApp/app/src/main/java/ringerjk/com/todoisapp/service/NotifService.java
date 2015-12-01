package ringerjk.com.todoisapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;

import java.util.Random;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.activity.EnterRecordActivity;
import ringerjk.com.todoisapp.activity.MainActivity;
import ringerjk.com.todoisapp.activity.NotificationActivity;

public class NotifService extends IntentService {
    NotificationManager nm;
    private Random r = new Random();

    public NotifService() {
        super("HelloIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String titleNote = intent.getExtras().getString(NotificationActivity.KEY_TITLE);
        long idNote = intent.getExtras().getInt(MainActivity.keyNodeId);

        Notification notif = new Notification(R.drawable.ic_launcher, "Новое уведомление", System.currentTimeMillis());
        Intent intent1 = new Intent(this, EnterRecordActivity.class);
        intent1.putExtra(MainActivity.keyNodeId, idNote);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        notif.setLatestEventInfo(this, "У вас напоминание", titleNote, pIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(r.nextInt(10000), notif);
    }
}
