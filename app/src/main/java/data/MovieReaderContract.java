package data;

import android.provider.BaseColumns;

/**********************************
 * Created by mohamed on 9/11/2016.
 **********************************/
public class MovieReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MovieReaderContract() {}


    /* Inner class that defines the table contents */

    public static class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_TITLE_COLUMN_NAME = "title";
        public static final String MOVIE_RATING_COLUMN_NAME = "rate";
        public static final String MOVIE_OVERVIEW_COLUMN_NAME = "overview";
        public static final String MOVIE_POSTER_LOCATION_COLUMN_NAME = "posterUri";
        public static final String MOVIE_RELEASE_DATE_COLUMN_NAME = "releaseDate";


    }

    public static class TrailersEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailers";
        public static final String TRAILER_MOVIE_ID_COLUMN_NAME = "movie_id";
        public static final String TRAILER_TITLE_COLUMN_NAME = "title";
        public static final String TRAILER_YOUTUBE_KEY_COLUMN_NAME = "key";


    }

    public static class ReviewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";
        public static final String REVIEW_MOVIE_ID_COLUMN_NAME = "movie_id";
        public static final String REVIEW_CONTENT_COLUMN_NAME = "content";

    }

}
