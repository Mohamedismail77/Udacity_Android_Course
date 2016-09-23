package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import data.MovieReaderContract.*;


/**********************************
 * Created by mohamed on 9/11/2016.
 **********************************/

public class MovieReaderDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MOVIE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.MOVIE_TITLE_COLUMN_NAME + TEXT_TYPE +" UNIQUE" + COMMA_SEP +
                    MovieEntry.MOVIE_RATING_COLUMN_NAME + " REAL" + COMMA_SEP +
                    MovieEntry.MOVIE_RELEASE_DATE_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.MOVIE_OVERVIEW_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.MOVIE_POSTER_LOCATION_COLUMN_NAME + TEXT_TYPE+" )";

    private static final String SQL_CREATE_TRAILER_ENTRIES =
            "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +
                    TrailersEntry._ID + " INTEGER PRIMARY KEY," +
                    TrailersEntry.TRAILER_MOVIE_ID_COLUMN_NAME + " INTEGER" + COMMA_SEP +
                    TrailersEntry.TRAILER_TITLE_COLUMN_NAME + TEXT_TYPE  + COMMA_SEP +
                    TrailersEntry.TRAILER_YOUTUBE_KEY_COLUMN_NAME + TEXT_TYPE+" )";

    private static final String SQL_CREATE_REVIEW_ENTRIES =
            "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                    ReviewsEntry._ID + " INTEGER PRIMARY KEY," +
                    ReviewsEntry.REVIEW_MOVIE_ID_COLUMN_NAME + " INTEGER" + COMMA_SEP +
                    ReviewsEntry.REVIEW_CONTENT_COLUMN_NAME + TEXT_TYPE+" )";


    private static final String SQL_DELETE_MOVIE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    private static final String SQL_DELETE_TRAILER_ENTRIES =
            "DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME;

    private static final String SQL_DELETE_REVIEW_ENTRIES =
            "DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MovieReader.db";

    public MovieReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL(SQL_CREATE_MOVIE_ENTRIES);
       db.execSQL(SQL_CREATE_TRAILER_ENTRIES);
        db.execSQL(SQL_CREATE_REVIEW_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MOVIE_ENTRIES);
        db.execSQL(SQL_DELETE_TRAILER_ENTRIES);
        db.execSQL(SQL_DELETE_REVIEW_ENTRIES);
        onCreate(db);
    }


}
