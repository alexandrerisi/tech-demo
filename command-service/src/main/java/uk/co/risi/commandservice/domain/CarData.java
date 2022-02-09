package uk.co.risi.commandservice.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@Document("commandCarData")
public class CarData {
    @Id
    private final String vin;
    private Collection<CommandData> commandData = new ArrayList<>();
}
