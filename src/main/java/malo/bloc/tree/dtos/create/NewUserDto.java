package malo.bloc.tree.dtos.create;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NewUserDto implements Serializable {
    @Valid
    private final NewTreeDto tree;
    private final String firstName;
    private final String lastName;
    @NotNull
    @Email
    private final String email;
    private final String country;
    private final String city;
    private final String address;
    @DateTimeFormat
    private final LocalDateTime updatedAt;

    @DateTimeFormat
    private final LocalDateTime createdAt;
}
