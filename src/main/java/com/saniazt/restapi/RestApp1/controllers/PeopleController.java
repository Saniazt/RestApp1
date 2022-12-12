package com.saniazt.restapi.RestApp1.controllers;


import com.saniazt.restapi.RestApp1.dto.PersonDTO;
import com.saniazt.restapi.RestApp1.models.Person;
import com.saniazt.restapi.RestApp1.services.PeopleService;
import com.saniazt.restapi.RestApp1.util.PersonErrorResponse;
import com.saniazt.restapi.RestApp1.util.PersonNotCreatedException;
import com.saniazt.restapi.RestApp1.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;


    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDTO> getPeople(){
        return peopleService.findAll().stream().map(this::convertToPersonDTO).collect(Collectors.toList()); //Jackson automatically convert objects to JSON
        }

        @GetMapping("/{id}")
        public PersonDTO getPerson(@PathVariable("id") int id){
        return  convertToPersonDTO(peopleService.findOne(id));
        }

        @PostMapping
        public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                                 BindingResult bindingResult){
            if(bindingResult.hasErrors()) {
                StringBuilder errorsMsg = new StringBuilder();
               List<FieldError> errors1 =  bindingResult.getFieldErrors();
               for (FieldError fieldError: errors1) {
                   errorsMsg.append(fieldError.getField()).append("-").append(fieldError.getDefaultMessage()).append(";");
               }
                throw new PersonNotCreatedException(errorsMsg.toString());
            }
            peopleService.save(convertToPerson(personDTO));
            return ResponseEntity.ok(HttpStatus.OK); //ок тк не хотим создавать отдельный обьект для сообщения об успехе
        }




    @ExceptionHandler
        private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
            PersonErrorResponse response = new PersonErrorResponse("Person with this ID wasn't found",
                    System.currentTimeMillis());
            //In http response will be our response message + status in header
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//NOT FOUND = 404 ERROR
        }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
        PersonErrorResponse response =
                new PersonErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO,Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person,PersonDTO.class);
    }
}
