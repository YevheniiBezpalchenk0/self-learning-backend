package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class QuestionDTO {
    private int id;
    private String text;
    private int points;
    private List<AnswerDTO> answers;
}
