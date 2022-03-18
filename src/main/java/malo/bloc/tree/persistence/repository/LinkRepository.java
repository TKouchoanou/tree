package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Integer> {
}