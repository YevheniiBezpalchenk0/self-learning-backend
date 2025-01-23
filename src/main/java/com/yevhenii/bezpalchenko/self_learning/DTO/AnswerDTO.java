package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerDTO {
    private int id;
    private String text;
    private boolean isCorrect;
}
