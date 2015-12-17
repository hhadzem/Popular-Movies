package com.hadzem.mojaaplikacija.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hadzem.mojaaplikacija.classes.ApiManager;
import com.hadzem.mojaaplikacija.classes.ImageSizes;
import com.hadzem.mojaaplikacija.classes.LinkResponse;
import com.hadzem.mojaaplikacija.classes.Movie;
import com.hadzem.mojaaplikacija.classes.MoviesProvider;
import com.hadzem.mojaaplikacija.classes.MoviesResponse;
import com.hadzem.mojaaplikacija.classes.Review;
import com.hadzem.mojaaplikacija.classes.ReviewsResponse;
import com.hadzem.mojaaplikacija.classes.videoLink;
import com.hadzem.mojaaplikacija.interfaces.FindMovie;
import com.hadzem.mojaaplikacija.MainActivity;
import com.hadzem.mojaaplikacija.R;
import com.hadzem.mojaaplikacija.interfaces.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleFragment extends Fragment {
    final static String ARG_POSITION = "position";
    private int mCurrentPosition = -1;
    private Movie mCurrentMovie;
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

    public boolean isInDatabase(int position){
        Movie movie = moviesResponse.getMovies().get(position);
        int id = movie.getId();
        Cursor result = getContext().getContentResolver().query(
                MoviesProvider.MoviesContract.CONTENT_URI, null,
                MoviesProvider.MoviesContract.ID + " = ? ", new String[]{Integer.toString(id)}, "");
        if( result == null || result.getCount() == 0) {
            Log.d("DB", "Nema filma u bazi");
            if( result != null) result.close();
            return false;
        }
        else
            Log.d("DB", "Nasao film u bazi");
        result.close();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();

        if(getActivity().findViewById(R.id.fragment_container) != null) {
            MainActivity main = (MainActivity) getActivity();
            main.getSupportActionBar().setTitle(getContext().getString(R.string.details));
            main.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (args != null) {// Set article based on argument passed in
            mCurrentPosition = args.getInt("position");
            moviesResponse = (MoviesResponse) args.getSerializable("movies");
            updateArticleView(args.getInt("position"));
        }
    }

    private void addToFavorites(Movie movie){
        ContentValues values = new ContentValues();
        values.put(MoviesProvider.MoviesContract.TITLE, movie.getTitle());
        values.put(MoviesProvider.MoviesContract.OVERVIEW, movie.getOverview());
        values.put(MoviesProvider.MoviesContract.ID, movie.getId() );
        values.put(MoviesProvider.MoviesContract.RELEASE_DATE, movie.getRelease_date());
        values.put(MoviesProvider.MoviesContract.POPULARITY, movie.getVote_average());
        values.put(MoviesProvider.MoviesContract.IMAGE_URL_PATH, movie.getPoster_path());
        String finalReview = "";
        String separator = "__,__";
        for( int i = 0; i < movie.getReviews().size(); i++){
            finalReview = finalReview + movie.getReviews().get(i).getContent();
            if( i != movie.getReviews().size()-1)
                finalReview = finalReview + separator;
        }
        values.put(MoviesProvider.MoviesContract.REVIEWS, finalReview);
        String finalLink = "";
        for ( int i = 0; i < movie.getLinks().size(); i++) {
            finalLink = finalLink + movie.getLinks().get(i).getKey();
            if( i != movie.getLinks().size()-1)
                finalLink = finalLink + ',';
        }
        values.put(MoviesProvider.MoviesContract.TRAILER_LINKS, finalLink);

        getContext().getContentResolver().insert(MoviesProvider.MoviesContract.CONTENT_URI, values);
        Log.d("DB", "Dodao film u bazu");
    }

    private void removeFromFavorites(Movie movie){
        String whereClause = "ID" + "=?";
        String[] whereArgs = new String[] { String.valueOf(movie.getId()) };
        getContext().getContentResolver().delete(MoviesProvider.MoviesContract.CONTENT_URI, whereClause, whereArgs);
        Log.d("DB", "Obrisao film iz baze");
    }

    public void setPosition(Bundle state){
        moviesResponse = (MoviesResponse) state.getSerializable("movies");
        mCurrentPosition = state.getInt("position");
        updateArticleView(mCurrentPosition);
    }
    public void refreshFeed(){
        updateArticleView(mCurrentPosition);
    }


    private void loadMoviesAndReviews(){
        ServiceGenerator api = ApiManager.getAdapter(getContext()).create(ServiceGenerator.class);
        if(mCurrentMovie.getLinks().size() == 0)
        api.getVideo(mCurrentMovie.getId(),getActivity().getString(R.string.API_KEY),  new Callback<LinkResponse>() {
            @Override
            public void success(LinkResponse linkResponse, Response response) {
                mCurrentMovie.setLinks(linkResponse.getResults());
                LinearLayout llv = (LinearLayout) getActivity().findViewById(R.id.trailers);
                llv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));

                for(videoLink vL:mCurrentMovie.getLinks()){
                    TextView t = new TextView(getContext());
                    t.setTextColor(getResources().getColor(R.color.black));
//                    t.setText(vL.getKey());
                    t.setText(Html.fromHtml("<a href=\"http://www.youtube.com/watch?v="+vL.getKey()+"\">Trailer</a> "));
                    t.setMovementMethod(LinkMovementMethod.getInstance());
                    llv.addView(t);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("FEED_VIDEO", error.getMessage());
            }
        });
        else{
            LinearLayout llv = (LinearLayout) getActivity().findViewById(R.id.trailers);
            llv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
            for(videoLink vL:mCurrentMovie.getLinks()){
                TextView t = new TextView(getContext());
                t.setTextColor(getResources().getColor(R.color.black));
                t.setText(Html.fromHtml("<a href=\"http://www.youtube.com/watch?v=" + vL.getKey() + "\">Trailer</a> "));
                t.setMovementMethod(LinkMovementMethod.getInstance());
                llv.addView(t);
            }
        }
        if(mCurrentMovie.getReviews().size() == 0)
        api.getReview(mCurrentMovie.getId(), getActivity().getString(R.string.API_KEY), new Callback<ReviewsResponse>() {
            @Override
            public void success(ReviewsResponse reviewsResponse, Response response) {
                mCurrentMovie.setReviews(reviewsResponse.getResults());
                LinearLayout lw = (LinearLayout) getActivity().findViewById(R.id.review);
                lw.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
                int i = 0;
                for (Review rV : mCurrentMovie.getReviews()) {
                    if( i == 3) break; else i++;
                    TextView t = new TextView(getContext());
                    t.setTextColor(getResources().getColor(R.color.black));
                    t.setText(rV.getContent());
                    lw.addView(t);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("FEED_REVIEW", error.getMessage());
            }
        });
        else{
            LinearLayout lw = (LinearLayout) getActivity().findViewById(R.id.review);
            lw.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
            for (Review rV : mCurrentMovie.getReviews()) {
                TextView t = new TextView(getContext());
                t.setTextColor(getResources().getColor(R.color.black));
                t.setText(rV.getContent());
                lw.addView(t);
            }
        }
    }

    public void updateArticleView(int position) {
        if( isInDatabase(position) )
            ( (CheckedTextView) getActivity().findViewById(R.id.favoritesButton)).setChecked(true);
        else
            ( (CheckedTextView) getActivity().findViewById(R.id.favoritesButton)).setChecked(false);
        mCurrentMovie = moviesResponse.getMovies().get(position);
        loadMoviesAndReviews();
        getActivity().findViewById(R.id.favoritesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckedTextView) v).isChecked()) {
                    ((CheckedTextView) v).setChecked(false);
                    removeFromFavorites(mCurrentMovie);
                } else {
                    ((CheckedTextView) v).setChecked(true);
                    addToFavorites(mCurrentMovie);
                }
            }
        });
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
        String tekst = "Overview: \n" + movie.getOverview();
        overview.setText(tekst);
        RelativeLayout rLayout = (RelativeLayout) getActivity().findViewById(R.id.article_layout);
        rLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
        overview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
        CheckedTextView ctw = (CheckedTextView) getActivity().findViewById(R.id.favoritesButton);

        ctw.setVisibility(CheckedTextView.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

}