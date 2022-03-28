package malo.bloc.tree.search.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.IOException;
import java.util.List;

@NoRepositoryBean
public interface SearchRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
     void runFullIndexation() ;
     String getBeanId(T bean);
     String getBeanName(T bean);
     String getIndexName();
     List <T> getAllBeans() ;
}
