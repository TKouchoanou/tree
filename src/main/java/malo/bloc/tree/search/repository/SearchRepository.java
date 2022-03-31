package malo.bloc.tree.search.repository;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoRepositoryBean
public interface SearchRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    public  <S extends T> Iterable<S> saveAll();
}
