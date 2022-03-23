package malo.bloc.tree.dtos.create;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
public class NewTreeDto implements Serializable {
    @NotNull @NotBlank
    private final String name;
    private final LocalDateTime updatedAt;
    private final LocalTime createdAt;
    @Valid
    private final NewNodeLeafDto nodeLeaf;
    private final Set<NewTreeDto> metadata;
}
