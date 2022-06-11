package com.javaabuser.mvc.controllers;

import com.javaabuser.mvc.models.Person;
import com.javaabuser.mvc.services.PeopleService;
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
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getPeople(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "/person/index";
    }

    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("receivedBook", peopleService.getReceivedBooks(id));
        System.out.println(id);
        System.out.println(peopleService.getReceivedBooks(id));
        System.out.println(peopleService.findById(id));
        return "/person/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "/person/new";
    }

    @PostMapping()
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "/person/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.findById(id));
        return "/person/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "/person/edit";
        }
        peopleService.update(person);
        model.addAttribute("person", person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }
}
