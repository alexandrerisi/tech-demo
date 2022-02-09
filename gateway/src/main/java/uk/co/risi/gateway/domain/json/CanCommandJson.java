package uk.co.risi.gateway.domain.json;

import uk.co.risi.gateway.validation.Ipv4;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanCommandJson {
    @Ipv4
    private String identifier;
    private String command;
}
