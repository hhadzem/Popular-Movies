package com.hadzem.mojaaplikacija.Adapters;

import android.app.ActionBar;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hadzem.mojaaplikacija.Classes.ImageSizes;
import com.hadzem.mojaaplikacija.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hadze_000 on 10/24/2015.
 */
public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    private String link;
    private ImageView imageView;
    private static int counter = 0;
    private ArrayList<String> images;

    public ImageAdapter(Context c, ArrayList<String> images){
        mContext = c;
        this.images = images;
        counter = images.size();
    }

    public int getCount(){
        return counter;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
           // imageView.setMaxHeight((int) mContext.getResources().getDimension(R.dimen.image_height)) ;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else{
            imageView = (ImageView) convertView;
        }
        if(position >= images.size() ) return imageView;
        link = images.get(position);

        Picasso.with(mContext).load(mContext.getString(R.string.image_base_link) + ImageSizes.IMAGE_RESOLUTION_185+ link).into(imageView);
        //Picasso.with(mContext).load(mThumbIds[position]).into(imageView);
        return imageView;
    }
}
