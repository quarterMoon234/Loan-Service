package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.ApplicationDTO.GrantAmount;
import com.fastcampus.loan.dto.JudgmentDTO.*;

public interface JudgmentService {

    Response create(Request request);

    Response get(Long judgmentId);

    Response getJudgmentOfApplication(Long applicationId);

    Response update(Long judgmentId, Request request);

    void delete(Long judgmentId);

    GrantAmount grant(Long judgmentId);
}
