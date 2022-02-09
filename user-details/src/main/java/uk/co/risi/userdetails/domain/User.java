package uk.co.risi.userdetails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "permissions"})
public class User {
    @Id
    @JsonIgnore
    private String id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private Collection<Permission> permissions;
}
