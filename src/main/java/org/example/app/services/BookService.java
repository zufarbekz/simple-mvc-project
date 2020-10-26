package org.example.app.services;

import org.example.web.dto.Book;
import org.example.web.dto.BookToRemove;
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

    public void removeByID(Integer bookID) {
        bookRepo.removeItemByID(bookID);
    }

    public boolean removeByItem(BookToRemove bookToRemove){
      return  bookRepo.removeItem(bookToRemove);
    }

    public List<Book> searchBook(String searchAuthor, String searchTitle){
       return bookRepo.find(searchAuthor, searchTitle);
    }

    private void defaultInit(){
        logger.info("Default Init in BookService");
    }

    private void defaultDestroy(){
        logger.info("Default Destroy in BookService");
    }
}
