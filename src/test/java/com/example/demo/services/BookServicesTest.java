package com.example.demo.services;

import com.example.demo.data.vo.v1.BookVO;
import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.mocks.MockBook;
import com.example.demo.mocks.MockBook;
import com.example.demo.model.Book;
import com.example.demo.model.Book;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.BookRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

    MockBook input;

    @InjectMocks
    private BookServices service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Book> list = input.mockEntityList();

        // Mock Mockito
        when(repository.findAll()).thenReturn(list);
        var books = service.findAll();
        System.out.println(books);

        Assertions.assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);
        Assertions.assertNotNull(bookOne.getKey());
        Assertions.assertNotNull(bookOne.getLinks());
        //Assertions.assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals(bookOne.getKey(), list.get(1).getId());
        assertEquals(bookOne.getAuthor(), list.get(1).getAuthor());
        assertEquals(bookOne.getPrice(), list.get(1).getPrice());
        assertEquals(bookOne.getTitle(), list.get(1).getTitle());

        var bookFour = books.get(1);
        Assertions.assertNotNull(bookFour.getKey());
        Assertions.assertNotNull(bookFour.getLinks());
        //Assertions.assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals(bookFour.getKey(), list.get(1).getId());
        assertEquals(bookFour.getAuthor(), list.get(1).getAuthor());
        assertEquals(bookFour.getPrice(), list.get(1).getPrice());
        assertEquals(bookFour.getTitle(), list.get(1).getTitle());
    }

    // TODO: Failing, should be returning the links, but is not
    @Test
    void testFindById() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        var result = service.findById(1L);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        //Assertions.assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getPrice(), result.getPrice());
        assertEquals(book.getAuthor(), result.getAuthor());
    }

    @Test
    void testCreate() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(book)).thenReturn(persisted);

        var result = service.create(vo);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        //Assertions.assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getPrice(), result.getPrice());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
    }

    @Test
    void testUpdate() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        Book persisted = book;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = service.update(vo);
        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getKey());
        Assertions.assertNotNull(result.getLinks());
        //Assertions.assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    void testDelete() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        service.delete(1L);
    }

    @Test
    void testCreateWithNullBook() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNullBook() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
