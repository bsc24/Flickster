package com.example.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flickster.DetailActivity;
import com.example.flickster.MainActivity;
import com.example.flickster.R;
import com.example.flickster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "MovieAdapter";
    private static final int MOVIE = 0, POPULAR_MOVIE = 1;
    private static final int ROUNDING_RADIUS = 50;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MOVIE || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View v1 = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder =  new MovieViewHolder(v1);
        } else {    //  if (viewType == POPULAR_MOVIE)
            View v2 = inflater.inflate(R.layout.item_popular_movie, parent, false);
            viewHolder = new PopularMovieViewHolder(v2);
        } /*else {
            Log.e(TAG, "Invalid viewType value provided: " + viewType);
            return null;
        }*/
        return viewHolder;
    }

    // Involves populating the data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        if (holder.getItemViewType() == MOVIE || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MovieViewHolder mvh = (MovieViewHolder) holder;     // Cast as a MovieViewHolder
            mvh.bind(movie);
        } else {    //  if (viewType == POPULAR_MOVIE)
            PopularMovieViewHolder pmvh = (PopularMovieViewHolder) holder;
            pmvh.bind(movie);
        }
        //holder.bind(movie);
    }

    // Returns total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        double rating = movies.get(position).getRating();
        if (rating > 5)
            return POPULAR_MOVIE;
        else
            return MOVIE;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvRating;
        TextView tvOverview;
        ImageView ivPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.rlMovies);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvRating.setText("Rating: " + movie.getRating());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int height = 0;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                height = 800;
            } else {
                imageUrl = movie.getPosterPath();
                height = 470;
            }

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .override(1600, 470)
                    .fitCenter()
                    .transform(new RoundedCorners(ROUNDING_RADIUS))
                    .into(ivPoster);

            // Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }


    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        ImageView ivPoster;

        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.rlMovies);
        }

        public void bind(Movie movie) {
            String imageUrl = movie.getBackdropPath();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .override(1200, 605)
                    .fitCenter()
                    .transform(new RoundedCorners(ROUNDING_RADIUS))
                    .into(ivPoster);
            // Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }


}
