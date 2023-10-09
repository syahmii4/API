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
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int classroomId;
    @Getter
    @Setter
    private int studentId;
    @Getter
    @Setter
    private int classId;
    @Getter
    @Setter
    private Timestamp joinDate;
    @Getter
    @Setter
    private Timestamp lastViewed;
}
