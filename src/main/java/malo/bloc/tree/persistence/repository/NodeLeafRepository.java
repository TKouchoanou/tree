package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.NodeLeaf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeLeafRepository extends JpaRepository<NodeLeaf, Integer> {
}