package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Balance;
import com.fastcampus.loan.dto.BalanceDTO.*;
import com.fastcampus.loan.dto.BalanceDTO.Request;
import com.fastcampus.loan.dto.BalanceDTO.Response;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();

        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, Response.class);
    }

    @Override
    public Response update(Long applicationId, UpdateRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();

        BigDecimal afterEntryAmount = request.getAfterEntryAmount();

        BigDecimal updatedBalance = balance.getBalance().subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, Response.class);
    }

    @Override
    public Response repaymentUpdate(Long applicationId, RepaymentRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        BigDecimal updatedBalance = balance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();

        if (request.getType().equals(RepaymentRequest.RepaymentType.ADD)) {
            updatedBalance = updatedBalance.add(repaymentAmount);
        } else {
            updatedBalance = updatedBalance.subtract(repaymentAmount);
            if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                updatedBalance = BigDecimal.valueOf(0);
            }
        }

        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, Response.class);
    }
}
