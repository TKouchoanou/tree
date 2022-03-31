package malo.bloc.tree.search.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component("main")
public class UserSearchRepository implements malo.bloc.tree.search.repository.UserRepository {
    @Autowired
    private UserRepositoryPartial userRepositoryPartial;
    @Override
    public <S extends malo.bloc.tree.search.bean.User> Iterable<S> saveAll() {
        return userRepositoryPartial.saveAll();
    }

    @Override
    public Iterable<malo.bloc.tree.search.bean.User> findAll(Sort sort) {
        return userRepositoryPartial.findAll(sort);
    }

    @Override
    public Page<malo.bloc.tree.search.bean.User> findAll(Pageable pageable) {
        return userRepositoryPartial.findAll(pageable);
    }

    @Override
    public <S extends malo.bloc.tree.search.bean.User> S save(S entity) {
        return userRepositoryPartial.save(entity);
    }

    @Override
    public <S extends malo.bloc.tree.search.bean.User> Iterable<S> saveAll(Iterable<S> entities) {
        return userRepositoryPartial.saveAll(entities);
    }

    @Override
    public Optional<malo.bloc.tree.search.bean.User> findById(java.lang.String id) {
        return userRepositoryPartial.findById(id);
    }

    @Override
    public boolean existsById(java.lang.String s) {
        return userRepositoryPartial.existsById(s);
    }

    @Override
    public Iterable<malo.bloc.tree.search.bean.User> findAll() {
        return userRepositoryPartial.findAll();
    }

    @Override
    public Iterable<malo.bloc.tree.search.bean.User> findAllById(Iterable<java.lang.String> ids) {
        return userRepositoryPartial.findAllById(ids);
    }

    @Override
    public long count() {
        return userRepositoryPartial.count();
    }

    @Override
    public void deleteById(java.lang.String id) {
        userRepositoryPartial.deleteById(id);

    }

    @Override
    public void delete(malo.bloc.tree.search.bean.User entity) {
              userRepositoryPartial.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends java.lang.String> ids) {
              userRepositoryPartial.deleteAllById(ids);
    }

    @Override
    public void deleteAll(Iterable<? extends malo.bloc.tree.search.bean.User> entities) {
              userRepositoryPartial.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
       userRepositoryPartial.deleteAll();
    }
}
