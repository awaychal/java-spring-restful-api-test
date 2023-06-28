package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.request.PersonRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonDataService personDataService;

    private Person person;

    private PersonRequest personRequest;

    @Before
    public void setUp() {
        person = new Person("Mary", "Smith");
        personRequest = new PersonRequest("Nick", "Lab");
    }

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(person);
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNotFoundResponseWhenNonExistingNamesGiven() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(get("/person/smith1/mary"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnPersonsFromServiceWhenExistingNameGiven() throws Exception {
        List<Person> list = new ArrayList<>();
        list.add(person);
        when(personDataService.findPersons(any())).thenReturn(list);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNoContentResponseWhenNonExistingNameGiven() throws Exception {
        List<Person> list = new ArrayList<>();
        when(personDataService.findPersons(any())).thenReturn(list);
        this.mockMvc.perform(get("/person/smith1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldSavePersonWhenNonExistingValidNameGiven() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(post("/person").
                        accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestResponseWhenInvalidNamesGiven() throws Exception {
        personRequest.setFirstName("");
        this.mockMvc.perform(post("/person")
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnConflictResponseWhenExistingNamesGiven() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(person);
        this.mockMvc.perform(post("/person").
                        accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }


}