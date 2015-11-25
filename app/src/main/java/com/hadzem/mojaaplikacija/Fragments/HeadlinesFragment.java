/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadzem.mojaaplikacija.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hadzem.mojaaplikacija.adapters.ImageAdapter;
import com.hadzem.mojaaplikacija.classes.ApiManager;
import com.hadzem.mojaaplikacija.classes.ImageSizes;
import com.hadzem.mojaaplikacija.classes.MoviesResponse;
import com.hadzem.mojaaplikacija.interfaces.ServiceGenerator;
import com.hadzem.mojaaplikacija.MainActivity;
import com.hadzem.mojaaplikacija.R;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HeadlinesFragment extends Fragment {
    private View inflatedView;
    private GridView gridView;
    private MoviesResponse moviesResponse;
    private int currentPosition = 0;
    public View getInflatedView(){
        return inflatedView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflatedView = inflater.inflate(R.layout.main_menu_grid, container, false);
        if(moviesResponse == null) refreshFeed();
        else makeGrid(moviesResponse);

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable("movies", moviesResponse);
    }

    public void refreshFeed(){
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.pref_file), 0);
        String order = pref.getString("order", getString(R.string.order_by_rating));

        ServiceGenerator api = ApiManager.getAdapter(getContext()).create(ServiceGenerator.class);
        String page = "1";
        api.getFeed(getActivity().getString(R.string.API_KEY), order, page, new Callback<MoviesResponse>() {
            @Override
            public void success(final MoviesResponse _moviesResponse, Response response) {
                makeGrid(_moviesResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), getContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                Log.d("FEED", error.getMessage());
            }
        });
    }

    private void makeGrid(final MoviesResponse _moviesResponse){
        moviesResponse = _moviesResponse;
        gridView = (GridView) inflatedView.findViewById(R.id.gridview);
        ArrayList<String> images = new ArrayList<>();

        for (int i = 0; i < _moviesResponse.getMovies().size(); i++)
            images.add(_moviesResponse.getMovies().get(i).getPosterUrl(getContext(), ImageSizes.IMAGE_RESOLUTION_154));

        gridView.setAdapter(new ImageAdapter(getActivity(), images));
        gridView.smoothScrollToPosition(currentPosition);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                currentPosition = position;
                if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                    ArticleFragment newFragment = new ArticleFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt("position", position);
                    arguments.putSerializable("movies", _moviesResponse);
                    newFragment.setArguments(arguments);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    ArticleFragment article = (ArticleFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.article);
                    Bundle arguments = new Bundle();
                    arguments.putInt("position", position);
                    arguments.putSerializable("movies", _moviesResponse);
                    article.setPosition(arguments);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(moviesResponse != null)
        makeGrid(moviesResponse);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity().findViewById(R.id.fragment_container) != null) {
            MainActivity main = (MainActivity) getActivity();
            main.getSupportActionBar().setTitle(getContext().getString(R.string.title));
            main.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
}