package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.ApplicationDTO.*;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);

    Boolean acceptTerms(long applicationId, AcceptTerms request);

    Response contract(Long applicationId);
}
