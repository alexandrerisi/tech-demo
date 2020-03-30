package com.risi.demo.datalake.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Table;


@EqualsAndHashCode(callSuper = true)
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CommandData extends Data{
    @JsonAlias("identifier")
    private String vin;
    private String command;
}
