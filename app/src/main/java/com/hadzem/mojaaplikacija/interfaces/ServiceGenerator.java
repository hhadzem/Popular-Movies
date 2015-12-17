package com.hadzem.mojaaplikacija.interfaces;

import com.hadzem.mojaaplikacija.classes.LinkResponse;
import com.hadzem.mojaaplikacija.classes.MoviesResponse;
import com.hadzem.mojaaplikacija.classes.ReviewsResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by hadze_000 on 10/30/2015.
 */
public interface ServiceGenerator{
    @GET("/discover/movie")
    public void getFeed(@Query ("api_key") String api_key, @Query("sort_by") String sort, @Query("page") String pageNum,  Callback<MoviesResponse> response);

    @GET("/movie/{id}/videos")
    public void getVideo(@Path("id") int id, @Query("api_key") String api_key, Callback<LinkResponse> link);

    @GET("/movie/{id}/reviews")
    public void getReview(@Path("id") int id, @Query("api_key") String api_key, Callback<ReviewsResponse> reviews);
}
