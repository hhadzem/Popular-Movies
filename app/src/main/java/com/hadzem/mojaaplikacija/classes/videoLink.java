package com.hadzem.mojaaplikacija.classes;

import java.io.Serializable;

/**
 * Created by hadzem on 12/17/15.
 */
public class videoLink implements Serializable{
    private String key;
    public void setKey(String t){
        key = t;
    }
    public String getKey(){
        return key;
    }
}
