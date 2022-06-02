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
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getPeople(){
        return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());
    }

    public Person getPerson(int id){
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?", new Object[]{id}, new PersonMapper())
                .stream().findAny().orElse(null);
    }

    public void savePerson(Person person){
        jdbcTemplate.update("INSERT INTO Person(fullName, yearOfBirth) VALUES(?,?)",
                person.getFullName(), person.getYearOfBirth());
    }

    public void updatePerson(Person mustBeUpdatedPerson){
        jdbcTemplate.update("UPDATE Person SET fullName = ?, yearOfBirth = ? WHERE id = ?",
                mustBeUpdatedPerson.getFullName(), mustBeUpdatedPerson.getYearOfBirth(), mustBeUpdatedPerson.getId());
    }

    public void deletePerson(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
    }

    public List<Book> getReceivedBook(Person person){
        List<Book> books =  jdbcTemplate.query("SELECT Book.id AS id, Book.person_id AS person_id, Book.name AS name, Book.author AS author, Book.year AS year" +
                        " FROM Person JOIN Book ON Person.id = Book.person_id WHERE Person.id = ?",
                new BookMapper(), person.getId());
        person.setBooks(books);
        return books;
    }

    public Person getByName(String fullName){
        return jdbcTemplate.query("SELECT * FROM Person WHERE fullName = ?", new Object[]{fullName}, new PersonMapper())
                .stream().findAny().orElse(null);
    }
}