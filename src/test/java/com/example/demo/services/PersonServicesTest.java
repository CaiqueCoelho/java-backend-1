package com.example.demo.services;

import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.mocks.MockPerson;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testFindAll() {
//        List<Person> list = input.mockEntityList();
//
//        when(repository.findAll()).thenReturn(list);
//        var people = service.findAll();
//        System.out.println(people);
//
//        Assertions.assertNotNull(people);
//        assertEquals(14, people.size());
//
//        var personOne = people.get(1);
//        Assertions.assertNotNull(personOne.getKey());
//        Assertions.assertNotNull(personOne.getLinks());
//        //Assertions.assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
//        assertEquals(personOne.getAddress(), list.get(1).getAddress());
//        assertEquals(personOne.getFirstName(), list.get(1).getFirstName());
//        assertEquals(personOne.getLastName(), list.get(1).getLastName());
//        assertEquals(personOne.getGender(), list.get(1).getGender());
//
//        var personFour = people.get(4);
//        Assertions.assertNotNull(personFour.getKey());
//        Assertions.assertNotNull(personFour.getLinks());
//        //Assertions.assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
//        assertEquals(personFour.getAddress(), list.get(4).getAddress());
//        assertEquals(personFour.getFirstName(), list.get(4).getFirstName());
//        assertEquals(personFour.getLastName(), list.get(4).getLastName());
//        assertEquals(personFour.getGender(), list.get(4).getGender());
//    }

    // TODO: Failing, should be returning the links, but is not
    @Test
    void testFindById() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.findById(1L);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        Assertions.assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    void testCreate() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(person)).thenReturn(persisted);

        var result = service.create(vo);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        Assertions.assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    void testUpdate() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        Person persisted = person;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);

        var result = service.update(vo);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        Assertions.assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        service.delete(1L);
    }

    @Test
    void testCreateWithNullPerson() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNullPerson() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
