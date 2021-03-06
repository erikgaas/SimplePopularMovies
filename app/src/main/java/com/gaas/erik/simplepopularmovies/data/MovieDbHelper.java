package com.gaas.erik.simplepopularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by erik on 8/22/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public  MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " TEXT PRIMARY KEY, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_FOREIGN + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT PRIMARY KEY, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_FOREIGN + " INTEGER NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROPT TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
