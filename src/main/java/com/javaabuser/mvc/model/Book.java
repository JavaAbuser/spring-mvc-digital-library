package com.javaabuser.mvc.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {
    private int id;
    @NotBlank(message = "Name should be not blank")
    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;
    @NotBlank(message = "Author should be not blank")
    @NotEmpty(message = "Author should be not empty")
    @Size(min = 2, max = 30, message = "Author should be between 2 and 30 characters")
    private String author;
    @Min(value = 0, message = "Year should be greater than 0")
    private int year;

    private int person_id;

    public Book() {
    }

    public void addPerson(int person_id){
        this.person_id = person_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public int getPerson() {
        return person_id;
    }

    public void setPerson(int person) {
        this.person_id = person;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
