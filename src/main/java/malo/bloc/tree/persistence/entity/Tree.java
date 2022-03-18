package malo.bloc.tree.persistence.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tree")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tree implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Tree parent;

    @Column(name = "name")
    private String name;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = true)
    private LocalTime createdAt;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tree",cascade = CascadeType.ALL)
    private NodeLeaf nodeLeaf;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private Set<Tree> children = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tree")
    @JsonIgnore
    private User user;
}