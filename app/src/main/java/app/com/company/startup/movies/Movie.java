package app.com.company.startup.movies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/*************************************
 * Created by MonaIsmail on 8/10/2016.
 **************************************/

public class Movie implements Parcelable {

    private String mPoster;
    private String mTitle;
    private String mOverView;
    private String mReleaseDate;
    private ArrayList<String> mTrailers_names;
    private ArrayList<String> mTrailers_keys;
    private ArrayList<String> mReviews;

    private ArrayList<MovieDetails> mMovieAdapter;

    private Double mRate;
    private int mID;
    private int mFavorite;




    public Movie(String poster, String title, String overView,String releaseDate, Double rate,int id,int fav) {

        mPoster = poster;
        mTitle = title;
        mRate = rate;
        mOverView = overView;
        mReleaseDate = releaseDate;
        mID = id;
        mFavorite = fav;
        mTrailers_names = new ArrayList<>();
        mTrailers_keys = new ArrayList<>();
        mReviews = new ArrayList<>();
        mMovieAdapter = new ArrayList<>();
        createAdapter();

    }

    public void setmTrailers_names(ArrayList<String> mTrailers_names) {
        this.mTrailers_names = mTrailers_names;
    }

    public void setmTrailers_keys(ArrayList<String> mTrailers_keys) {
        this.mTrailers_keys = mTrailers_keys;
    }

    public void setmReviews(ArrayList<String> mReviews) {
        this.mReviews = mReviews;
        mMovieAdapter = createAdapter();
    }

    public ArrayList<String> getmTrailers_names() {
        return mTrailers_names;
    }

    public ArrayList<String> getmTrailers_keys() {
        return mTrailers_keys;
    }

    public ArrayList<String> getmReviews() {
        return mReviews;
    }

    public String getmOverView() {
        return mOverView;
    }

    public Double getmRate() {
        return mRate;
    }

    public String getReadableRate(Double vote) {
        return vote.toString().concat("/10");
    }

    public String getmPosterUrl() {

        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(mPoster.replace("/",""));

        return url.build().toString();
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public int getmID() {
        return mID;
    }

    public int getmFavorite() {
        return mFavorite;
    }

    public ArrayList<MovieDetails> getmMovieAdapter() {
        return mMovieAdapter;
    }

    protected Movie(Parcel in) {
        mPoster = in.readString();
        mTitle = in.readString();
        mOverView = in.readString();
        mReleaseDate = in.readString();
        mRate = in.readDouble();
        mID = in.readInt();
        mFavorite = in.readInt();
        mTrailers_names = in.readArrayList(ClassLoader.getSystemClassLoader());
        mTrailers_keys = in.readArrayList(ClassLoader.getSystemClassLoader());
        mReviews = in.readArrayList(ClassLoader.getSystemClassLoader());
        mMovieAdapter = in.readArrayList(ClassLoader.getSystemClassLoader());



    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeString(mOverView);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mRate);
        dest.writeInt(mID);
        dest.writeInt(mFavorite);
        dest.writeStringList(mTrailers_names);
        dest.writeStringList(mTrailers_keys);
        dest.writeStringList(mReviews);
        dest.writeList(mMovieAdapter);


    }

    private ArrayList createAdapter() {

        ArrayList temp = new ArrayList();

        MovieDetails details = new MovieDetails("Head",mTitle);
        temp.add(details);
        for (String obj:mTrailers_names)
        {

            details = new MovieDetails("Trailer",obj);
            details.mKey = mTrailers_keys.get(mTrailers_names.indexOf(obj));
            temp.add(details);
        }

        for (String obj:mReviews)
        {
            details = new MovieDetails("review",obj);
            temp.add(details);
        }
        return temp;

    }

    public class MovieDetails implements Parcelable{

        public String mTag;
        public String mValue;
        public String mKey;

        public MovieDetails(String tag, String value) {

            mTag = tag;
            mValue = value;

        }


        protected MovieDetails(Parcel in) {
            mTag = in.readString();
            mValue = in.readString();
            mKey = in.readString();
        }

        public final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
            @Override
            public MovieDetails createFromParcel(Parcel in) {
                return new MovieDetails(in);
            }

            @Override
            public MovieDetails[] newArray(int size) {
                return new MovieDetails[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {


            dest.writeString(mTag);
            dest.writeString(mValue);
            dest.writeString(mKey);
        }
    }
}





