package app.com.company.startup.movies;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import data.MovieReaderContract.MovieEntry;
import data.MovieReaderDbHelper;


/**************************************************
 * A placeholder fragment containing a simple view.
 **************************************************/

public class MainActivityFragment extends Fragment {


    private String API_URL_SCHEME  = "https";
    private String API_URL_AUTHORITY = "api.themoviedb.org";
    private String API_URL_PATH_1 = "3";
    private String API_URL_PATH_2 = "discover";
    private String API_URL_PATH_3 = "movie";
    private String API_URL_APP_KEY = "ADD YOUR API KEY";
    private String API_URL_QUERY = "popularity.desc";

    public RequestQueue mrequestQueue;

    public ArrayList<Movie> mMovies;
    public ArrayList<String> mMoviesTitles;
    public ArrayList<String> mPostersUrl;
    public  GridViewAdabter mGridAdapter;

    public Callback mCallback;


    public GridView mGrid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mrequestQueue  = Volley.newRequestQueue(getActivity().getApplicationContext());
        mMovies = new ArrayList<>();
        mMoviesTitles = new ArrayList<>();
        mPostersUrl = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String setting = sharedPreferences.getString("sort_by","0");

        switch (setting){
            case "0":
                API_URL_QUERY = "popularity.desc";
                getActivity().setTitle(" Popular Movie");
                FetchJsonData();
                break;
            case "-1":
                API_URL_QUERY = "vote_average.desc";
                getActivity().setTitle(" High Rated Movie");
                FetchJsonData();
                break;
            default:
                //TODO  get the saved movies from database
                getActivity().setTitle(" Favorite Movies");
                mGridAdapter = new GridViewAdabter(getActivity(),R.layout.poster_layout,mMovies);
                getMoviesFromDb();
                break;
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_main, container, false);

        mGrid = (GridView) frameLayout.findViewById(R.id.gridview);
        mGridAdapter = new GridViewAdabter(getActivity(),R.layout.poster_layout,mMovies);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCallback.onItemSelected(mMovies.get(position));

            }
        });

        mGrid.setAdapter(mGridAdapter);
        return frameLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallback = (Callback) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String setting = sharedPreferences.getString("sort_by","0");

        switch (setting){
            case "0":
                API_URL_QUERY = "popularity.desc";
                getActivity().setTitle(" Popular Movie");
                FetchJsonData();
                break;
            case "-1":
                API_URL_QUERY = "vote_average.desc";
                getActivity().setTitle(" High Rated Movie");
                FetchJsonData();
                break;
            default:
                //TODO  get the saved movies from database
                getActivity().setTitle(" Favorite Movies");
                mGridAdapter = new GridViewAdabter(getActivity(),R.layout.poster_layout,mMovies);
                getMoviesFromDb();
                break;
        }
    }

    public  void FetchJsonData() {

        Uri.Builder url = new Uri.Builder();
        url.scheme(API_URL_SCHEME)
                .authority(API_URL_AUTHORITY)
                .appendPath(API_URL_PATH_1)
                .appendPath(API_URL_PATH_2)
                .appendPath(API_URL_PATH_3)
                .appendQueryParameter("api_key",API_URL_APP_KEY)
                .appendQueryParameter("sort_by",API_URL_QUERY);
        String URL = url.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String posterPath ;
                String originalTitle;
                String overView;
                String releaseDate ;
                Double rate;
                int id;
                int favorite;

                try {
                    JSONArray result = response.getJSONArray("results");
                    mPostersUrl.clear();
                    mMoviesTitles.clear();
                    mGridAdapter.clear();

                    for(int i = 0; i < result.length(); i++) {


                         posterPath = result.getJSONObject(i).getString("poster_path");
                         originalTitle = result.getJSONObject(i).getString("original_title");
                         overView = result.getJSONObject(i).getString("overview");
                         releaseDate = result.getJSONObject(i).getString("release_date");
                         rate = result.getJSONObject(i).getDouble("vote_average");
                         id = result.getJSONObject(i).getInt("id");
                         favorite = 0;



                        Movie movie = new Movie(posterPath,originalTitle,overView,releaseDate,rate,id,favorite);
                        mMovies.add(movie);

                        mMoviesTitles.add(movie.getmTitle());
                        mPostersUrl.add(movie.getmPosterUrl());

                    }

                    mGridAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mrequestQueue.add(jsonObjectRequest);
    }



    public interface Callback{

        public void onItemSelected(Movie movie);

    }


    public void getMoviesFromDb() {
        MovieReaderDbHelper movieReaderDbHelper = new MovieReaderDbHelper(getActivity());
        SQLiteDatabase db = movieReaderDbHelper.getReadableDatabase();

        String posterPath ;
        String originalTitle;
        String overView;
        String releaseDate ;
        Double rate;
        int id;
        int favorite;

        mPostersUrl.clear();
        mMoviesTitles.clear();
        mGridAdapter.clear();
        Cursor c = db.query(
                MovieEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while(c.moveToNext()){



            posterPath = c.getString(c.getColumnIndex(MovieEntry.MOVIE_POSTER_LOCATION_COLUMN_NAME));
            originalTitle = c.getString(c.getColumnIndex(MovieEntry.MOVIE_TITLE_COLUMN_NAME));
            overView = c.getString(c.getColumnIndex(MovieEntry.MOVIE_OVERVIEW_COLUMN_NAME));
            releaseDate = c.getString(c.getColumnIndex(MovieEntry.MOVIE_RELEASE_DATE_COLUMN_NAME));
            rate = c.getDouble(c.getColumnIndex(MovieEntry.MOVIE_RATING_COLUMN_NAME));
            id = c.getInt(c.getColumnIndex(MovieEntry._ID));
            favorite = 1;

            Movie movie = new Movie(posterPath,originalTitle,overView,releaseDate,rate,id,favorite);
            mMovies.add(movie);

            mMoviesTitles.add(movie.getmTitle());
            mPostersUrl.add(movie.getmPosterUrl());
        }
        mGridAdapter.notifyDataSetChanged();
        c.close();
        db.close();

    }


}
