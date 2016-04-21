package ringerjk.com.themoviedb.entity;

import com.google.gson.annotations.SerializedName;

public class ProductionCountries {

    @SerializedName("iso_3166_1")
    private String iso;
    @SerializedName("name")
    private String name;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
