package com.hadzem.mojaaplikacija.Interfaces;

import com.hadzem.mojaaplikacija.Classes.Movies;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hadze_000 on 11/2/2015.
 */
public interface FindMovie {
    @GET("/movie/{id}")
    public void getFeed(@Path("id") int id, @Query("api_key") String api_key, Callback<Movies> movie);
}
