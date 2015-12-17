package com.hadzem.mojaaplikacija.classes;

import android.content.Context;

import com.hadzem.mojaaplikacija.R;

import retrofit.Endpoint;
import retrofit.RestAdapter;

/**
 * Created by hadze_000 on 11/9/2015.
 */
public class ApiManager {
    static volatile RestAdapter restAdapter = null;
    private static Endpoint endpoint;

    private ApiManager(){
    }

    public static RestAdapter getAdapter(final Context mContext){
        if(restAdapter == null){
            synchronized (ApiManager.class){
                if(restAdapter == null){
                    endpoint = new Endpoint() {
                        @Override
                        public String getUrl() {
                            return mContext.getString(R.string.API);
                        }

                        @Override
                        public String getName() {
                            return null;
                        }
                    };
                    restAdapter = new RestAdapter.Builder().setEndpoint(endpoint.getUrl()).setLogLevel(RestAdapter.LogLevel.NONE).build();
                }
            }
        }
        return restAdapter;
    }

}
