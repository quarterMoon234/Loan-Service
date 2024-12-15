package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.TermsDTO.*;

import java.util.List;

public interface TermsService {

    Response create (Request request);

    List<Response> getAll();
}
