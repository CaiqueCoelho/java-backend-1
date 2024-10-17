package com.example.demo.services;

import com.example.demo.controllers.BookController;
import com.example.demo.data.vo.v1.BookVO;
import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.DozerMapper;
import com.example.demo.model.Book;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookVO> findAll() {
        logger.info("Finding all book");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books.forEach(p -> p.add(linkTo(methodOn(BookController.class).getBook(p.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id) {
        Book book = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found for id " + id));
        BookVO vo = DozerMapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).getBook(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Creating book");
        Book savedBook = repository.save(DozerMapper.parseObject(book, Book.class));
        BookVO vo = DozerMapper.parseObject(savedBook, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).getBook(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        Book findBook = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("Book not found for id " + book.getKey()));
        findBook.setAuthor(book.getAuthor());
        findBook.setPrice(book.getPrice());
        findBook.setTitle(book.getTitle());
        findBook.setLaunchDate(book.getLaunchDate());
        BookVO vo = DozerMapper.parseObject(repository.save(findBook), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).getBook(findBook.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting book " + id);
        repository.delete(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found for id " + id)));
    }
}
