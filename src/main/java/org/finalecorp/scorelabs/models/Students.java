package org.finalecorp.scorelabs.models;

<<<<<<< Updated upstream
public class Students {
=======
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int studentId;
    @Getter
    @Setter
    private int suerId;
    @Getter
    @Setter
    private int academicLevel;
    @Getter
    @Setter
    private int academicYear;
    @Getter
    @Setter
    private int points;


>>>>>>> Stashed changes
}
