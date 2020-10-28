package org.example.web.controllers;

import org.apache.commons.io.IOUtils;
import org.example.app.exceptions.UploadingException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookToRemove;
import org.example.web.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        model.addAttribute("filter", new Filter());
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
            model.addAttribute("filter", new Filter());
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
            model.addAttribute("filter", new Filter());
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
                model.addAttribute("filter", new Filter());
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
    public String searchBook(@Valid Filter filter,
                             BindingResult bindingResult,
                             Model model){
        if (bindingResult.hasFieldErrors("searchAuthor") && bindingResult.hasFieldErrors("searchTitle")){
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookToRemove", new BookToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
        }else {
            logger.info("---GET books/shelf page");
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookToRemove", new BookToRemove());
            model.addAttribute("bookList", bookService.searchBook(filter.getSearchAuthor(), filter.getSearchTitle()));
        }
        return "books_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws  UploadingException {
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
            throw new UploadingException("Please Choose the File to Upload");
        }
    }

    @GetMapping("/downloadFile")
    public String download(HttpServletRequest request,
                         HttpServletResponse response) {
        File file =new File("D:\\apache-tomcat-9.0.37\\external_uploads\\text.txt");
        try
        {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

            if (mimeType == null){
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setContentLength((int)file.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/books/shelf";
    }

    @ExceptionHandler(UploadingException.class)
    public String handleError(UploadingException exception, Model model){
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/505";
    }
}
