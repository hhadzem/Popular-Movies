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
package com.hadzem.mojaaplikacija;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.hadzem.mojaaplikacija.Fragments.HeadlinesFragment;


public class MainActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    HeadlinesFragment firstFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);
        //getSupportActionBar().setLogo(R.drawable.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            firstFragment = new HeadlinesFragment();
            firstFragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, firstFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed(){
        if(findViewById(R.id.fragment_container) != null) {
            GridView gridView = (GridView) firstFragment.getInflatedView().findViewById(R.id.gridview);
            if( gridView.getFirstVisiblePosition() == 0)
                finish();
            gridView.smoothScrollToPosition(0);
        }
        else{
            HeadlinesFragment headlinesFragment = (HeadlinesFragment) getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
            GridView gridView = (GridView) headlinesFragment.getInflatedView().findViewById(R.id.gridview);
            if( gridView.getFirstVisiblePosition() == 0)
                super.onBackPressed();
            gridView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("HEPEK", "POZVALO ME");
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Fragment getFragment(){
        if( findViewById(R.id.fragment_container) != null){
            return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }
        else return getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.action_refresh:
                if( getFragment() instanceof HeadlinesFragment )
                    ((HeadlinesFragment) getFragment()).refreshFeed();
                break;
        }
        return true;
    }
}