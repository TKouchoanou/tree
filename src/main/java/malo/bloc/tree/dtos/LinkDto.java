package malo.bloc.tree.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto implements Serializable {
    private   Integer id;
    private   Integer leafId;
    @NotNull
    @NotBlank
    private   String name;
    @NotNull
    @NotBlank
    private   String value;
    private   LocalDateTime updatedAt;
}
