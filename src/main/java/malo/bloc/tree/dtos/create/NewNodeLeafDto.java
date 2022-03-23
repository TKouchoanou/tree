package malo.bloc.tree.dtos.create;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class NewNodeLeafDto implements Serializable {
    @NotNull
    private final String title;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;
    @Valid
    private final Set<NewLinkDto> links;
    @Valid
    private final Set<NewMetadataDto> metadata;
}
