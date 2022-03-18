package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Tree, Integer> {
}