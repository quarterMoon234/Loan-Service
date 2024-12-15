package com.fastcampus.loan.controller;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.CounselServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.fastcampus.loan.dto.CounselDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/counsels")
public class CounselController extends AbstractController{

    private final CounselServiceImpl counselService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        Response response = counselService.create(request);
        return ok(response);
    }

    @GetMapping("/{counselId}")
    public ResponseDTO get(@PathVariable Long counselId) {
        Response response = counselService.get(counselId);
        return ok(response);
    }

    @PutMapping("/{counselId}")
    public ResponseDTO update(@PathVariable Long counselId, @RequestBody Request request) {
        Response response = counselService.update(counselId, request);
        return ok(response);
    }

    @DeleteMapping("{counselId}")
    public ResponseDTO delete(@PathVariable Long counselId) {
        counselService.delete(counselId);
        return ok();
    }
}
