package com.example.demo.e2e.tests.repository;

import com.example.demo.e2e.config.Config;
import com.example.demo.e2e.config.ConfigRepository;
import com.example.demo.e2e.config.PersonEndpoints;
import com.example.demo.e2e.pojo.PersonVO;
import com.example.demo.e2e.pojo.WrapperPersonVO;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends ConfigRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PersonRepository repository;

    @Test
    public void findByName() throws IOException {

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByName("ar", pageable).getContent().getFirst();

        MatcherAssert.assertThat(person.getAddress(), not(emptyString()));
        assertFalse(person.getFirstName().isEmpty());
        assertTrue(person.getFirstName().toLowerCase().contains("ar"));
    }

    @Test
    @Transactional
    public void disablePerson() throws IOException {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByName("ar", pageable).getContent().getFirst();
        System.out.println(person.toString());
        repository.disablePerson(person.getId());

        // Clear the persistence context
        entityManager.flush();
        entityManager.clear();

        person = repository.findById(person.getId()).get();
        System.out.println(person.toString());

        MatcherAssert.assertThat(person.getAddress(), not(emptyString()));
        assertFalse(person.getFirstName().isEmpty());
        assertTrue(person.getFirstName().toLowerCase().contains("ar"));
        assertFalse(person.getEnabled());

        repository.enablePerson(person.getId());

        entityManager.flush();
        entityManager.clear();
        person = repository.findById(person.getId()).get();
        System.out.println(person.toString());
        assertTrue(person.getEnabled());
    }
}
