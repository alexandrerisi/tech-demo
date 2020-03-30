package com.risi.demo.gateway.domain.json;

import com.risi.demo.gateway.validation.Ipv4;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanCommandJson {
    @Ipv4
    private String identifier;
    private String command;
}
