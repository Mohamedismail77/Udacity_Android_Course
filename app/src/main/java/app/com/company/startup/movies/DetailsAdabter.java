package app.com.company.startup.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*************************************
 * Created by MonaIsmail on 8/19/2016.
 ************************************/

public class DetailsAdabter extends BaseAdapter{

    private Context mContext;
    private Movie mMovie;



    public DetailsAdabter(Context context, Movie movie) {

        this.mContext = context;
        this.mMovie = movie;


    }

    @Override
    public int getCount() {
        return mMovie.getmMovieAdapter().size();
    }

    @Override
    public Movie.MovieDetails getItem(int position) {

        if(mMovie.getmMovieAdapter().size() > 0) {
            return mMovie.getmMovieAdapter().get(position);
        } else{
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mMovie.getmMovieAdapter().get(position).mTag) {
            case "Head":
                return 0;
            case "Trailer":
                return 1;
            default:
                return 2;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if(type == 0) {


            if(convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_details,parent,false);

            }

            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.poster_thumbnail);
            TextView originalTitle = (TextView) convertView.findViewById(R.id.original_title);
            TextView releaseDaate = (TextView) convertView.findViewById(R.id.date);
            TextView vote = (TextView) convertView.findViewById(R.id.vote);
            TextView overView = (TextView) convertView.findViewById(R.id.over_view);
            Button addOrRemove = (Button) convertView.findViewById(R.id.add_remove);

            originalTitle.setText(mMovie.getmTitle());
            releaseDaate.setText(mMovie.getmReleaseDate());
            vote.setText(mMovie.getReadableRate(mMovie.getmRate()));
            overView.setText(mMovie.getmOverView());

            if(mMovie.getmFavorite() > 0) {
                addOrRemove.setText("Remove from favorite");
                addOrRemove.setTag("remove");
                ContextWrapper cw = new ContextWrapper(mContext);
                File directory = cw.getDir("posters", Context.MODE_PRIVATE);
                File myImageFile = new File(directory, mMovie.getmTitle().concat(".jpeg"));
                Picasso.with(mContext).load(myImageFile).into(thumbnail);
            } else {
                addOrRemove.setText("Add to favorite");
                addOrRemove.setTag("add");
                Picasso.with(mContext)
                        .load(mMovie.getmPosterUrl())
                        .into(thumbnail);
            }



            return convertView;

        } else if(type == 1) {

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_template,parent,false);
            }

            TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
            if(mMovie.getmMovieAdapter().get(position).mValue != null) {
                 trailerName.setText(mMovie.getmMovieAdapter().get(position).mValue);
            }
            return  convertView;
        } else {

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.reviews_template,parent,false);
            }

            TextView review = (TextView) convertView.findViewById(R.id.reviews);
            if(mMovie.getmMovieAdapter().get(position).mValue != null) {
                review.setText(mMovie.getmMovieAdapter().get(position).mValue);
            }
            return convertView;
        }

    }


}
