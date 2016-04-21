package ringerjk.com.themoviedb.ui.util;

import android.content.Intent;
import android.view.View;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.ui.ListMoviesAndMovieActivity;

public class TodayInCinemaBtnListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {

        Intent intentListFilmActivity = new Intent(view.getContext(), ListMoviesAndMovieActivity.class);
        intentListFilmActivity.putExtra(MyConst.PATH_URL, "/movie/now_playing");
        view.getContext().startActivity(intentListFilmActivity);
    }
}
