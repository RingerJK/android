package ringerjk.com.themoviedb.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;

public class MovieActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }
        int id = getIntent().getIntExtra(MyConst.ID_MOVIE_REQUEST, 0);
        MovieFragment movieFragment = MovieFragment.newInstance(id);
        getFragmentManager().beginTransaction().add(R.id.fragmentMovie, movieFragment).commit();
        Log.i(MyConst.MY_LOG, "id movie = " + id);
    }

}
