package org.finalecorp.scorelabs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Users {
    @Id
    @Getter
    @Setter
    private int userId;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String fullName;
    @Getter
    @Setter
    private int role;
    @Getter
    @Setter
    private Date dateOfBirth;
    @Getter
    @Setter
    private String addressLine1;
    @Getter
    @Setter
    private String addressLine2;
    @Getter
    @Setter
    private String city;
    @Getter
    @Setter
    private String postcode;
    @Getter
    @Setter
    private String state;
    @Getter
    @Setter
    private Timestamp dateCreated;
    @Getter
    @Setter
    private Timestamp lastLogin;


}
