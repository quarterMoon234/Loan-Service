package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ApplicationDTO;
import com.fastcampus.loan.dto.ApplicationDTO.GrantAmount;
import com.fastcampus.loan.dto.JudgmentDTO.*;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/judgments")
public class JudgmentController extends AbstractController {

    private final JudgmentService judgmentService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        Response response = judgmentService.create(request);
        return ok(response);
    }

    @GetMapping("/{judgmentId}")
    public ResponseDTO<Response> get(@PathVariable Long judgmentId) {
        Response response = judgmentService.get(judgmentId);
        return ok(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<Response> getJudgmentOfApplication(@PathVariable Long applicationId) {
        Response response = judgmentService.getJudgmentOfApplication(applicationId);
        return ok(response);
    }

    @PutMapping("/{judgmentId}")
    public ResponseDTO<Response> update(@PathVariable Long judgmentId, @RequestBody Request request) {
        Response response = judgmentService.update(judgmentId, request);
        return ok(response);
    }

    @DeleteMapping("/{judgmentId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgmentId) {
        judgmentService.delete(judgmentId);
        return ok();
    }

    @PatchMapping("/{judgementId}/grant")
    public ResponseDTO<GrantAmount> grant(@PathVariable Long judgementId) {
        GrantAmount response = judgmentService.grant(judgementId);
        return ok(response);
    }
}
