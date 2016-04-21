package ringerjk.com.themoviedb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Movie {

    @SerializedName("id")
    private int id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("budget")
    private int budget;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("production_countries")
    private List<ProductionCountries> productionCountriesList;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("revenue")
    private int revenue;
    @SerializedName("status")
    private String status;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<ProductionCountries> getProductionCountriesList() {
        return productionCountriesList;
    }

    public void setProductionCountriesList(List<ProductionCountries> productionCountriesList) {
        this.productionCountriesList = productionCountriesList;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
