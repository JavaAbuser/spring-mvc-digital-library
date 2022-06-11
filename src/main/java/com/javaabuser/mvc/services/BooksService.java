package com.javaabuser.mvc.services;

import com.javaabuser.mvc.models.Book;
import com.javaabuser.mvc.models.Person;
import com.javaabuser.mvc.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(){
        return booksRepository.findAll();
    }

    public List<Book> findAll(boolean sortByYear){
        if(sortByYear){
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findAllWithPagination(int page, int booksPerPage, boolean sortByYear){
        if(sortByYear){
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> findByNameStartingWith(String startingWith){
        return booksRepository.findByNameStartingWith(startingWith);
    }

    public Book findById(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook){
        Book book = null;
        if(booksRepository.findById(updatedBook.getId()).isPresent()){
           book = booksRepository.findById(updatedBook.getId()).get();
           book.setName(updatedBook.getName());
           book.setAuthor(updatedBook.getAuthor());
           book.setYear(updatedBook.getYear());
           booksRepository.save(book);
        }
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id){
        Optional<Person> owner = null;
        if(booksRepository.findById(id).isPresent()){
             owner = booksRepository.findById(id).map(Book::getPerson);
        }
        return owner;
    }

    @Transactional
    public void setBookOwner(int id, Person person){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(person);
                    book.setTakenAt(new Date());
                });
    }

    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(null);
                    book.setTakenAt(null);
                }
        );
    }
}
