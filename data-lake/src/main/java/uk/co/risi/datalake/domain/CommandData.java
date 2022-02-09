package uk.co.risi.datalake.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CommandData extends Data {
    @JsonAlias("identifier")
    private String vin;
    private String command;
}
