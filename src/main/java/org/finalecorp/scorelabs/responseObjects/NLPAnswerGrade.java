package org.finalecorp.scorelabs.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NLPAnswerGrade {
    private String answer;
    private int score;
    private String result;
}
