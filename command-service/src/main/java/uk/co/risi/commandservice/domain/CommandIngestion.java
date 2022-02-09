package uk.co.risi.commandservice.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = "time")
public class CommandIngestion {
    @JsonAlias("identifier")
    private String vin;
    private LocalDateTime time;
    private String command;
}
