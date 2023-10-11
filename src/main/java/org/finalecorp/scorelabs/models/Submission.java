package org.finalecorp.scorelabs.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity @NoArgsConstructor @AllArgsConstructor
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
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Object, Object> answer;
    @Getter
    @Setter
    private int points;



}
