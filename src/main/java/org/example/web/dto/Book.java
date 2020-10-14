package org.example.web.dto;

import javax.validation.constraints.Digits;

public class Book {
    private String id;
    private String author;
    private String title;
    @Digits(integer = 5, fraction = 0)
    private String size;

    public Book() {
    }

    public Book(String id, String author, String title, String size) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.size = size;
    }

    public Book(String author, String title, String size) {
        this.author = author;
        this.title = title;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }

}
