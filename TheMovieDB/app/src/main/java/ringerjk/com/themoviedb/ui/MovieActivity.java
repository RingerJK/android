package ringerjk.com.themoviedb.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.adapter.MyFragmentPageAdapter;

public class MovieActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }
        int id = getIntent().getIntExtra(MyConst.ID_MOVIE_REQUEST, 0);
        MovieFragment movieFragment = MovieFragment.newInstance(id);
//        getFragmentManager().beginTransaction().add(R.id.fragmentMovie, movieFragment).commit();
        Log.i(MyConst.MY_LOG, "id movie = " + id);
        ListCastsFragment listCastsFragment = ListCastsFragment.newInstance(id);


        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(movieFragment);
        fragmentList.add(listCastsFragment);

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pagerAdapter);
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
