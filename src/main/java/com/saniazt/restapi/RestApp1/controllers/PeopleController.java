package com.saniazt.restapi.RestApp1.controllers;


import com.saniazt.restapi.RestApp1.models.Person;
import com.saniazt.restapi.RestApp1.services.PeopleService;
import com.saniazt.restapi.RestApp1.util.PersonErrorResponse;
import com.saniazt.restapi.RestApp1.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;}

    @GetMapping()
    public List<Person> getPeople(){
        return peopleService.findAll(); //Jackson automatically convert objects to JSON
        }

        @GetMapping("/{id}")
        public Person getPerson(@PathVariable("id") int id){
        return  peopleService.findOne(id);
        }


        @ExceptionHandler
        private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
            PersonErrorResponse response = new PersonErrorResponse("Person with this ID wasn't found",
                    System.currentTimeMillis());
            //In http response will be our response message + status in header
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//NOT FOUND = 404 ERROR
        }

}
