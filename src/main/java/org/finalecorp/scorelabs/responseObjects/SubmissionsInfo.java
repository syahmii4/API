package org.finalecorp.scorelabs.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.finalecorp.scorelabs.models.Submission;

import java.util.Map;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SubmissionsInfo extends Submission {
    public SubmissionsInfo(Submission submission) {
        super(submission.getSubmissionId(), submission.getAssignmentId(), submission.getStudentId(), submission.getAnswer(), submission.getPoints());
    }

    private String fullName;
    private String userName;
    private String profilePicture;
}
