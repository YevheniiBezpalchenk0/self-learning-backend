package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserAnswerDTO {
    private int questionId;
    private int answerId;
    private boolean isCorrect;
    private int score;
}
