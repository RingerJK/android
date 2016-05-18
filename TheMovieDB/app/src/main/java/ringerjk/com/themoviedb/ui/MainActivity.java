package ringerjk.com.themoviedb.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.ui.util.ComingSoonBtnListener;
import ringerjk.com.themoviedb.ui.util.PopularBtnListener;
import ringerjk.com.themoviedb.ui.util.TodayInCinemaBtnListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MyConst.MY_LOG, "onCreate MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button comingSoonBtn = (Button) findViewById(R.id.comingSoonBtn);
        Button todayInCinemaBtn = (Button) findViewById(R.id.todayInCinemaBtn);
        Button popularBtn = (Button) findViewById(R.id.popularBtn);

        comingSoonBtn.setOnClickListener(new ComingSoonBtnListener());
        todayInCinemaBtn.setOnClickListener(new TodayInCinemaBtnListener());
        popularBtn.setOnClickListener(new PopularBtnListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
}
