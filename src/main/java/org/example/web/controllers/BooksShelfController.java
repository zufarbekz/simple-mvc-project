package org.example.web.controllers;

import org.example.app.exceptions.UploadingException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;


@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BooksShelfController {

    private final Logger logger = Logger.getLogger(BooksShelfController.class);
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
        model.addAttribute("bookToRemove", new BookToRemove());
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
            model.addAttribute("bookToRemove", new BookToRemove());
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
            model.addAttribute("bookToRemove", new BookToRemove());
            return "books_shelf";
        } else {
            bookService.removeByID(bookIdToRemove.getId());
            logger.info("---Book Successfully deleted!");
            return "redirect:/books/shelf";
        }
    }


    @PostMapping("/remove")
    public String deleteBook(@Valid BookToRemove bookToRemove,
                             BindingResult bindingResult,
                             Model model){
        if ((bindingResult.hasFieldErrors("author") && bindingResult.hasFieldErrors("title")) || bindingResult.hasFieldErrors("size")) {
                model.addAttribute("book", bookToRemove);
                model.addAttribute("bookIdToRemove", new BookIdToRemove());
                model.addAttribute("bookList", bookService.getAllBooks());
                return "books_shelf";
        }else {
            if (bookService.removeByItem(bookToRemove) ){
                logger.info("---Removing Book(s) Completed!");
            }
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/search")
    public String searchBook(@RequestParam(value = "searchAuthor") String searchAuthor,
                             @RequestParam(value = "searchTitle") String searchTitle,
                             Model model){
        logger.info("---GET books/shelf page");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookToRemove", new BookToRemove());
        model.addAttribute("bookList",  bookService.searchBook(searchAuthor, searchTitle));
        return "books_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws IOException, UploadingException {
        try {
            String name = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            //create dir
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "external_uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //create file
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            logger.info("New file saved: " + serverFile.getAbsolutePath());

            return "redirect:/books/shelf";
        }catch (Exception exception){
            throw new UploadingException("Please Choose the File");
        }
    }

    @ExceptionHandler(UploadingException.class)
    public String handleError(UploadingException exception, Model model){
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/505";
    }
}
