package uk.co.risi.routingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activation {

    @Id
    private String vin;
    private int telematicsActivations;
    private int commandsActivations;
}
