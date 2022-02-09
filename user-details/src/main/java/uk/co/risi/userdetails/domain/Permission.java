package uk.co.risi.userdetails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;

@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @JsonIgnore
    private String id;
    @NotEmpty
    private String description;
}
