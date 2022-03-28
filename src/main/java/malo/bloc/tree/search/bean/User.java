package malo.bloc.tree.search.bean;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private   Integer id;
    private   String firstName;
    private   String lastName;
    private   String email;
}
