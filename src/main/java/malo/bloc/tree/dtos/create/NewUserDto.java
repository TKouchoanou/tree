package malo.bloc.tree.dtos.create;

import lombok.*;
import lombok.experimental.Accessors;
import malo.bloc.tree.dtos.PartialUserDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Accessors(chain = true)
@Getter
@Setter
public class NewUserDto extends PartialUserDto implements Serializable {
    @Valid
    private  NewTreeDto tree;
    private  String firstName;
    private  String lastName;
    @NotNull
    @Email
    private  String email;
    @NotNull
    private  String password;
    @NotEmpty
    private       Set<String> roleNames=new HashSet<>();
    private  String country;
    private  String city;
    private  String address;
    @DateTimeFormat
    private  LocalDateTime updatedAt;

    @DateTimeFormat
    private  LocalDateTime createdAt;
}
