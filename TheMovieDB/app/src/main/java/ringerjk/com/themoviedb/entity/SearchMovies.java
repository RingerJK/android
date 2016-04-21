package ringerjk.com.themoviedb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchMovies {
    @SerializedName("page")
    public int pageNum;

    @SerializedName("results")
    public List<MovieInList> moviesList;
}
