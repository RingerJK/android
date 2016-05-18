package ringerjk.com.themoviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.entity.MovieInList;

public class CustomMoviesListAdapter extends ArrayAdapter<MovieInList> {

    List<MovieInList> moviesList;

    static class ViewHolder{
        TextView title;
        ImageView poster;
        TextView movieYear;
        TextView rateMovie;
    }

    public CustomMoviesListAdapter(Context context, List<MovieInList> objects) {
        super(context, R.layout.custom_movies_list_item, objects);
        moviesList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(MovieInList item) {
        return super.getPosition(item);
    }

    @Override
    public MovieInList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_movies_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.filmTitle);
            holder.poster = (ImageView) convertView.findViewById(R.id.posterFilm);
            holder.movieYear = (TextView) convertView.findViewById(R.id.movieYear);
            holder.rateMovie = (TextView) convertView.findViewById(R.id.rateMovie);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(moviesList.get(position).getOriginalTitle());
        Picasso.with(getContext()).load(MyConst.urlDefaultImage + moviesList.get(position).getPosterPath()).into(holder.poster);
        holder.movieYear.setText(new SimpleDateFormat("yyyy").format(moviesList.get(position).getReleaseDate()));
        holder.rateMovie.setText(Double.toString(moviesList.get(position).getVoteAverage()));
        convertView.setId(moviesList.get(position).getId());
        return convertView;
    }
}
