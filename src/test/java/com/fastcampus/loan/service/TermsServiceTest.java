package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.TermsDTO.*;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {

    @InjectMocks
    private TermsServiceImpl termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void create_success() {
        //Given
        Request request = Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-store.acc/sang234")
                .build();

        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-store.acc/sang234")
                .build();
        //When
        when(termsRepository.save(any(Terms.class))).thenReturn(entity);

        //Then
        Response result = termsService.create(request);

        assertThat(result.getName()).isSameAs(request.getName());
        assertThat(result.getTermsDetailUrl()).isSameAs(request.getTermsDetailUrl());
    }

    @Test
    void getAll_success() {
        //Given
        Terms entity1 = Terms.builder()
                .name("약관1")
                .termsDetailUrl("https://license1")
                .build();

        Terms entity2 = Terms.builder()
                .name("약관2")
                .termsDetailUrl("https://license2")
                .build();

        List<Terms> list = new ArrayList<>(Arrays.asList(entity1, entity2));
        //When
        when(termsRepository.findAll()).thenReturn(list);
        //Then
        List<Response> result = termsService.getAll();

        assertThat(result.size()).isSameAs(list.size());
    }
}
