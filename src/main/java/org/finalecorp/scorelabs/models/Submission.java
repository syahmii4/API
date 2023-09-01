package org.finalecorp.scorelabs.models;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int submissionId;
    @Getter
    @Setter
    private int assignmentId;
    @Getter
    @Setter
    private int studentId;
    @Getter
    @Setter
    private JSONPObject answer;
    @Getter
    @Setter
    private int points;



}
