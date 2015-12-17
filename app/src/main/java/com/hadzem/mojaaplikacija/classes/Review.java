package com.hadzem.mojaaplikacija.classes;

import java.io.Serializable;

/**
 * Created by hadzem on 12/17/15.
 */
public class Review implements Serializable{
    private String content;
    public void setContent(String t){
        content = t;
    }
    public String getContent(){
        return content;
    }
}
