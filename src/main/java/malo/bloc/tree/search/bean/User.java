package malo.bloc.tree.search.bean;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private   Integer id;
    private   String firstName;
    private   String lastName;
    private   String email;
    private   String treeId;
    private   String entityId;
}
