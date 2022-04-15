package malo.bloc.tree.dtos;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends PartialUserDto implements Serializable {
    @Valid
    private TreeDto tree;
    @NotNull
    private   Integer id;
    private   String firstName;
    private   String lastName;
    @Email
    @NotNull
    private   String email;
    private   Set<String> roleNames;
    private   String country;
    private   String city;
    private   String address;
    private   LocalDateTime updatedAt;
    private   LocalDateTime createdAt;
  
}
