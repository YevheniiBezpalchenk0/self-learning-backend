package com.yevhenii.bezpalchenko.self_learning.Model;

import java.util.List;
import java.util.Optional;

public interface IService<T> {

    List<T> getAll();
    Optional<T> getById(int id);
    T update(int id, T updated);
    void delete(int id);
}
