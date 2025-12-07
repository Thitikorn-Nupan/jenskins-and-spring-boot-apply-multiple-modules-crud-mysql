package com.ttknp.api.repository;

import java.util.List;

public interface ModelRepository<T> {
    List<T> retrieveAllModels();
    <U> T retrieveModel(U key);
    Boolean createModel(T model);
    Boolean updateModel(T model);
    <U> Boolean deleteModel(U key);
}