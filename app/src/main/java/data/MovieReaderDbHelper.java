package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import data.MovieReaderContract.MovieEntry;


/**
 * Created by mohamed on 9/11/2016.
 */
public class MovieReaderDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.MOVIE_TITLE_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.MOVIE_RATING_COLUMN_NAME + " REAL" + COMMA_SEP +
                    MovieEntry.MOVIE_RELEASE_DATE_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.MOVIE_OVERVIEW_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    MovieEntry.MOVIE_POSTER_LOCATION_COLUMN_NAME + TEXT_TYPE+" )";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovieReader.db";

    public MovieReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
