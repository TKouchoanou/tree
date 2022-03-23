package malo.bloc.tree.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private TreeDto tree;
    @NotNull
    private   Integer id;
    private   String firstName;
    private   String lastName;
    @Email
    @NotNull
    private   String email;
    private   String country;
    private   String city;
    private   String address;
    private   LocalDateTime updatedAt;
    private   LocalDateTime createdAt;
  
}
