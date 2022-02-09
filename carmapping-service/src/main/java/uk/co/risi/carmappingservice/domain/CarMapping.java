package uk.co.risi.carmappingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarMapping {
    @Id
    private String ip;
    @Length(max = 17, min = 17)
    private String vin;
}
