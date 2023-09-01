package org.finalecorp.scorelabs.models;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int assignmentId;
    @Getter
    @Setter
    private int subjectId;
    @Getter
    @Setter
    private Timestamp dateCreated;
    @Getter
    @Setter
    private Timestamp dateDue;
    @Getter
    @Setter
    private String assignmentTitle;
    @Getter
    @Setter
    private int attempts;
    @Getter
    @Setter
    private String assignmentType;
    @Getter
    @Setter
    private JSONPObject question;

}


