package com.wistron.androidaaexample;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDataBase extends RoomDatabase {
    private static NoteDataBase instance;

    public abstract NoteDao noteDao();   //code generate by ROOM

    //only one thread can access this database
    public static synchronized NoteDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDataBase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("Alex", "alex111");
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("Alex", "alex");
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }

        private PopulateDbAsyncTask(NoteDataBase db){
            noteDao = db.noteDao();
        }
    }
}
