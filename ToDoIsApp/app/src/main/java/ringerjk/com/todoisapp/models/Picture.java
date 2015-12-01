package ringerjk.com.todoisapp.models;

import android.content.Intent;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Picture implements Serializable {

    public static final String KEY_PICTURE = "Note";
    int _id;
    private String imageLink;
    private int note_id;

    public Picture() {
    }

    public Picture(String imageLink, int note_id) {
        this.imageLink = imageLink;
        this.note_id = note_id;
    }

    public Picture(int _id, String imageLink, int note_id) {
        this._id = _id;
        this.imageLink = imageLink;
        this.note_id = note_id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }
}
