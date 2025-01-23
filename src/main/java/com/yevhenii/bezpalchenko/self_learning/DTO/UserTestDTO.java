package com.yevhenii.bezpalchenko.self_learning.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserTestDTO {
    private int id;
    private int totalScore;
    private List<UserAnswerDTO> answers;
}
