package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.config.DBConfig;
import org.example.web.controllers.BooksShelfController;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BooksShelfController.class);
    //private List<Book> repo = new ArrayList<>();
    private ApplicationContext context;
    //private final int booksSize = repo.size();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository() {
        DBConfig dbConfig = new DBConfig();
        this.jdbcTemplate = dbConfig.namedParameterJdbcTemplate();
    }

    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books",
                (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
                });

        return new ArrayList(books);
    }

    @Override
    public void store(Book book) {
        //book.setId(context.getBean(IdProvider.class).provideID(book));
        logger.info("store new book: " + book);
       // repo.add(book);
    }

    @Override
    public void removeItemByID(Integer bookID) {
        for (Book book : retrieveAll()) {
            if (book.getId().equals(bookID)){
                logger.info("remove book completed:" + book);
               // repo.remove(book);
            }else{
                logger.info("Wrong input(book ID)");
            }
        }
    }

    @Override
    public boolean removeItem(Book book) {
        String author = book.getAuthor();
        String title = book.getTitle();
        Integer size = book.getSize();

        if (author.length() != 0 && title.length() != 0){
          removeByTitleAndAuthor(author,title);
     //     logger.info("--- Books size: " + repo.size());
        }else {
            if (author.length() != 0) {
               removeByAuthor(author);
        //      logger.info("--- Books size: " + repo.size());
            }
            if (title.length() != 0) {
            removeByTitle(title);
         //      logger.info("--- Books size: " + repo.size());
            }
            if (size != null) {
                logger.info("---Not Enough Information ");
            }
        }
        return true;// booksSize != repo.size();
    }

    private void removeByTitle(String title) {
//        for (int i = 0; i < repo.size(); i++) {
//            if (repo.get(i).getTitle().equals(title)){
//                repo.remove(repo.get(i));
//                logger.info("-Book with title: " + title + " deleted");
//            }
//        }
    }

    private void removeByAuthor(String author) {
//        for (int i = 0; i < repo.size(); i++) {
//            if (repo.get(i).getAuthor().equals(author)){
//                repo.remove(repo.get(i));
//                logger.info("-Book with author: " + author + " deleted");
//            }
//        }
    }

    private void removeByTitleAndAuthor(String author, String title) {
//        for (int i = 0; i < repo.size(); i++) {
//            if (repo.get(i).getAuthor().equals(author) && repo.get(i).getTitle().equals(title)){
//                repo.remove(repo.get(i));
//                logger.info("-Book with author and title: " + author + " "+ title + " deleted");
//            }
//        }
    }

    @Override
    public List<Book> find(String searchAuthor, String searchTitle) {
        List<Book> books = new ArrayList<>();
        for (Book book : retrieveAll()) {
            if (searchAuthor.equals(book.getAuthor()) && searchTitle.equals(book.getTitle())){
                books.add(book);
            }else{
                if (searchAuthor.length() != 0 && searchTitle.length() == 0){
                    if (searchAuthor.equals(book.getAuthor()))
                        books.add(book);
                }
                if (searchTitle.length() != 0 && searchAuthor.length() == 0){
                    if (searchTitle.equals(book.getTitle()))
                        books.add(book);
                }
            }
        }
        return books;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit(){
        logger.info("Default Init in BookRepository");
    }

    private void defaultDestroy(){
        logger.info("Default Destroy in BookRepository");
    }

}
