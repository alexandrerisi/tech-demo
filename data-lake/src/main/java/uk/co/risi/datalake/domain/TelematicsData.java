package uk.co.risi.datalake.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class TelematicsData extends Data {
    @JsonAlias("identifier")
    private String vin;
    private int speed;
    private int rpm;
    private int gear;
}
