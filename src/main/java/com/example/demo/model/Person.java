package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name="last_name", nullable = false, length = 80)
    private String lastName;
    @Column(nullable = false, length = 180)
    private String address;
    @Column(nullable = false, length = 20)
    private String gender;
    @Column(nullable = false)
    private Boolean enabled;
}
