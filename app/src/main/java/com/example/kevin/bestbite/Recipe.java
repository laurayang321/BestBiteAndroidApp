package com.example.kevin.bestbite;

import java.sql.Blob;

/**
 * Created by Jing on 26-Nov-2016.
 */

public class Recipe {
    private String title;
    private String method;
    private Integer time;
    private byte[] image;
    private Integer cookTimes;

    public Recipe(){

    }

    public Recipe(String title, String method, Integer time, byte[] image, Integer cookTimes){
        this.title = title;
        this.method = method;
        this.time = time;
        this.image = image;
        this.cookTimes = cookTimes;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setMethod(String method){
        this.method = method;
    }
    public void setTime(Integer time){
        this.time = time;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
    public void setcookTimes(Integer cookTimes){
        this.cookTimes = cookTimes;
    }

    public String getTitle(){
        return this.title;
    }

    public String getMethod(){
        return this.method;
    }

    public Integer getTime(){
        return this.time;
    }

    public byte[] getImage(){
        return this.image;
    }

    public Integer getCookTimes(){
        return this.cookTimes;
    }

}
