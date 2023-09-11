package org.finalecorp.scorelabs.repositories;

import org.finalecorp.scorelabs.models.Classes;
import org.finalecorp.scorelabs.models.Parent;
import org.finalecorp.scorelabs.responseObjects.ClassesInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends CrudRepository<Classes, Integer> {
    public Classes findClassesByClassId(int classId);
    public List<Classes> findClassesByTeacherId(int teacherId);

    Classes findClassesByClassIdAndTeacherId(int classId, int teacherId);
}
