package org.finalecorp.scorelabs.responseObjects;

import lombok.Getter;
import lombok.Setter;
import org.finalecorp.scorelabs.models.Classes;

public class ClassesInfo extends Classes {
    @Getter @Setter
    private String teacherName;
    public ClassesInfo(){

    };

    public ClassesInfo(Classes classesTemp) {
        super(classesTemp.getClassId(), classesTemp.getClassName(), classesTemp.getTeacherId(), classesTemp.getDateCreated());
    }
}
