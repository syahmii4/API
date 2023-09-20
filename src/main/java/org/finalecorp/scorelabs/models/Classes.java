package org.finalecorp.scorelabs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity @AllArgsConstructor @NoArgsConstructor
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int classId;
    @Getter
    @Setter
    private String className;
    @Getter
    @Setter
    private int teacherId;
    @Getter
    @Setter
    private Timestamp dateCreated;
    @Getter
    @Setter
    private String displayColor;
}
