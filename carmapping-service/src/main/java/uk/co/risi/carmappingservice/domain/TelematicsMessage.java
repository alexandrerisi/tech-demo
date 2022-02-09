package uk.co.risi.carmappingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelematicsMessage extends MessageIdentifier {

    @Min(value = 0)
    private int speed;

    @Min(value = -1)
    @Max(value = 6)
    private int gear;

    @Min(value = 0)
    private int rpm;
}
