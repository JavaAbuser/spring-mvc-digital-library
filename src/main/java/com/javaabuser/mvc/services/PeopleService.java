package com.javaabuser.mvc.services;

import com.javaabuser.mvc.models.Book;
import com.javaabuser.mvc.models.Person;
import com.javaabuser.mvc.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findById(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson){
        Optional<Person> person = peopleRepository.findById(updatedPerson.getId());
        if(person.isPresent()){
            person.get().setFullName(updatedPerson.getFullName());
            person.get().setYearOfBirth(updatedPerson.getYearOfBirth());
            peopleRepository.save(person.get());
        }
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public List<Book> getReceivedBooks(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long tenDaysInMilliseconds = 864_000_000L;
                long differentInMilliseconds = Math.abs(new Date().getTime() - book.getTakenAt().getTime());
                book.setExpired(differentInMilliseconds >= tenDaysInMilliseconds);
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<Person> findByFullName(String fullName){
        return peopleRepository.findByFullName(fullName);
    }
}
