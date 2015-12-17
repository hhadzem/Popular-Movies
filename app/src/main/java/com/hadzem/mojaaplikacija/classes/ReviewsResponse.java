package com.hadzem.mojaaplikacija.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hadzem on 12/17/15.
 */
public class ReviewsResponse implements Serializable {
    private ArrayList<Review> results = new ArrayList<>();
    public void setResults(ArrayList<Review> t){
        results = t;
    }
    public ArrayList<Review> getResults(){
        return results;
    }
}
