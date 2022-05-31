package com.javaabuser.mvc.controller;

import com.javaabuser.mvc.DAO.BookDAO;
import com.javaabuser.mvc.DAO.PersonDAO;
import com.javaabuser.mvc.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getBooks(Model model){
        model.addAttribute("books", bookDAO.getBooks());
        return "book/books_page";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, @ModelAttribute("book") Book book, Model model){
        model.addAttribute("book", bookDAO.getBook(id));
        System.out.println(bookDAO.getBook(id).getId());
        model.addAttribute("bookOwner", bookDAO.getBookOwner(id));
        model.addAttribute("people", personDAO.getPeople());
        return "book/book_page";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "book/book_new_page";
    }

    @PostMapping()
    public String createNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "book/book_new_page";
        }
        bookDAO.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.getBook(id));
        return "book/book_edit_page";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "book/book_edit_page";
        }
        bookDAO.updateBook(book);
        model.addAttribute("book", book);
        return "redirect:/books";
    }

    @PatchMapping()
    public String selectOwnerToBook(@ModelAttribute("book") Book book){
        bookDAO.setBookOwner(book.getId(), book.getPerson());
        book.setPerson(book.getPerson());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        bookDAO.releaseBook(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id){
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }
}
