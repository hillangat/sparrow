package com.techmaster.sparrow.services;

public interface RepositoryService<T> {
    <T> T getRepository();
}
