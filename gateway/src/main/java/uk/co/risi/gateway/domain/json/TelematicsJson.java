package uk.co.risi.gateway.domain.json;

import uk.co.risi.gateway.validation.Ipv4;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TelematicsJson {
    private int speed;
    private int rpm;
    private int gear;
    @Ipv4
    private String identifier;
}
