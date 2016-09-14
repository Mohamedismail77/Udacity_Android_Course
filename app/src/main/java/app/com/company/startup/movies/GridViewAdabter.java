package app.com.company.startup.movies;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by MonaIsmail on 8/16/2016.
 */
public class GridViewAdabter extends ArrayAdapter<Movie> {

    private Context mContext;

    public GridViewAdabter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        mContext = context;
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

        if(movie.getmFavorite() > 0) {
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("posters", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, movie.getmTitle().concat(".jpeg"));
            Picasso.with(mContext).load(myImageFile).into(PosterImage);
        } else {
            Picasso.with(mContext)
                    .load(movie.getmPosterUrl())
                    .into(PosterImage);
        }


        TextView PosterName = (TextView) convertView.findViewById(R.id.movie_rate);
        PosterName.setText(String.valueOf(movie.getmRate()));



        return convertView;

    }
}
