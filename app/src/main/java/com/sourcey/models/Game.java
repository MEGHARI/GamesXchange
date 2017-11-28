package com.sourcey.models;

import java.util.Date;

/**
 * Created by aghiles on 28/11/17.
 */

public class Game {

    private int id;
    private String title;
    private String overview;
    private Date dateRelease;
    private int[] platforms;
    private Object image;

    public Game(){}

    public Game(int id, String title, String overview, Date dateRelease, int[] platforms, Object image) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.dateRelease = dateRelease;
        this.platforms = platforms;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(Date dateRelease) {
        this.dateRelease = dateRelease;
    }

    public int[] getPlatforms() {
        return platforms;
    }

    public void setPlatforms(int[] platforms) {
        this.platforms = platforms;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }
}
