package com.hadzem.mojaaplikacija.interfaces;

import com.hadzem.mojaaplikacija.classes.MoviesResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by hadze_000 on 10/30/2015.
 */
public interface ServiceGenerator{
    @GET("/discover/movie")
    public void getFeed(@Query ("api_key") String api_key, @Query("sort_by") String sort, @Query("page") String pageNum,  Callback<MoviesResponse> response);
    //MoviesResponse getFeed(@Query("api_key") String api_key );
}
