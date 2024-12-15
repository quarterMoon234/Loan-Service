package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.dto.TermsDTO.*;
import com.fastcampus.loan.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController extends AbstractController{

    private final TermsService termsService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        Response response = termsService.create(request);
        return ok(response);
    }

    @GetMapping
    public ResponseDTO<List<Response>> getAll() {
        List<Response> response = termsService.getAll();
        return ok(response);
    }
}
