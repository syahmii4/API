package org.finalecorp.scorelabs.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.finalecorp.scorelabs.models.Classroom;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StudentInfo extends Classroom {
    public StudentInfo(Classroom classroom) {
        super(classroom.getClassroomId(), classroom.getStudentId(), classroom.getClassId(), classroom.getJoinDate(), classroom.getLastViewed());
    }
    private String fullName;
    private String userName;
    private String profilePicture;
}
