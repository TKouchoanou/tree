package malo.bloc.tree.persistence.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Tree implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    @ToString.Exclude
    private Tree parent;

    @Column(name = "name")
    private String name;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "tree",cascade = CascadeType.ALL)
    private NodeLeaf nodeLeaf;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Tree> children = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @PrePersist
    protected void onUpdate() {
       if(this.createdAt==null);
       this.createdAt=LocalDateTime.now();
    }
}