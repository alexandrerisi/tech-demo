package uk.co.risi.telematicsservice.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TelematicsIngestion {
    @JsonAlias("identifier")
    private String vin;
    private int rpm, speed, gear;
}
