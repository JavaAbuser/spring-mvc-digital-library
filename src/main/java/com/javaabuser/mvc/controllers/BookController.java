package com.javaabuser.mvc.controllers;

import com.javaabuser.mvc.models.Book;
import com.javaabuser.mvc.models.Person;
import com.javaabuser.mvc.services.BooksService;
import com.javaabuser.mvc.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getBooks(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "books_per_page", required = false) Integer bookPerPage,
                           @RequestParam(value = "sort_by_year", required = false) boolean sortByYear, Model model){
        if(page == null || bookPerPage == null){
            model.addAttribute("books", booksService.findAll(sortByYear));
        } else {
            model.addAttribute("books", booksService.findAllWithPagination(page, bookPerPage, sortByYear));
        }
        return "book/index";
    }

    @GetMapping("/search")
    public String findBook(@ModelAttribute("book") Book book, Model model){
        model.addAttribute("book", book);
        return "/book/search";
    }

    @PostMapping("/search")
    public String doSearch(@ModelAttribute("book") Book book, Model model){
        List<Book> foundedBooks = booksService.findByNameStartingWith(book.getName());
        model.addAttribute("foundedBooks", foundedBooks);
        return "/book/search";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, @ModelAttribute("book") Book book,
                          @ModelAttribute("person") Person person, Model model){
        model.addAttribute("book", booksService.findById(id));
        Optional<Person> bookOwner = booksService.getBookOwner(id);
        if(bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        System.out.println(bookOwner.isPresent());
        System.out.println(peopleService.findAll());
        System.out.println(booksService.findById(id));
        return "/book/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "/book/new";
    }

    @PostMapping()
    public String createNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/book/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findById(id));
        return "/book/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "/book/edit";
        }
        booksService.update(book);
        model.addAttribute("book", book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/select")
    public String selectOwnerToBook(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        booksService.setBookOwner(id, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        booksService.release(id);
        System.out.println(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }
}
