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
package com.hadzem.mojaaplikacija.Fragments;

import android.app.Activity;
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

import com.hadzem.mojaaplikacija.Adapters.ImageAdapter;
import com.hadzem.mojaaplikacija.Interfaces.ServiceGenerator;
import com.hadzem.mojaaplikacija.MainActivity;
import com.hadzem.mojaaplikacija.Classes.MoviesDatabase;
import com.hadzem.mojaaplikacija.R;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HeadlinesFragment extends Fragment {
    private View inflatedView;
    private MoviesDatabase moviesDatabase;
    public View getInflatedView(){
        return inflatedView;
    }
    private ArrayList<String> images = null;
    private Handler mHandler = new Handler();
    private GridView gridView;
    private boolean finished;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesDatabase = new MoviesDatabase();
        inflatedView = inflater.inflate(R.layout.main_menu_grid, container, false);
        refreshFeed();
        return inflatedView;
    }

    public void refreshFeed(){
        String API = "https://api.themoviedb.org/3";
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        ServiceGenerator api = restAdapter.create(ServiceGenerator.class);


        String SORT_ORDER = "popularity.desc";
        String SORT_ORDER_2 = "vote_average.desc";

        api.getFeed(getActivity().getString(R.string.API_KEY), SORT_ORDER, new Callback<MoviesDatabase>() {
            @Override
            public void success(MoviesDatabase _moviesDatabase, Response response) {
                moviesDatabase = _moviesDatabase;
                gridView = (GridView) inflatedView.findViewById(R.id.gridview);
                ArrayList<String> images = new ArrayList<>();

                for (int i = 0; i < _moviesDatabase.getMovies().size(); i++)
                    images.add(_moviesDatabase.getMovies().get(i).getPoster_path());
                Log.d("CHECKIN", "DOSAO");
                gridView.setAdapter(new ImageAdapter(getActivity(), images));
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
                Log.d("FEED", error.getMessage());
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity().findViewById(R.id.fragment_container) != null) {
            MainActivity main = (MainActivity) getActivity();
            main.getSupportActionBar().setTitle("Popular Movies");
            main.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
    }

}