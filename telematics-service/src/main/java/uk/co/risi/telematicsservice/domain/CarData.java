package uk.co.risi.telematicsservice.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@Document("telematicsCarData")
public class CarData {
    @Id
    private final String vin;
    private Collection<TelematicsData> telematicsData = new ArrayList<>();
}
