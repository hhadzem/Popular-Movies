package com.hadzem.mojaaplikacija;

import android.content.SharedPreferences;
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
    private void deleteDatabase(){
        getApplicationContext().deleteDatabase("MoviesDatabase_test");
        getApplicationContext().deleteDatabase("MoviesDatabase");
        getApplicationContext().deleteDatabase("MoviesDatabase_testing");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //deleteDatabase();
        setContentView(R.layout.news_articles);

        if (findViewById(R.id.fragment_container) != null) {
            HeadlinesFragment firstFragment = new HeadlinesFragment();
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SharedPreferences pref = getSharedPreferences(getString(R.string.pref_file), 0);
        String order = pref.getString("order", getString(R.string.order_by_rating));
        if( order.equals(getString(R.string.order_by_rating)) )
            menu.findItem(R.id.top_rated).setChecked(true);
        else if(order.equals(R.string.most_popular))
            menu.findItem(R.id.most_popular).setChecked(true);
        else
            menu.findItem(R.id.favorites).setChecked(true);
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
                else if( getFragment() instanceof ArticleFragment)
                    ((ArticleFragment) getFragment()).refreshFeed();
                break;
            case R.id.top_rated:
                SharedPreferences pref = getSharedPreferences(getString(R.string.pref_file), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("order", getString(R.string.order_by_rating));
                editor.apply();
                if( getFragment() instanceof ArticleFragment)
                    onBackPressed();
                ( (HeadlinesFragment) getFragment()).refreshFeed();
                item.setChecked(true);
                break;
            case R.id.most_popular:
                pref = getSharedPreferences(getString(R.string.pref_file), 0);
                editor = pref.edit();
                editor.putString("order", getString(R.string.order_by_popularity));
                editor.apply();
                if( getFragment() instanceof ArticleFragment)
                    onBackPressed();
                ( (HeadlinesFragment) getFragment()).refreshFeed();
                item.setChecked(true);
                break;
            case R.id.favorites:
                pref = getSharedPreferences(getString(R.string.pref_file), 0);
                editor = pref.edit();
                editor.putString("order", getString(R.string.favorites));
                editor.apply();
                if(getFragment() instanceof ArticleFragment)
                    onBackPressed();
                ((HeadlinesFragment) getFragment()).refreshFeed();
                item.setChecked(true);
                break;
        }

        return true;
    }
}