package app.com.company.startup.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import data.MovieReaderDbHelper;
import data.MovieReaderContract.MovieEntry;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwobane;
    private Movie mMovie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.play);

        if (findViewById(R.id.detail_container) != null) {

            mTwobane = true;


        } else {

            mTwobane = false;

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Movie movie) {

        if (mTwobane) {

            mMovie = movie;
            Fragment fb = new Details_ActivityFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);
            fb.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.detail_container, fb);
            ft.commit();


        } else {

            Intent intent = new Intent(MainActivity.this, Details_Activity.class);
            intent.putExtra("details", movie);
            startActivity(intent);

        }
    }

    public void AddOrRemove(View V) {

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
            String selection = MovieEntry.MOVIE_TITLE_COLUMN_NAME + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { mMovie.getmTitle() };
            // Issue SQL statement.
            db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
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
