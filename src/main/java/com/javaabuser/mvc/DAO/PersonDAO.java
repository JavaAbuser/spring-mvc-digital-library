package com.javaabuser.mvc.DAO;

import com.javaabuser.mvc.model.Book;
import com.javaabuser.mvc.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PersonDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> getPeople(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Person getPerson(int id){
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void savePerson(Person person){
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void updatePerson(Person mustBeUpdatedPerson){
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, mustBeUpdatedPerson.getId());
        person.setFullName(mustBeUpdatedPerson.getFullName());
        person.setYearOfBirth(mustBeUpdatedPerson.getYearOfBirth());
        Hibernate.initialize(mustBeUpdatedPerson.getBooks());
        person.setBooks(mustBeUpdatedPerson.getBooks());
    }

    @Transactional
    public void deletePerson(int id){
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Person.class, id));
    }

    @Transactional
    public List<Book> getReceivedBook(Person person){
        Session session = sessionFactory.getCurrentSession();
        Hibernate.initialize(session.get(Person.class, person.getId()).getBooks());
        return session.get(Person.class, person.getId()).getBooks();
    }

    @Transactional
    public Person getByName(String fullName){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person p WHERE p.fullName =: fullName", Person.class)
                .setParameter("fullName", fullName)
                .stream().findAny().orElse(null);
    }
}