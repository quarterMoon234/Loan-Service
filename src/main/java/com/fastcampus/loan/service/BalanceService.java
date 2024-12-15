package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.BalanceDTO.*;

public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);
}
