package ringerjk.com.themoviedb.ui.util;

import android.content.Intent;
import android.view.View;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.service.RequestListFilmActService;
import ringerjk.com.themoviedb.ui.ListMoviesAndMovieActivity;

public class ComingSoonBtnListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Intent intentListFilmActivity = new Intent(view.getContext(), ListMoviesAndMovieActivity.class);
        intentListFilmActivity.putExtra(MyConst.PATH_URL, "/movie/upcoming");
        view.getContext().startActivity(intentListFilmActivity);
    }
}
