package app.com.company.startup.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MonaIsmail on 8/16/2016.
 */
public class GridViewAdabter extends ArrayAdapter<Movie> {
    public GridViewAdabter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.poster_layout, parent, false);
        }

        ImageView PosterImage = (ImageView) convertView.findViewById(R.id.poster);
        Picasso.with(getContext())
                .load(movie.getmPosterUrl())
                .into(PosterImage);

        TextView PosterName = (TextView) convertView.findViewById(R.id.movie_rate);
        PosterName.setText(String.valueOf(movie.getmRate()));



        return convertView;

    }
}
