package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class BookToRemove {
    @NotEmpty
    private String author;
    @NotEmpty
    private String title;
    @Digits(integer = 5, fraction = 0)
    private Integer size;

    public BookToRemove() {
    }

    public BookToRemove(String author, String title, Integer size) {
        this.author = author;
        this.title = title;
        this.size = size;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
