package org.finalecorp.scorelabs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Map;

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
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> question;

}


