package malo.bloc.tree.persistence.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners({AuditingEntityListener.class})
public class Link implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leaf_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private NodeLeaf leaf;

    @Column(name = "name")
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    @LastModifiedDate
    @Column(name = "updated_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}