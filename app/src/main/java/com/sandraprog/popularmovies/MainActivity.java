package com.sandraprog.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sandraprog.popularmovies.adapter.MoviesAdapter;
import com.sandraprog.popularmovies.api.RetrofitClient;
import com.sandraprog.popularmovies.api.RetrofitInterface;
import com.sandraprog.popularmovies.model.Movie;
import com.sandraprog.popularmovies.model.MoviesList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private List<Movie> mList;
    private SharedPreferences preferences;
    private Menu mOptionsMenu;

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "insert api key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        initViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        mOptionsMenu = menu;
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        sortMovies(sortOrder);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                sortMovies(this.getString(R.string.pref_most_popular));
                return true;
            case R.id.menu_highest_rated:
                sortMovies(this.getString(R.string.pref_highest_rated));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, mList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void loadJSON(String sortOrder) {
        RetrofitClient client = new RetrofitClient();
        RetrofitInterface apiService = RetrofitClient.getClient().create(RetrofitInterface.class);

        Call<MoviesList> call;
        if (sortOrder.equals(this.getString(R.string.pref_most_popular)))
            call = apiService.getPopularMovies(API_KEY);
        else
            call = apiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                List<Movie> movies = response.body().getResults();
                mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                mRecyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.error_no_data_from_retrofit, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortMovies(String sortOrder) {

        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            mOptionsMenu.findItem(R.id.menu_most_popular).setVisible(false);
            mOptionsMenu.findItem(R.id.menu_highest_rated).setVisible(true);
            preferences.edit().putString(this.getString(R.string.pref_sort_order_key),
                    this.getString(R.string.pref_most_popular)).apply();
        } else {
            mOptionsMenu.findItem(R.id.menu_highest_rated).setVisible(false);
            mOptionsMenu.findItem(R.id.menu_most_popular).setVisible(true);
            preferences.edit().putString(this.getString(R.string.pref_sort_order_key),
                    this.getString(R.string.pref_highest_rated)).apply();
        }

        loadJSON(sortOrder);

    }


}
