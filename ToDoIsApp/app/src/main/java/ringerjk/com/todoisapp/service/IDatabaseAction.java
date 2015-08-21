package ringerjk.com.todoisapp.service;

import java.util.ArrayList;
import java.util.List;

import ringerjk.com.todoisapp.domain.Note;

public interface IDatabaseAction {

    public void addNote(Note note);

    public List<Note> getAllNotes();

    public ArrayList<String> getAllTitle();

    public void deleteNote(int _id);

    public void deleteAllNotes();


    }
