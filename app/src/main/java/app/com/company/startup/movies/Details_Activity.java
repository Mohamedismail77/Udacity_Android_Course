package app.com.company.startup.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import data.MovieReaderContract.*;
import data.MovieReaderDbHelper;


public class Details_Activity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            toolbar.setLogo(R.drawable.play);
        }


        setTitle(" Movie Details");

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mMovie = this.getIntent().getExtras().getParcelable("details");

        Fragment fb = new Details_ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", mMovie);
        fb.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.detail_container,fb);
        ft.commit();


    }


    public void AddOrRemove(View V){


        //TODO add movie to data base
        MovieReaderDbHelper movieReaderDbHelper = new MovieReaderDbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = movieReaderDbHelper.getWritableDatabase();
        if(V.getTag().equals("add")) {
            Picasso.with(this).load(mMovie.getmPosterUrl()).into(picassoImageTarget(this, "posters", mMovie.getmTitle().concat(".jpeg")));


            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieEntry.MOVIE_TITLE_COLUMN_NAME,mMovie.getmTitle());
            values.put(MovieEntry.MOVIE_RELEASE_DATE_COLUMN_NAME,mMovie.getmReleaseDate());
            values.put(MovieEntry.MOVIE_RATING_COLUMN_NAME,mMovie.getmRate());
            values.put(MovieEntry.MOVIE_OVERVIEW_COLUMN_NAME,mMovie.getmOverView());
            values.put(MovieEntry.MOVIE_POSTER_LOCATION_COLUMN_NAME,mMovie.getmTitle());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(MovieEntry.TABLE_NAME, null, values);
            if(newRowId < 0) {
                Toast.makeText(this,"Movie already in the favorite list",Toast.LENGTH_SHORT).show();
            } else {

                //TODO add trailers to database

                for(int i = 0; i < mMovie.getmTrailers_names().size(); i++) {
                    values.clear();
                    values.put(TrailersEntry.TRAILER_TITLE_COLUMN_NAME, mMovie.getmTrailers_names().get(i));
                    values.put(TrailersEntry.TRAILER_YOUTUBE_KEY_COLUMN_NAME, mMovie.getmTrailers_keys().get(i));
                    values.put(TrailersEntry.TRAILER_MOVIE_ID_COLUMN_NAME, newRowId);
                    db.insert(TrailersEntry.TABLE_NAME, null, values);
                }

                Toast.makeText(this,"Movie add sucessfully in the favorite list",Toast.LENGTH_SHORT).show();
            }
            db.close();

        } else{
            //TODO remove the movie from database

            // Define 'where' part of query.
            String movieSelection = MovieEntry.MOVIE_TITLE_COLUMN_NAME + " LIKE ?";
            String trailerSelection = TrailersEntry.TRAILER_MOVIE_ID_COLUMN_NAME + " LIKE ?";

            // Specify arguments in placeholder order.
            String[] movieSelectionArgs = { mMovie.getmTitle() };
            String[] trailerSelectionArgs = { String.valueOf(mMovie.getmID()) };

            // Issue SQL statement.
            db.delete(MovieEntry.TABLE_NAME, movieSelection, movieSelectionArgs);
            db.delete(TrailersEntry.TABLE_NAME, trailerSelection, trailerSelectionArgs);
            db.close();

            // Delete poster image from the phone.
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("posters", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, mMovie.getmTitle().concat(".jpeg"));

            if(myImageFile.delete()) {
                Toast.makeText(this,"Movie removed sucessfully",Toast.LENGTH_SHORT).show();
            }

        }
    }



    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }



}
