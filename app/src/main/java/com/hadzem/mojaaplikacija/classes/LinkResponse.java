package com.hadzem.mojaaplikacija.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hadzem on 12/17/15.
 */
public class LinkResponse implements Serializable{
    private ArrayList<videoLink> results = new ArrayList<>();
    public void setResults(ArrayList<videoLink> r){
        results = r;
    }
    public ArrayList<videoLink> getResults(){
        return results;
    }
}
