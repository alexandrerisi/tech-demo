package com.jaguarlandrover.demo.gateway.domain.json;

import com.jaguarlandrover.demo.gateway.validation.Ipv4;
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
