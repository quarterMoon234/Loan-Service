package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.EntryDto.*;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final BalanceService balanceService;

    private final EntryRepository entryRepository;

    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);
        entryRepository.save(entry);

        balanceService.create(applicationId, BalanceDTO.Request.builder()
                .applicationId(applicationId)
                .entryAmount(request.getEntryAmount())
                .build());

        return modelMapper.map(entry, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);

        if (entry.isPresent()) {
            return modelMapper.map(entry, Response.class);
        } else {
            return null;
        }
    }

    @Override
    public UpdateResponse update(Long entryId, Request request) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        entry.setEntryAmount(request.getEntryAmount());

        entryRepository.save(entry);

        Long applicationId = entry.getApplicationId();

        balanceService.update(applicationId, BalanceDTO.UpdateRequest.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build());

        return UpdateResponse.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }

    @Override
    public Void delete(Long entryId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        entry.setIsDeleted(true);

        entryRepository.save(entry);

        Long applicationId = entry.getApplicationId();

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        balanceService.update(applicationId, BalanceDTO.UpdateRequest.builder()
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(BigDecimal.ZERO)
                .build());

        return null;
    }

    private boolean isContractedApplication(Long applicationId) {

        Optional<Application> application = applicationRepository.findById(applicationId);

        if (!application.isPresent()) {
            return false;
        }

        return application.get().getContractedAt() != null;
    }
}
