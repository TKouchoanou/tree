package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}