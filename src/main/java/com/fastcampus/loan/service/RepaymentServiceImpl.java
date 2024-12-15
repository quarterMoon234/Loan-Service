package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.dto.RepaymentDTO.*;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import com.fastcampus.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final ApplicationRepository applicationRepository;

    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {

        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);

        repayment.setApplicationId(applicationId);

        Repayment saved = repaymentRepository.save(repayment);

        Response response = modelMapper.map(saved, Response.class);

        BalanceDTO.Response balance = balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                .RepaymentAmount(request.getRepaymentAmount())
                .build());

        response.setBalance(balance.getBalance());

        return response;
    }

    @Override
    public List<ListResponse> get(Long applicationId) {
        List<Repayment> repaymentList = repaymentRepository.findAllByApplicationId(applicationId);

        return repaymentList.stream().map(r -> modelMapper.map(r, ListResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UpdateResponse update(Long repaymentId, Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();
        BigDecimal afterRepaymentAmount = request.getRepaymentAmount();
        Long applicationId = repayment.getApplicationId();

        repayment.setRepaymentAmount(afterRepaymentAmount);

        repaymentRepository.save(repayment);

        balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .RepaymentAmount(beforeRepaymentAmount)
                .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                .build());

        BalanceDTO.Response balance = balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .RepaymentAmount(afterRepaymentAmount)
                .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                .build());

        return UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(afterRepaymentAmount)
                .balance(balance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        repayment.setIsDeleted(true);

        repaymentRepository.save(repayment);

        balanceService.repaymentUpdate(applicationId, BalanceDTO.RepaymentRequest.builder()
                .RepaymentAmount(removeRepaymentAmount)
                .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                .build());
    }

    private boolean isRepayableApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        if (application.getContractedAt() == null) {
            return false;
        }

        return entryRepository.findByApplicationId(applicationId).isPresent();
    }
}
