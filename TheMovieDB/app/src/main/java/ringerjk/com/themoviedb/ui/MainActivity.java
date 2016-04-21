package ringerjk.com.themoviedb.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.ui.util.ComingSoonBtnListener;
import ringerjk.com.themoviedb.ui.util.PopularBtnListener;
import ringerjk.com.themoviedb.ui.util.TodayInCinemaBtnListener;

public class MainActivity extends AppCompatActivity {

    //    String url = "https://api.themoviedb.org/3";
    MainActivityBroadcastReceiver receiver;

    Button comingSoonBtn;
    Button todayInCinemaBtn;
    Button popularBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MyConst.MY_LOG, "onCreate MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comingSoonBtn = (Button) findViewById(R.id.comingSoonBtn);
        todayInCinemaBtn = (Button) findViewById(R.id.todayInCinemaBtn);
        popularBtn = (Button) findViewById(R.id.popularBtn);

        comingSoonBtn.setOnClickListener(new ComingSoonBtnListener());
        todayInCinemaBtn.setOnClickListener(new TodayInCinemaBtnListener());
        popularBtn.setOnClickListener(new PopularBtnListener());
    }

    public class MainActivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
