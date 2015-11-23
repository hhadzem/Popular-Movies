package com.hadzem.mojaaplikacija.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
           // imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 500));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else{
            imageView = (ImageView) convertView;
        }
        if(position >= images.size() ) return imageView;
        Picasso.with(mContext).load(images.get(position)).into(imageView);

        //Picasso.with(mContext).load(mThumbIds[position]).into(imageView);
        return imageView;
    }

}