package uk.co.risi.gateway.domain.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JlrUserJson {

    private String username;
    private String password;
    private Collection<String> permissions;
}
