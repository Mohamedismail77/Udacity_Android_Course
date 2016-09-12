package app.com.company.startup.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwobane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.play);

        if(findViewById(R.id.detail_container) != null) {

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
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Movie movie) {

        if(mTwobane) {
            //Toast.makeText(getApplicationContext(),"Two bane activated",Toast.LENGTH_SHORT).show();
            Fragment fb = new Details_ActivityFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("movie",movie);
            fb.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.detail_container,fb);
            ft.commit();


        } else {

            Intent intent = new Intent(MainActivity.this,Details_Activity.class);
            intent.putExtra("details",movie);
            startActivity(intent);

        }
    }

    public void AddOrRemove(View V){

        Toast.makeText(this,"from main activity",Toast.LENGTH_SHORT).show();
        //TODO add movie to data base

    }
}
