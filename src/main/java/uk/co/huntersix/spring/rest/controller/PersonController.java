package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.request.PersonRequest;

import java.net.URI;
import java.util.List;

@RestController
public class PersonController {
    private final PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> person(@PathVariable(value = "lastName") String lastName,
                                         @PathVariable(value = "firstName") String firstName) {
        Person person = personDataService.findPerson(lastName, firstName);
        if (person == null) {
            //Could return proper error message in body if created and thrown PersonNotFoundException
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, null, HttpStatus.OK);
    }

    @GetMapping("/person/{lastName}")
    public ResponseEntity<List<Person>> persons(@PathVariable(value = "lastName") String lastName) {
        List<Person> persons = personDataService.findPersons(lastName);
        if (persons.isEmpty()) {
            return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(persons, null, HttpStatus.OK);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> person(@RequestBody PersonRequest personRequest) {
        if (personRequest == null || personRequest.getFirstName().isEmpty() || personRequest.getLastName().isEmpty()) {
            //Could return proper error message in body if created and thrown KeyNotFoundException
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        Person person = personDataService.findPerson(personRequest.getLastName(), personRequest.getFirstName());
        if (person != null) {
            return new ResponseEntity<>(null, null, HttpStatus.CONFLICT);
        }

        personDataService.savePerson(personRequest);
        return ResponseEntity.created(URI.create("/person/" + personRequest.getLastName() + "/" + personRequest.getFirstName())).build();
    }
}