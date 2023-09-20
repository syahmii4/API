package org.finalecorp.scorelabs.responseObjects;

import lombok.Getter;
import lombok.Setter;
import org.finalecorp.scorelabs.models.Assignment;

public class AssignmentStream extends Assignment {
    public AssignmentStream(Assignment assignment){
        super(assignment.getAssignmentId(),
                assignment.getClassId(),
                assignment.getDescription(),
                assignment.getDateCreated(),
                assignment.getDateDue(),
                assignment.getAssignmentTitle(),
                assignment.getAttempts(),
                assignment.getAssignmentType(),
                assignment.getQuestion());
    }

    @Getter @Setter
    private String displayColor;
}
