package malo.bloc.tree.persistence.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "link")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Link implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leaf_id", nullable = false)
    @JsonIgnore
    private NodeLeaf leaf;

    @Column(name = "name")
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "updated_at", nullable = true,columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}