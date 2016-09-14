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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import data.MovieReaderContract;
import data.MovieReaderDbHelper;


public class Details_Activity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.play);
        setTitle(" Movie Details");

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        mMovie = this.getIntent().getExtras().getParcelable("details");

        Fragment fb = new Details_ActivityFragment();
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
            values.put(MovieReaderContract.MovieEntry.MOVIE_TITLE_COLUMN_NAME,mMovie.getmTitle());
            values.put(MovieReaderContract.MovieEntry.MOVIE_RELEASE_DATE_COLUMN_NAME,mMovie.getmReleaseDate());
            values.put(MovieReaderContract.MovieEntry.MOVIE_RATING_COLUMN_NAME,mMovie.getmRate());
            values.put(MovieReaderContract.MovieEntry.MOVIE_OVERVIEW_COLUMN_NAME,mMovie.getmOverView());
            values.put(MovieReaderContract.MovieEntry.MOVIE_POSTER_LOCATION_COLUMN_NAME,mMovie.getmTitle());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(MovieReaderContract.MovieEntry.TABLE_NAME, null, values);

            db.close();

        } else{
            //TODO remove the movie from database
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("posters", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, mMovie.getmTitle().concat(".jpeg"));
            if(myImageFile.delete()) {
                Toast.makeText(this,"Movie removed sucessfully",Toast.LENGTH_SHORT).show();
            }
            // Define 'where' part of query.
            String selection = MovieReaderContract.MovieEntry.MOVIE_TITLE_COLUMN_NAME + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { mMovie.getmTitle() };
            // Issue SQL statement.
            db.delete(MovieReaderContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
            db.close();

        }
    }



    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }



}
