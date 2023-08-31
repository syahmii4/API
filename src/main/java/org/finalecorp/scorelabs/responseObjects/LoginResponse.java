package org.finalecorp.scorelabs.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
}
