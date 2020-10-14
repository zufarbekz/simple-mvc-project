package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.apache.log4j.Logger;

@Service
public class BookService {

    private  ProjectRepository<Book> bookRepo;
    private Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public void removeByID(String bookID) {
        bookRepo.removeItemByID(bookID);
    }

    public boolean removeByItem(String author, String title, String size){
      Book book = new Book(author,title,size);
      return  bookRepo.removeItem(book);
    }

    public List<Book> searchBook(String searchAuthor, String searchTitle){
       return bookRepo.find(searchAuthor, searchTitle);
    }
}
