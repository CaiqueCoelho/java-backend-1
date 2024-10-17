package com.example.demo.e2e.pojo;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement
public class AccountCredentialsVO implements Serializable {

    private String username;
    private String password;
}
