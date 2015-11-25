package com.hadzem.mojaaplikacija.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hadzem.mojaaplikacija.classes.ApiManager;
import com.hadzem.mojaaplikacija.classes.ImageSizes;
import com.hadzem.mojaaplikacija.classes.Movie;
import com.hadzem.mojaaplikacija.classes.MoviesResponse;
import com.hadzem.mojaaplikacija.interfaces.FindMovie;
import com.hadzem.mojaaplikacija.MainActivity;
import com.hadzem.mojaaplikacija.R;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleFragment extends Fragment {
    final static String ARG_POSITION = "position";
    private int mCurrentPosition = -1;
    protected View inflatedView;
    private MoviesResponse moviesResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        inflatedView = inflater.inflate(R.layout.article_view, container, false);
        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getActivity().findViewById(R.id.fragment_container) != null) {
            MainActivity main = (MainActivity) getActivity();
            main.getSupportActionBar().setTitle(getContext().getString(R.string.details));
            main.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle args = getArguments();
        if (args != null) {// Set article based on argument passed in
            mCurrentPosition = args.getInt("position");
            moviesResponse = (MoviesResponse) args.getSerializable("movies");
            updateArticleView(args.getInt("position"));
        } else if (mCurrentPosition != -1) {
            updateArticleView(mCurrentPosition);
        }
    }


    public void setPosition(Bundle state){
        moviesResponse = (MoviesResponse) state.getSerializable("movies");
        mCurrentPosition = state.getInt("position");
        updateArticleView(mCurrentPosition);
    }
    public void refreshFeed(){
        updateArticleView(mCurrentPosition);
    }

    public void updateArticleView(int position) {
        mCurrentPosition = position;
        Movie movie = moviesResponse.getMovies().get(position);
        ImageView image = (ImageView) getActivity().findViewById(R.id.movie_thumbnail);
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String link = movie.getPoster_path();
        Picasso.with(getContext()).load(getActivity().getString(R.string.image_base_link) + ImageSizes.IMAGE_RESOLUTION_342 + link).into(image);
        TextView title = (TextView) getActivity().findViewById(R.id.movie_title);
        title.setText(movie.getTitle());
        title.setTextSize(20);
        TextView releaseDate = (TextView) getActivity().findViewById(R.id.release_date);
        releaseDate.setText(movie.getRelease_date());
        TextView rating = (TextView) getActivity().findViewById(R.id.rating);
        String string = String.format("%.1f", movie.getVote_average());
        rating.setText(string);
        rating.setTextSize(50);
        TextView overview = (TextView) getActivity().findViewById(R.id.overview);
        overview.setText(movie.getOverview());
        RelativeLayout rLayout = (RelativeLayout) getActivity().findViewById(R.id.article_layout);
        rLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
        overview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

}