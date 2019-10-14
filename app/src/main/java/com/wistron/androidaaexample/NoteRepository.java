package com.wistron.androidaaexample;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDataBase database = NoteDataBase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getALLNotes();
    }
    //room not allowed to run data base operation in main thread so you will need async task
    //Api expose to view model
    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    //Api expose to view model
    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    //Api expose to view model
    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    //Api expose to view model
    public void deleteAllNotes(){
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    //Api expose to view model
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;
        private DeleteAllNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteALL();
            return null;
        }
    }
}