package com.example.demo.e2e.pojo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonVO> content;
}
