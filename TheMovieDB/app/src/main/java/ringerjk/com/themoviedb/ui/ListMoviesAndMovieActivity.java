package ringerjk.com.themoviedb.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.adapter.MyFragmentPageAdapter;

public class ListMoviesAndMovieActivity extends AppCompatActivity implements ListMoviesFragment.SelectMovieListener {

    private boolean mDualPane;
    ViewPager pager;

    @Override
    public void selectMovie(int id) {
        Log.i(MyConst.MY_LOG, "id from Fragment = " + id);
        if (mDualPane) {
            pager = (ViewPager) findViewById(R.id.pager);
            MovieFragment movieFragment = null;//(MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMovie); доработать
            if (movieFragment == null || movieFragment.getMovieId() != id) {
                movieFragment = MovieFragment.newInstance(id);

                ListCastsFragment listCastsFragment = ListCastsFragment.newInstance(id);
                List<Fragment> fragmentList = new ArrayList<>();
                fragmentList.add(movieFragment);
                fragmentList.add(listCastsFragment);
                pager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager(), fragmentList));
            }
        } else {
            Intent intent = new Intent(this, MovieActivity.class);
            intent.putExtra(MyConst.ID_MOVIE_REQUEST, id);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies_and_movie);
        View pager = (ViewPager) findViewById(R.id.pager);
        mDualPane = pager != null && pager.getVisibility() == View.VISIBLE;
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
