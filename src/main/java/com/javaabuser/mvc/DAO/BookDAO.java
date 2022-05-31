package com.javaabuser.mvc.DAO;

import com.javaabuser.mvc.model.Book;
import com.javaabuser.mvc.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:database.properties")
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooks(){
        return jdbcTemplate.query("SELECT * FROM Book", new BookMapper());
    }

    public Book getBook(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id = ?", new Object[]{id}, new BookMapper())
                .stream().findAny().orElse(null);
    }

    public void saveBook(Book book){
        jdbcTemplate.update("INSERT INTO Book(name, author, year) VALUES(?,?,?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public void updateBook(Book mustBeUpdatedBook){
        jdbcTemplate.update("UPDATE Book SET name = ?, author = ?, year = ? WHERE id = ?",
                mustBeUpdatedBook.getName(), mustBeUpdatedBook.getAuthor(), mustBeUpdatedBook.getYear(), mustBeUpdatedBook.getId());
    }

    public void deleteBook(int id){
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?", id);
    }

    public Person getBookOwner(int id){
        return jdbcTemplate.query("SELECT * FROM Person JOIN Book ON Person.id = Book.person_id WHERE Book.id = ?", new PersonMapper(), id)
                .stream().findAny().orElse(null);
    }

    public void setBookOwner(int bookId, int personId){
        jdbcTemplate.update("UPDATE Book SET person_id = ? WHERE id = ?",
                personId, bookId);
    }

    public void releaseBook(int Id){
        jdbcTemplate.update("UPDATE Book SET person_id = NULL WHERE id = ?",
                Id);
    }
}