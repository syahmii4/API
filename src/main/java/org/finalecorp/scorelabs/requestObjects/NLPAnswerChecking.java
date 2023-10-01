package org.finalecorp.scorelabs.requestObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NLPAnswerChecking {
    private List<String> answersAccepted;
    private List<String> answersGiven;
    private int acceptedThreshold;
}
