package malo.bloc.tree.dtos.create;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NewMetadataDto implements Serializable {
    @NotNull
    private final String name;
    @NotNull
    private final String value;
    private final LocalDateTime updatedAt;
}
