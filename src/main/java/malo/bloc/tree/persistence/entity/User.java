package malo.bloc.tree.persistence.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "tree_id")
    private Tree tree;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "adresse", length = 150)
    private String adresse;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @PrePersist
    void preInsert() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.from(LocalDateTime.now());
        }
        if (this.updatedAt == null) {
            this.updatedAt= LocalDateTime.from(LocalDateTime.now());
        }
    }
}