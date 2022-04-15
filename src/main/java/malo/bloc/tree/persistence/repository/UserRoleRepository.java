package malo.bloc.tree.persistence.repository;

import malo.bloc.tree.persistence.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}