package ringerjk.com.todoisapp.models;

import java.io.Serializable;

public class Note implements Serializable {

    public static final String KEY_NOTE = "Note";
    public int _id;
    private String title;
    private String description;

    public Note() {
    }

    public Note(int _id, String title, String description) {
        this._id = _id;
        this.title = title;
        this.description = description;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
