package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.request.PersonRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {

    //Added static to pass test case
    //Modify list object to add new objects
    public static final List<Person> PERSON_DATA = new ArrayList<>(Arrays.asList(
            new Person("Mary", "Smith"),
            new Person("Brian", "Archer"),
            new Person("Collin", "Brown")
    ));

    public Person findPerson(String lastName, String firstName) {
        List<Person> persons = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        return persons.isEmpty() ? null : persons.get(0);
    }

    public List<Person> findPersons(String lastName) {
        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public void savePerson(PersonRequest personRequest) {
        //should return the object if db call made and any new fields have been added to it eg. ID}
        PERSON_DATA.add(new Person(personRequest.getFirstName(), personRequest.getLastName()));
    }
}
