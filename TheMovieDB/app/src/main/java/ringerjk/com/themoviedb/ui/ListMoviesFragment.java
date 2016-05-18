package ringerjk.com.themoviedb.ui;


import android.app.Activity;
import android.app.SearchManager;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.adapter.CustomMoviesListAdapter;
import ringerjk.com.themoviedb.entity.MovieInList;
import ringerjk.com.themoviedb.entity.SearchMovies;
import ringerjk.com.themoviedb.service.RequestListFilmActService;

public class ListMoviesFragment extends Fragment {


    private ListMoviesFragmentBroadcastReceiver receiver;
    private ListView listViewMovies;

    public interface SelectMovieListener {
        public void selectMovie(int id);
    }

    SelectMovieListener selectMovieListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            Log.i(MyConst.MY_LOG, "context instanceof Activity");
            a = (Activity) context;
            try {
                selectMovieListener = (SelectMovieListener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(ListMoviesFragmentBroadcastReceiver.INTENT_FILTER_LIST_FILM);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ListMoviesFragmentBroadcastReceiver();
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.flagment_list_movies, null);
        listViewMovies = (ListView) v.findViewById(R.id.listViewMovies2);
        handleIntent(getActivity().getIntent());
        return v;
    }
    private void handleIntent(Intent intent){
        Intent intentRequestListMoviesFragmentService = new Intent(getActivity(), RequestListFilmActService.class);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            intentRequestListMoviesFragmentService.putExtra(MyConst.EXTRA_REQUEST_URI_PATH, MyConst.pathUrlSearchMovie);
            intentRequestListMoviesFragmentService.putExtra(MyConst.QUERY_PATH, query);
        } else {
            String pathUrl = intent.getExtras().getString(MyConst.PATH_URL);
            intentRequestListMoviesFragmentService.putExtra(MyConst.EXTRA_REQUEST_URI_PATH, pathUrl);
        }
        intentRequestListMoviesFragmentService.putExtra(MyConst.INTENT_FILTER, ListMoviesFragmentBroadcastReceiver.INTENT_FILTER_LIST_FILM);
        getActivity().startService(intentRequestListMoviesFragmentService);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMovieInfo(view.getId());
            }
        });
    }

    private void showMovieInfo(int id) {
        selectMovieListener.selectMovie(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public class ListMoviesFragmentBroadcastReceiver extends BroadcastReceiver {
        public static final String INTENT_FILTER_LIST_FILM = "INTENT_FILTER_LIST_FILM";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MyConst.MY_LOG, "onReceive by ListFilmActivityBroadcastReceiver");
            String responseJson = intent.getStringExtra(MyConst.EXTRA_RESPONSE_JSON);
            Gson gson = new GsonBuilder().create();
            SearchMovies searchMovies = gson.fromJson(responseJson, SearchMovies.class);
            List<MovieInList> moviesList = searchMovies.moviesList;
            CustomMoviesListAdapter adapter = new CustomMoviesListAdapter(getActivity().getApplicationContext(), moviesList);
            listViewMovies.setAdapter(adapter);

        }
    }
}
