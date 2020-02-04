package com.jaguarlandrover.demo.datalake.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table
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
