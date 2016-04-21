package ringerjk.com.themoviedb.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;

public class ListMoviesAndMovieActivity extends AppCompatActivity implements ListMoviesFragment.SelectMovieListener {

    private boolean mDualPane;

    @Override
    public void selectMovie(int id) {
        Log.i(MyConst.MY_LOG, "id from Fragment = "+id);
        if (mDualPane) {
            MovieFragment movieFragment = (MovieFragment) getFragmentManager().findFragmentById(R.id.frameMovie);
            if (movieFragment == null || movieFragment.getMovieId() != id){
                movieFragment = MovieFragment.newInstance(id);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //if (id == 0) {
                    ft.replace(R.id.frameMovie, movieFragment);
               // } else {
               //     ft.replace(R.)
               // }
                ft.commit();
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
        setContentView(R.layout.fragment_list_movies);
        View movieFragment = findViewById(R.id.frameMovie);
        mDualPane = movieFragment != null && movieFragment.getVisibility() == View.VISIBLE;
    }
}
