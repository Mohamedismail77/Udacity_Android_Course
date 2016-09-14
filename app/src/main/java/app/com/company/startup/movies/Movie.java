package app.com.company.startup.movies;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by MonaIsmail on 8/10/2016.
 */
public class Movie implements Parcelable {

    private String mPoster;
    private String mTitle;
    private String mOverView;
    private String mReleaseDate;
    private ArrayList<String> mTrailers_names;
    private ArrayList<String> mTrailers_keys;
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

    }

    public void setmTrailers_names(ArrayList<String> mTrailers_names) {
        this.mTrailers_names = mTrailers_names;
    }

    public void setmTrailers_keys(ArrayList<String> mTrailers_keys) {
        this.mTrailers_keys = mTrailers_keys;
    }


    public ArrayList<String> getmTrailers_names() {
        return mTrailers_names;
    }

    public ArrayList<String> getmTrailers_keys() {
        return mTrailers_keys;
    }

    public String getmOverView() {
        return mOverView;
    }

    public Double getmRate() {
        return mRate;
    }

    public String getReadableRate(Double vote) {
        String rate = vote.toString().concat("/10");
        return rate;
    }

    public String getmPosterUrl() {

        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(mPoster.replace("/",""));
        String URL = url.build().toString();

        return URL;
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


    }
}
