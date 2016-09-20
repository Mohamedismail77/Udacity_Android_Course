package app.com.company.startup.movies;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.RelativeLayout;


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
import java.util.List;

import data.MovieReaderContract.TrailersEntry;
import data.MovieReaderDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class Details_ActivityFragment extends Fragment {

    public RequestQueue mrequestQueue;

    public Movie movie;

    public DetailsAdabter detailsAdabter;




    public Details_ActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        movie = bundle.getParcelable("movie");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout detail_layout = (RelativeLayout) inflater.inflate(R.layout.fragment_details_, container, false);
        ListView listView = (ListView) detail_layout.findViewById(R.id.movie_details_list);


            detailsAdabter = new DetailsAdabter(getActivity(), movie);
            listView.setAdapter(detailsAdabter);

            if(movie.getmFavorite() > 0) {
                getTrailersFromDb();
            } else {
                mrequestQueue  = Volley.newRequestQueue(getActivity().getApplicationContext());
                FetshTrailersJsonData();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position > 0) {

                        Uri.Builder url = new Uri.Builder();
                        url.scheme("https")
                                .authority("youtube.com")
                                .appendPath("watch")
                                .appendQueryParameter("v", movie.getmTrailers_keys().get(position - 1));


                        Intent imIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.build().toString()));
                        PackageManager packageManager = getActivity().getPackageManager();
                        List activities = packageManager.queryIntentActivities(imIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        boolean isIntentSafe = activities.size() > 0;

                        // Start an activity if it's safe
                        if (isIntentSafe) {
                            startActivity(imIntent);
                        }


                    }

                }
            });





        return detail_layout;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public  void FetshTrailersJsonData() {

        final Uri.Builder url = new Uri.Builder();
        url.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(movie.getmID()))
                .appendPath("videos")
                .appendQueryParameter("api_key","b0556b9a5c20ae2d3dcb3c4fd463c6de");
        String URL = url.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ArrayList<String> trailer_name = new ArrayList<>() ;
                ArrayList<String> trailer_key = new ArrayList<>();
                try {
                    JSONArray result = response.getJSONArray("results");

                    for(int i = 0; i < result.length(); i++) {

                        trailer_name.add(result.getJSONObject(i).getString("name"));
                        trailer_key.add(result.getJSONObject(i).getString("key"));

                    }

                    movie.setmTrailers_keys(trailer_key);
                    movie.setmTrailers_names(trailer_name);


                    detailsAdabter.notifyDataSetChanged();


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


    public void getTrailersFromDb() {
        MovieReaderDbHelper movieReaderDbHelper = new MovieReaderDbHelper(getActivity());
        SQLiteDatabase db = movieReaderDbHelper.getReadableDatabase();

        String selection = TrailersEntry.TRAILER_MOVIE_ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(movie.getmID()) };

        ArrayList<String> trailer_name = new ArrayList<>() ;
        ArrayList<String> trailer_key = new ArrayList<>();

        Cursor c = db.query(
                TrailersEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while(c.moveToNext()) {

            trailer_name.add(c.getString(c.getColumnIndex(TrailersEntry.TRAILER_TITLE_COLUMN_NAME)));
            trailer_key.add(c.getString(c.getColumnIndex(TrailersEntry.TRAILER_YOUTUBE_KEY_COLUMN_NAME)));

        }

        movie.setmTrailers_keys(trailer_key);
        movie.setmTrailers_names(trailer_name);


        detailsAdabter.notifyDataSetChanged();

        c.close();
        db.close();

    }





}
