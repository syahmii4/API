package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Parent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends CrudRepository<Parent, Integer> {
    public Parent findParentByParentId(int parentId);
}
