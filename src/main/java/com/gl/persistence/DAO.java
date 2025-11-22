package com.gl.persistence;

import java.util.List;

public interface DAO<T> {
    void save(T entity);
    T findById(int id);
    List<T> findAll();
    void update(T entity);
    void delete(int id);
}
