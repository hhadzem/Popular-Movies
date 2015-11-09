package com.hadzem.mojaaplikacija.classes;

import java.util.List;

/**
 * Created by hadze_000 on 10/31/2015.
 */
public class MoviesResponse {
    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public void setMovies(List<Movie> movies) {
        this.results = movies;
    }
}
