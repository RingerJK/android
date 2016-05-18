package ringerjk.com.themoviedb.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.adapter.CustomCastsListAdapter;
import ringerjk.com.themoviedb.entity.CastsList;
import ringerjk.com.themoviedb.entity.Movie;
import ringerjk.com.themoviedb.service.RequestListFilmActService;

public class ListCastsFragment extends Fragment {

    private GridView gridViewCasts;

    private ListCastsFragmentBroadcastReceiver receiver;

    public static ListCastsFragment newInstance(int id){
        ListCastsFragment fragment = new ListCastsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConst.ID_MOVIE_REQUEST, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_casts, null);

        int id = getMovieId();

        IntentFilter intentFilter = new IntentFilter(ListCastsFragmentBroadcastReceiver.INTENT_FILTER_LIST_CASTS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ListCastsFragmentBroadcastReceiver();
        getActivity().registerReceiver(receiver, intentFilter);

        Intent intentRequestListCastsFragmentService = new Intent(getActivity(), RequestListFilmActService.class);
        intentRequestListCastsFragmentService.putExtra(MyConst.EXTRA_REQUEST_URI_PATH, "/movie/"+id+"/credits");
        intentRequestListCastsFragmentService.putExtra(MyConst.INTENT_FILTER, ListCastsFragmentBroadcastReceiver.INTENT_FILTER_LIST_CASTS);
        getActivity().startService(intentRequestListCastsFragmentService);

        gridViewCasts = (GridView) v.findViewById(R.id.gridViewCasts);

        return v;
    }

    public int getMovieId() {
        int id = 0;
        try {
            id = getArguments().getInt(MyConst.ID_MOVIE_REQUEST, 0);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Ошибка поиска id для списка актеров", Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public class ListCastsFragmentBroadcastReceiver extends BroadcastReceiver{
        public static final String INTENT_FILTER_LIST_CASTS = "INTENT_FILTER_LIST_CASTS";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseJson = intent.getStringExtra(MyConst.EXTRA_RESPONSE_JSON);
            Gson gson = new GsonBuilder().create();
            CastsList castsList = gson.fromJson(responseJson, CastsList.class);
            CustomCastsListAdapter adapter = new CustomCastsListAdapter(getContext(), castsList.getCastInLists());
            gridViewCasts.setAdapter(adapter);
        }
    }
}
