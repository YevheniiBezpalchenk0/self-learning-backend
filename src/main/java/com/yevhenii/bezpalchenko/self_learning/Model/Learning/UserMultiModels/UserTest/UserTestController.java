package com.yevhenii.bezpalchenko.self_learning.Model.Learning.UserMultiModels.UserTest;

import com.yevhenii.bezpalchenko.self_learning.DTO.UserTestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-test")
@Slf4j
@RequiredArgsConstructor
public class UserTestController {
    private final UserTestService userTestService;

    @PostMapping
    public ResponseEntity<UserTestDTO> submitTest(@RequestBody UserTestDTO userTestDTO, @RequestHeader("Authorization") String token) {
        UserTestDTO result = userTestService.evaluateTest(userTestDTO, token);
        return ResponseEntity.ok(result);
    }
}
