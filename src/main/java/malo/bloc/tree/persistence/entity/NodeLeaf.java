package malo.bloc.tree.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "node_leaf")
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class NodeLeaf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tree_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Tree tree;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "leaf",cascade = CascadeType.ALL)
    private Set<Link> links = new LinkedHashSet<>();

    @OneToMany(mappedBy = "leaf",cascade = CascadeType.ALL)
    private Set<Metadata> metadata = new LinkedHashSet<>();

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;


}