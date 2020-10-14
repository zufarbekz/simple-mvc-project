package org.example.web.controllers;

import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BooksShelfController {

    private Logger logger = Logger.getLogger(BooksShelfController.class);
    private BookService bookService;

    @Autowired
    public BooksShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model){
        logger.info("---GET books/shelf page");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "books_shelf";
    }

    @PostMapping("/save")
    public String saveBooks(@Valid Book book,
                            BindingResult bindingResult,
                            Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("book",book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "books_shelf";
        }else {
            // Исключить возможность сохранения пустых записей
            if (!book.getTitle().equals("") || !book.getAuthor().equals("")) {
                bookService.saveBook(book);
                logger.info("Successfully saved! Total books: " + bookService.getAllBooks().size());
            } else {
                logger.info("!!! Necessary fields of book NOT entered");
            }
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/id")
    public String deleteBook(@Valid BookIdToRemove bookIdToRemove,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "books_shelf";
        } else {
            bookService.removeByID(bookIdToRemove.getId());
            logger.info("---Book Successfully deleted!");
            return "redirect:/books/shelf";
        }
    }


    @PostMapping("/remove")
    public String deleteBook(@RequestParam(value = "bookAuthor") String bookAuthor,
                             @RequestParam(value = "bookTitle") String bookTitle,
                             @RequestParam(value = "bookSize") String bookSize){
        if(bookAuthor.equals("") && bookTitle.equals("") && bookSize.equals("")) {
            logger.info("--- Necessary information is NOT entered");
        }else{
            if (!bookSize.equals("")){
                try{
                    Integer.parseInt(bookSize);
                }catch (NumberFormatException ex) {
                    logger.info("Wrong Input:  " + bookSize);
                }
            }
            if (bookService.removeByItem(bookAuthor, bookTitle, bookSize)){
                logger.info("---Removing Book(s) Completed!");
            }
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/search")
    public String searchBook(@RequestParam(value = "searchAuthor") String searchAuthor,
                             @RequestParam(value = "searchTitle") String searchTitle,
                             Model model){
        logger.info("---GET books/shelf page");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList",  bookService.searchBook(searchAuthor, searchTitle));
        return "books_shelf";
    }
}
