package uk.co.huntersix.spring.rest.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.model.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnPersonsDetails() {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldSavePersonsDetails() {
        Person person = new Person("Jay", "Chain");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, String.valueOf(APPLICATION_JSON));

        HttpEntity<Person> request = new HttpEntity<>(person, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/person", request,
                String.class);
        Assert.assertEquals(201, result.getStatusCodeValue());
    }
}