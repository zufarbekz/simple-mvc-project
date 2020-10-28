package org.example.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Filter {
    @NotNull
    private String searchAuthor;
    @NotNull
    private String searchTitle;

    public Filter() {
    }

    public Filter(String searchAuthor, String searchTitle) {
        this.searchAuthor = searchAuthor;
        this.searchTitle = searchTitle;
    }

    public String getSearchAuthor() {
        return searchAuthor;
    }

    public void setSearchAuthor(String searchAuthor) {
        this.searchAuthor = searchAuthor;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }
}
