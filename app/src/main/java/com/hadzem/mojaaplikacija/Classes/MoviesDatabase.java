package com.hadzem.mojaaplikacija.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadze_000 on 10/31/2015.
 */
public class MoviesDatabase {
    private int page;
    private List<Movies> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movies> getMovies() {
        return results;
    }

    public void setMovies(List<Movies> movies) {
        this.results = movies;
    }
}
