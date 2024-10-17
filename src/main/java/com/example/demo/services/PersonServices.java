package com.example.demo.services;

import com.example.demo.controllers.PersonController;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.data.vo.v2.PersonVOV2;
import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.DozerMapper;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.logging.Logger;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all person");
        var personPage = repository.findAll(pageable);
        var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).getPerson(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class).getAllPerson(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVosPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
        logger.info("Finding all person");
        var personPage = repository.findPersonByName(firstName, pageable);
        var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).getPerson(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class).getAllPerson(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVosPage, link);
    }

    public PersonVO findById(Long id) {
        Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id " + id));
        PersonVO vo = DozerMapper.parseObject(person, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();
        logger.info("Creating person");
        Person savedPerson = repository.save(DozerMapper.parseObject(person, Person.class));
        PersonVO vo = DozerMapper.parseObject(savedPerson, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).getPerson(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating person");
        Person savedPerson = repository.save(DozerMapper.parseObject(person, Person.class));
        return DozerMapper.parseObject(savedPerson, PersonVOV2.class);
    }

    public PersonVO update(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();
        Person findPerson = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("Person not found for id " + person.getKey()));
        findPerson.setFirstName(person.getFirstName());
        findPerson.setLastName(person.getLastName());
        findPerson.setAddress(person.getAddress());
        findPerson.setGender(person.getGender());
        PersonVO vo = DozerMapper.parseObject(repository.save(findPerson), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).getPerson(findPerson.getId())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling person " + id);
        repository.disablePerson(id);
        Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id " + id));
        logger.info("Applying DozerMapper");
        PersonVO vo = DozerMapper.parseObject(person, PersonVO.class);
        logger.info("Applying withSelfRel");
        vo.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting person " + id);
        repository.delete(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id " + id)));
    }
}
