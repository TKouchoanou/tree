package malo.bloc.tree.dtos;

import lombok.*;
import malo.bloc.tree.dtos.NodeLeafDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private   LocalTime createdAt;
    @Valid
    private   NodeLeafDto nodeLeaf;
    private   Set<Integer> childIds;
    private   Integer userId;
}
