package malo.bloc.tree.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDto implements Serializable {
    private   Integer id;
    private   Integer leafId;
    @NonNull
    private   String name;
    @NotNull
    private   String value;
    private   LocalDateTime updatedAt;
}
