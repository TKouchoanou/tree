package malo.bloc.tree.dtos;

import lombok.*;

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
public class NodeLeafDto implements Serializable {
    private   Integer id;
    private   Integer treeId;
    @NotNull
    @NotBlank
    private   String title;
    private   Set<LinkDto> links;
    private   Set<MetadataDto> metadata;
    private   LocalDateTime updatedAt;
    private   LocalDateTime createdAt;
}
