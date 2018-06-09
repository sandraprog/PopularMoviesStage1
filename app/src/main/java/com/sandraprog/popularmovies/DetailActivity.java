package com.sandraprog.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandraprog.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String POSTER_BIG_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView mPosterImage = (ImageView) findViewById(R.id.iv_poster);
        TextView mTitle = (TextView) findViewById(R.id.tv_title);
        TextView mReleaseDate = (TextView) findViewById(R.id.tv_date);
        TextView mOverview = (TextView) findViewById(R.id.tv_overview);
        TextView mVote = (TextView) findViewById(R.id.tv_vote);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movie")) {

            movie = getIntent().getParcelableExtra("movie");

            String posterPath = POSTER_BIG_BASE_URL + movie.getBackdropPath();
            Picasso.with(this).load(posterPath).into(mPosterImage);

            String title = movie.getOriginalTitle();
            mTitle.setText(movie.getOriginalTitle());
            mReleaseDate.setText(movie.getReleaseDate());
            mVote.setText(String.valueOf(movie.getVoteAverage()));
            mOverview.setText(movie.getOverview());

        } else {
            Toast.makeText(this, R.string.no_data_text, Toast.LENGTH_SHORT).show();
        }

    }
}
