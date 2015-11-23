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

    public View getInflatedView(){
        return inflatedView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshFeed();
        inflatedView = inflater.inflate(R.layout.main_menu_grid, container, false);
        if( savedInstanceState != null)
            if( gridView != null)
                gridView.setVerticalScrollbarPosition(savedInstanceState.getInt("POSITION"));
        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        if( gridView != null) {
            int position = gridView.getFirstVisiblePosition();
            state.putInt("POSITION", position);
        }
    }

    public void refreshFeed(){
        ServiceGenerator api = ApiManager.getAdapter(getContext()).create(ServiceGenerator.class);

        String page = "1";

        api.getFeed(getActivity().getString(R.string.API_KEY), getContext().getString(R.string.order_by_popularity), page, new Callback<MoviesResponse>() {
            @Override
            public void success(final MoviesResponse _moviesResponse, Response response) {
                gridView = (GridView) inflatedView.findViewById(R.id.gridview);
                ArrayList<String> images = new ArrayList<>();

                for (int i = 0; i < _moviesResponse.getMovies().size(); i++)
                    images.add(_moviesResponse.getMovies().get(i).getPosterUrl(getContext(), ImageSizes.IMAGE_RESOLUTION_154));

                gridView.setAdapter(new ImageAdapter(getActivity(), images));

                Log.d("IMAGE", images.get(2));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        if( getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null){
                            ArticleFragment newFragment = new ArticleFragment();
                            Bundle arguments = new Bundle();
                            arguments.putInt("ID", _moviesResponse.getMovies().get(position).getId());

                            newFragment.setArguments(arguments);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else{
                            ArticleFragment article = (ArticleFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.article);
                            article.updateArticleView(_moviesResponse.getMovies().get(position).getId());
                        }
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), getContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                Log.d("FEED", error.getMessage());
            }
        });


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
    }

    @Override
    public void onPause(){
        super.onPause();

    }

}