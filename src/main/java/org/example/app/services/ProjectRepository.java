package org.example.app.services;

import org.example.web.dto.Book;
import org.example.web.dto.BookToRemove;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void store(T book);

    void removeItemByID(Integer bookID);

    void removeItem(BookToRemove bookToRemove);

    List<T> find(String searchAuthor, String searchTitle);

}
