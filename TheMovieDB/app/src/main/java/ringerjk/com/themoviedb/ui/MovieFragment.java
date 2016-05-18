package ringerjk.com.themoviedb.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.entity.Movie;
import ringerjk.com.themoviedb.service.RequestListFilmActService;

public class MovieFragment extends Fragment {

    private View v;
    private MovieActivityBroadcastReceiver receiver;

    ImageView moviePoster;
    TextView movieTitle;
    TextView sinopsis;
    TextView movieYear;
    TextView countries;
    TextView rateMovie;
    TextView voteCount;

    public static MovieFragment newInstance(int id) {
        MovieFragment fragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConst.ID_MOVIE_REQUEST, id);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        v = inflater.inflate(R.layout.fragment_movie, null); //container, container == null

        moviePoster = (ImageView) v.findViewById(R.id.moviePoster);
        movieTitle = (TextView) v.findViewById(R.id.movieTitle);
        countries = (TextView) v.findViewById(R.id.countries);
        movieYear = (TextView) v.findViewById(R.id.movieYear);
        rateMovie = (TextView) v.findViewById(R.id.rateMovie);
        voteCount = (TextView) v.findViewById(R.id.voteCount);
        sinopsis = (TextView) v.findViewById(R.id.sinopsis);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int id = getMovieId();
        Log.i(MyConst.MY_LOG, "id movie = " + id);

        IntentFilter intentFilter = new IntentFilter(MovieActivityBroadcastReceiver.RESPONSE_MOVIE_ACTIVITY_RECEIVER);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MovieActivityBroadcastReceiver();
        getActivity().registerReceiver(receiver, intentFilter);

        Intent intentRequestMovieActivityService = new Intent(getActivity(), RequestListFilmActService.class);
        intentRequestMovieActivityService.putExtra(MyConst.EXTRA_REQUEST_URI_PATH, "/movie/" + id);
        intentRequestMovieActivityService.putExtra(MyConst.INTENT_FILTER, MovieActivityBroadcastReceiver.RESPONSE_MOVIE_ACTIVITY_RECEIVER);
        getActivity().startService(intentRequestMovieActivityService);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
    }

    public int getMovieId() {
        int id = 0;
        try {
            id = getArguments().getInt(MyConst.ID_MOVIE_REQUEST, 0);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Ошибка поиска id", Toast.LENGTH_SHORT).show();
        }
        return id;
    }


    public class MovieActivityBroadcastReceiver extends BroadcastReceiver {
        public static final String RESPONSE_MOVIE_ACTIVITY_RECEIVER = "RESPONSE_MOVIE_ACTIVITY_RECEIVER";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseJson = intent.getStringExtra(MyConst.EXTRA_RESPONSE_JSON);
            Gson gson = new GsonBuilder().create();
            Movie movie = gson.fromJson(responseJson, Movie.class);
            Picasso.with(context).load(MyConst.urlDefaultImage +movie.getPosterPath()).into(moviePoster);
            movieTitle.setText(movie.getOriginalTitle());
            sinopsis.setText(movie.getOverview());
            movieYear.setText(new SimpleDateFormat("yyyy").format(movie.getReleaseDate()));
            rateMovie.setText(Float.toString(movie.getVoteAverage()));
            voteCount.setText("("+Integer.toString(movie.getVoteCount())+")");

            String countriesProduct = null;
            for (int i=0; i<movie.getProductionCountriesList().size(); i++){
                countriesProduct = movie.getProductionCountriesList().get(i).getName();
                if (movie.getProductionCountriesList().size() != i+1){
                    countriesProduct = countriesProduct+", ";
                }
            }
            countriesProduct = countriesProduct != null ? "("+countriesProduct+")" : null;
            countries.setText(countriesProduct);
        }
    }
}
