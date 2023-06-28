package uk.co.huntersix.spring.rest.referencedata;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.request.PersonRequest;

import java.util.List;

@RunWith(SpringRunner.class)
public class PersonDataServiceTest {

    PersonDataService personDataService;

    Person personRequest;

    @Before
    public void setUp() {
        personDataService = new PersonDataService();
        personRequest = new Person("Brian", "Archer");
    }

    @After
    public void tearDown() {
        personRequest = null;
    }

    @Test
    public void shouldReturnPersonWhenExistingNamesGiven() {
        Person person = personDataService.findPerson(personRequest.getLastName(), personRequest.getFirstName());
        Assert.assertEquals(person.getLastName(), personRequest.getLastName());
        Assert.assertEquals(person.getFirstName(), personRequest.getFirstName());
    }

    @Test
    public void shouldReturnNullWhenNonExistingNamesGiven() {
        Person person = personDataService.findPerson(personRequest.getFirstName(), personRequest.getLastName());
        Assert.assertNull(person);
    }

    @Test
    public void shouldReturnPersonListWhenExistingNameGiven() {
        List<Person> persons = personDataService.findPersons(personRequest.getLastName());
        Assert.assertTrue(persons.size() > 0);
        Assert.assertEquals(persons.get(0).getLastName(), personRequest.getLastName());
    }

    @Test
    public void shouldReturnEmptyPersonListWhenNonExistingNameGiven() {
        List<Person> persons = personDataService.findPersons(personRequest.getFirstName());
        Assert.assertEquals(0, persons.size());
    }

    @Test
    public void shouldSavePersonWhenValidPersonRequestGiven() {
        PersonRequest newPerson = new PersonRequest("Nick", "Lego");
        int listSize = PersonDataService.PERSON_DATA.size();

        personDataService.savePerson(newPerson);

        Assert.assertEquals(PersonDataService.PERSON_DATA.size(), listSize + 1);
    }


}