package malo.bloc.tree.dtos;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeDto implements Serializable {
    private   Integer id;
    private   Integer parentId;
    @NotNull
    @NotBlank
    private   String name;
    private   LocalDateTime updatedAt;
    private   LocalDateTime createdAt;
    @Valid
    private   NodeLeafDto nodeLeaf;
    private   Set<TreeDto> children;
    private   Integer userId;
}
