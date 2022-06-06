package com.javaabuser.mvc.DAO;

import com.javaabuser.mvc.model.Book;
import com.javaabuser.mvc.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BookDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Book getBook(int id){
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void saveBook(Book book){
        Session session = sessionFactory.getCurrentSession();
        session.save(book);
    }

    @Transactional
    public void updateBook(Book mustBeUpdatedBook){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, mustBeUpdatedBook.getId());
        book.setName(mustBeUpdatedBook.getName());
        book.setAuthor(mustBeUpdatedBook.getAuthor());
        book.setYear(mustBeUpdatedBook.getYear());
    }

    @Transactional
    public void deleteBook(int id){
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Book.class, id));
    }

    @Transactional
    public Person getBookOwner(int id){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        return book.getPerson();
    }

    @Transactional
    public void setBookOwner(int bookId, Person person){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, bookId);
        book.setPerson(person);
        System.out.println(person);
    }

    @Transactional
    public void releaseBook(int id){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        book.setPerson(null);
    }
}