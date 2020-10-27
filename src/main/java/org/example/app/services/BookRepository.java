package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.config.DBConfig;
import org.example.web.controllers.BooksShelfController;
import org.example.web.dto.Book;
import org.example.web.dto.BookToRemove;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public void removeItemByID(Integer bookID) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookID);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);

        logger.info("remove book completed");
    }

    @Override
    public boolean removeItem(BookToRemove bookToRemove) {
        boolean isRemoved = false;
        String author = bookToRemove.getAuthor();
        String title = bookToRemove.getTitle();
        Integer size = bookToRemove.getSize();

        if (author.length() != 0 && title.length() != 0){
          removeByTitleAndAuthor(author,title);
          isRemoved= true;
        }else {
            if(author.length() != 0 && size != null){
                removeByAuthorAndSize(author,size);
                isRemoved=true;
            }else{
                if (author.length() != 0) {
                    removeByAuthor(author);
                    isRemoved =true;
                }
            }
            if(title.length() != 0 && size != null){
                removeByTitleAndSize(title,size);
                isRemoved=true;
            }else {
                if (title.length() != 0) {
                    removeByTitle(title);
                    isRemoved = true;
                }
            }
        }
        return isRemoved;
    }

    private void removeByTitleAndSize(String title, Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        parameterSource.addValue("size", size);
        jdbcTemplate.update("DELETE FROM books WHERE title = :title AND size = :size", parameterSource);
    }

    private void removeByAuthorAndSize(String author, Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        parameterSource.addValue("size", size);
        jdbcTemplate.update("DELETE FROM books WHERE author = :author AND size = :size", parameterSource);
    }

    private void removeByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        jdbcTemplate.update("DELETE FROM books WHERE title = :title", parameterSource);

//        for (int i = 0; i < repo.size(); i++) {
//            if (repo.get(i).getTitle().equals(title)){
//                repo.remove(repo.get(i));
//                logger.info("-Book with title: " + title + " deleted");
//            }
//        }
    }

    private void removeByAuthor(String author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);

//        for (int i = 0; i < repo.size(); i++) {
//            if (repo.get(i).getAuthor().equals(author)){
//                repo.remove(repo.get(i));
//                logger.info("-Book with author: " + author + " deleted");
//            }
//        }
    }

    private void removeByTitleAndAuthor(String author, String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        parameterSource.addValue("author", author);
        jdbcTemplate.update("DELETE FROM books WHERE title = :title AND author = :author", parameterSource);

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
