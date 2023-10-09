package org.finalecorp.scorelabs.requestObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClassroomAddForm {
    private int classId;
    private String classroomUsername;
}
