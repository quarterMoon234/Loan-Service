package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.ApplicationDTO.AcceptTerms;
import com.fastcampus.loan.dto.ApplicationDTO.Request;
import com.fastcampus.loan.dto.ApplicationDTO.Response;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.repository.AcceptTermsRepository;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void create_success() {
        //Given
        Application entity = Application.builder()
                .name("sang")
                .cellPhone("010-7156-5692")
                .email("234sang@naver.com")
                .hopeAmount(BigDecimal.valueOf(500000000))
                .build();

        Request request = Request.builder()
                .name("sang")
                .cellPhone("010-7156-5692")
                .email("234sang@naver.com")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();
        //When
        when(applicationRepository.save(any(Application.class))).thenReturn(entity);

        //Then
        Response result = applicationService.create(request);
        assertThat(result.getName()).isSameAs(entity.getName());
    }

    @Test
    void get_success() {
        //Given
        Long findId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();

        //When
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));
        //Then

        Response result = applicationService.get(findId);
        assertThat(result.getApplicationId()).isSameAs(findId);
    }

    @Test
    void update_success() {
        //Given
        Long findId = 1L;
        Request request = Request.builder()
                .hopeAmount(BigDecimal.valueOf(5000))
                .build();

        Application entity = Application.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        //When
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));
        when(applicationRepository.save(entity)).thenReturn(entity);
        //Then
        Response result = applicationService.update(findId, request);
        assertThat(result.getApplicationId()).isSameAs(findId);
        assertThat(result.getHopeAmount()).isSameAs(request.getHopeAmount());

    }

    @Test
    void delete_success() {
        //Given
        Long findId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();
        //When
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        //Then
        applicationService.delete(findId);
        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

    @Test
    void acceptTerms_success() {
        //Given
        Long applicationId = 1L;

        Terms terms1 = Terms.builder()
                .termsId(1L)
                .build();

        Terms terms2 = Terms.builder()
                .termsId(2L)
                .build();

        List<Long> acceptTermsIdList = Arrays.asList(1L, 2L);

        AcceptTerms request = AcceptTerms.builder()
                .acceptTermsIds(acceptTermsIdList)
                .build();
        //When
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll()).thenReturn(Arrays.asList(terms1, terms2));
        //Then
        boolean result = applicationService.acceptTerms(applicationId, request);
        assertThat(result).isSameAs(true);
    }

    @Test
    void acceptTerms_fail_because_your_not_accept_all_terms() {
        //Given
        Long applicationId = 1L;

        Terms terms1 = Terms.builder()
                .termsId(1L)
                .build();

        Terms terms2 = Terms.builder()
                .termsId(2L)
                .build();

        List<Long> acceptTermsIdList = Arrays.asList(1L);

        AcceptTerms request = AcceptTerms.builder()
                .acceptTermsIds(acceptTermsIdList)
                .build();
        //When
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll()).thenReturn(Arrays.asList(terms1, terms2));
        //Then
        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @Test
    void acceptTerms_fail_because_applicationId_not_exist() {
        //Given
        Long applicationId = 1L;

        Terms terms1 = Terms.builder()
                .termsId(1L)
                .build();

        Terms terms2 = Terms.builder()
                .termsId(2L)
                .build();

        List<Long> acceptTermsIdList = Arrays.asList(1L, 2L);

        AcceptTerms request = AcceptTerms.builder()
                .acceptTermsIds(acceptTermsIdList)
                .build();
        //When
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }
}
