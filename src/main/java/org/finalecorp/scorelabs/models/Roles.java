package org.finalecorp.scorelabs.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Roles {
    @Id
    @Getter
    @Setter
    private int roleId;
    @Getter
    @Setter
    private String roleName;
}
