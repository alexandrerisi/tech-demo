package com.risi.demo.gateway.domain.json;

import com.risi.demo.gateway.validation.Ipv4;
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
