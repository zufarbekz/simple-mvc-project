package org.example.app.services;

import org.example.web.dto.Book;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void store(T book);

    void removeItemByID(Integer bookID);

    boolean removeItem(Book book);

    List<T> find(String searchAuthor, String searchTitle);

}
