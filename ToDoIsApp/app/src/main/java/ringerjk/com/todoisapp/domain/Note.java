package ringerjk.com.todoisapp.domain;

public class Note {

    int _id;
    private String _title;
    private String _description;

    public Note() {
    }

    public Note(int _id, String _title, String _description) {
        this._id = _id;
        this._title = _title;
        this._description = _description;
    }

    public Note(String _title, String _description) {
        this._title = _title;
        this._description = _description;
    }

    public long getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }
}
