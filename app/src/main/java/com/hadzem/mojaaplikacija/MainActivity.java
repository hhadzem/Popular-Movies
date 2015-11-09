package com.hadzem.mojaaplikacija;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.hadzem.mojaaplikacija.fragments.ArticleFragment;
import com.hadzem.mojaaplikacija.fragments.HeadlinesFragment;


public class MainActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    HeadlinesFragment firstFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

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
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(currentFragment instanceof ArticleFragment) {
                super.onBackPressed();
            }
            else if(currentFragment instanceof HeadlinesFragment) {
                GridView gridView = (GridView) ( (HeadlinesFragment) currentFragment).getInflatedView().findViewById(R.id.gridview);
                if (gridView.getFirstVisiblePosition() == 0)
                    finish();
                gridView.smoothScrollToPosition(0);
            }
            else
                Log.d("BACK PRESSED", "NO FRAGMENT FOUND");
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
                else
                    ((ArticleFragment) getFragment()).refreshFeed();
                break;
        }
        return true;
    }
}