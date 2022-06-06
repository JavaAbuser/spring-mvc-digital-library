package com.javaabuser.mvc.controller;

import com.javaabuser.mvc.DAO.PersonDAO;
import com.javaabuser.mvc.model.Person;
import com.javaabuser.mvc.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getPeople(Model model){
        model.addAttribute("people", personDAO.getPeople());
        return "person/people_page";
    }

    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personDAO.getPerson(id));
        model.addAttribute("receivedBook", personDAO.getReceivedBook(personDAO.getPerson(id)));
        System.out.println(id);
        System.out.println(personDAO.getReceivedBook(personDAO.getPerson(id)));
        System.out.println(personDAO.getPerson(id));
        return "person/person_page";
    }

    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "person/person_new_page";
    }

    @PostMapping()
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "person/person_new_page";
        }
        personDAO.savePerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personDAO.getPerson(id));
        return "person/person_edit_page";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "person/person_edit_page";
        }
        personDAO.updatePerson(person);
        model.addAttribute("person", person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id){
        personDAO.deletePerson(id);
        return "redirect:/people";
    }
}
