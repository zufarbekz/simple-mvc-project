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
    private ApplicationContext context;
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
    public void removeItem(BookToRemove bookToRemove) {
        String query = "DELETE FROM books WHERE ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        String author = bookToRemove.getAuthor();
        String title = bookToRemove.getTitle();
        Integer size = bookToRemove.getSize();

        if (author.length() != 0){
            parameterSource.addValue("author", author);
            query += "author = :author AND ";
        }
        if (title.length() != 0){
            parameterSource.addValue("title", title);
            query += "title = :title AND ";
        }
        if (size != null){
            parameterSource.addValue("size", size);
            query += "size = :size AND ";
        }
        query = query.replaceAll("(AND )$", "");
        jdbcTemplate.update(query, parameterSource);
    }

    @Override
    public List<Book> find(String searchAuthor, String searchTitle) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String query = "SELECT * FROM books WHERE ";

        if(searchAuthor.length() != 0){
            parameterSource.addValue("author", searchAuthor);
            query += "author = :author AND ";
        }
        if (searchTitle.length() != 0){
            parameterSource.addValue("title", searchTitle);
            query += "title = :title";
        }
        query = query.replaceAll("(AND )$", "");

        List<Book> books = jdbcTemplate.query(query, parameterSource,
                (ResultSet rs, int rowNum) -> {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setAuthor(rs.getString("author"));
                    book.setTitle(rs.getString("title"));
                    book.setSize(rs.getInt("size"));
                    return book;
                });

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
