package org.finalecorp.scorelabs.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter @AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
}
