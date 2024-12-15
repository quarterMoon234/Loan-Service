package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.EntryDto.Request;
import com.fastcampus.loan.dto.EntryDto.Response;
import com.fastcampus.loan.dto.EntryDto.UpdateResponse;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.dto.RepaymentDTO.*;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.EntryService;
import com.fastcampus.loan.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController{

    private final EntryService entryService;

    private final RepaymentService repaymentService;

    @PostMapping("/{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        Response response = entryService.create(applicationId, request);
        return ok(response);
    }

    @GetMapping("/{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        Response response = entryService.get(applicationId);
        return ok(response);
    }

    @PutMapping("/entries/{entryId}")
    public ResponseDTO<UpdateResponse> update(@PathVariable Long entryId, @RequestBody Request request) {
        UpdateResponse response = entryService.update(entryId, request);
        return ok(response);
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseDTO<Void> delete(@PathVariable Long entryId) {
        entryService.delete(entryId);
        return ok();
    }

    @PostMapping("/{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(@PathVariable Long applicationId, @RequestBody RepaymentDTO.Request request) {
        RepaymentDTO.Response response = repaymentService.create(applicationId, request);

        return ok(response);
    }

    @GetMapping("/{applicationId}/repayments")
    public ResponseDTO<List<ListResponse>> getPayments(@PathVariable Long applicationId) {
        List<ListResponse> responses = repaymentService.get(applicationId);

        return ok(responses);
    }

    @PutMapping("/repayments/{repaymentId}")
    public ResponseDTO<RepaymentDTO.UpdateResponse> update(@PathVariable Long repaymentId, @RequestBody RepaymentDTO.Request request) {
        RepaymentDTO.UpdateResponse response = repaymentService.update(repaymentId, request);
        return ok(response);
    }

    @DeleteMapping("/repayments/{repaymentId}")
    public ResponseDTO<Void> deleteRepayment(@PathVariable Long repaymentId) {
        repaymentService.delete(repaymentId);
        return ok();
    }
}
