package ringerjk.com.themoviedb.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastsList {

    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<CastInList> castInLists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CastInList> getCastInLists() {
        return castInLists;
    }

    public void setCastInLists(List<CastInList> castInLists) {
        this.castInLists = castInLists;
    }
}
