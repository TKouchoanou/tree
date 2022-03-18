package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataRepository extends JpaRepository<Metadata, Integer> {
}