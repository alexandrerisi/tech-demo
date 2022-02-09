package uk.co.risi.telematicsservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class TelematicsData {

    private final int speedMph, rpm;
    private final Gear gear;
    private LocalDateTime time;

    public static TelematicsData convertJsonToJava(TelematicsIngestion telematicsIngestion) {
        return new TelematicsData((int) (telematicsIngestion.getSpeed() * 0.62),
                telematicsIngestion.getRpm(),
                Gear.getGear(telematicsIngestion.getGear()));
    }
}
