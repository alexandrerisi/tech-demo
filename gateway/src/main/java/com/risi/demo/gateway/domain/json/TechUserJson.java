package com.risi.demo.gateway.domain.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TechUserJson {

    private String username;
    private String password;
    private Collection<String> permissions;
}
