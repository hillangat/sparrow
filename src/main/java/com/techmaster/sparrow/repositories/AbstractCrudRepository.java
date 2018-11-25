package com.techmaster.sparrow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractCrudRepository<T, ID> extends CrudRepository<T, ID> {

    @Override
    default <S extends T> S save(S s) {
        return null;
    }

    @Override
    default <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    default Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    default boolean existsById(ID id) {
        return false;
    }

    @Override
    default Iterable<T> findAll() {
        return null;
    }

    @Override
    default Iterable<T> findAllById(Iterable<ID> iterable) {
        return null;
    }

    @Override
    default long count() {
        return 0;
    }

    @Override
    default void deleteById(ID id) {

    }

    @Override
    default void delete(T t) {

    }

    @Override
    default void deleteAll(Iterable<? extends T> iterable) {

    }

    @Override
    default void deleteAll() {

    }

}
