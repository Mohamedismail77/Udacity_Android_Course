package app.com.company.startup.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MonaIsmail on 8/19/2016.
 */
public class DetailsAdabter extends BaseAdapter{

    private Context mContext;
    private Movie mMovie;

    public DetailsAdabter(Context context,Movie movie) {

        this.mContext = context;
        this.mMovie = movie;
    }

    @Override
    public int getCount() {
        return mMovie.getmTrailers_names().size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(position == 0) {


            if(convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_details,parent,false);

            }

            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.poster_thumbnail);
            TextView originalTitle = (TextView) convertView.findViewById(R.id.original_title);
            TextView releaseDaate = (TextView) convertView.findViewById(R.id.date);
            TextView vote = (TextView) convertView.findViewById(R.id.vote);
            TextView overView = (TextView) convertView.findViewById(R.id.over_view);

            originalTitle.setText(mMovie.getmTitle());
            releaseDaate.setText(mMovie.getmReleaseDate());
            vote.setText(mMovie.getReadableRate(mMovie.getmRate()));
            overView.setText(mMovie.getmOverView());

            Picasso.with(mContext)
                    .load(mMovie.getmPosterUrl())
                    .into(thumbnail);

            return convertView;

        } else {

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_template,parent,false);
            }

            TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
            if(mMovie.getmTrailers_names().size() > 0) {
                trailerName.setText(mMovie.getmTrailers_names().get(position-1));
            }
            return  convertView;
        }

    }
}
