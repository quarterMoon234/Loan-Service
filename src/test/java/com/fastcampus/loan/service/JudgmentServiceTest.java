package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Judgment;
import com.fastcampus.loan.dto.ApplicationDTO;
import com.fastcampus.loan.dto.ApplicationDTO.GrantAmount;
import com.fastcampus.loan.dto.JudgmentDTO.*;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.JudgmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JudgmentServiceTest {

    @InjectMocks
    private JudgmentServiceImpl judgmentService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JudgmentRepository judgmentRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void create_success() {
        //Given
        Request request = Request.builder()
                .applicationId(1L)
                .name("엄상현")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();
        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .name("엄상현")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        //When

        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgmentRepository.save(any(Judgment.class))).thenReturn(entity);

        //Then
        Response result = judgmentService.create(request);
        assertThat(result.getJudgmentId()).isSameAs(1L);
    }

    @Test
    void get_success() {
        //Given
        Long judgmentId = 1L;

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .name("신창섭")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();
        //When
        when(judgmentRepository.findById(judgmentId)).thenReturn(Optional.ofNullable(entity));
        //Then
        Response result = judgmentService.get(judgmentId);
        assertThat(result.getJudgmentId()).isSameAs(judgmentId);
    }

    @Test
    void getJudgmentOfApplication() {
        //Given
        Long applicationId = 1L;

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .name("신창섭")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();
        //When
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgmentRepository.findByApplicationId(applicationId)).thenReturn(Optional.ofNullable(entity));
        //Then
        Response result = judgmentService.getJudgmentOfApplication(applicationId);
        assertThat(result.getApplicationId()).isSameAs(applicationId);
    }

    @Test
    void update_succes() {
        //Given
        Long judgementId = 1L;

        Request request = Request.builder()
                .name("신창섭")
                .approvalAmount(BigDecimal.valueOf(10000000))
                .build();

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .name("강원기")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        //When
        when(judgmentRepository.findById(judgementId)).thenReturn(Optional.ofNullable(entity));

        //Then
        Response result = judgmentService.update(judgementId, request);
        assertThat(result.getJudgmentId()).isSameAs(1L);
        assertThat(result.getName()).isSameAs(request.getName());
        assertThat(result.getApprovalAmount()).isSameAs(request.getApprovalAmount());
    }

    @Test
    void delete_success() {
        //Given
        Long judgmentId = 1L;

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .build();
        //When
        when(judgmentRepository.findById(judgmentId)).thenReturn(Optional.ofNullable(entity));
        //Then
        judgmentService.delete(judgmentId);
        assertThat(entity.getIsDeleted()).isSameAs(true);

    }

    @Test
    void grant_success() {
        //Given
        Long judgmentId = 1L;

        Judgment judgment = Judgment.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .name("신창섭")
                .approvalAmount(BigDecimal.valueOf(10000000))
                .build();

        Application application = Application.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(10000000))
                .build();

        //When
        when(judgmentRepository.findById(judgmentId)).thenReturn(Optional.of(judgment));
        when(applicationRepository.findById(judgment.getApplicationId())).thenReturn(Optional.ofNullable(application));

        //Then
        GrantAmount result = judgmentService.grant(judgmentId);
        assertThat(result.getApprovalAmount()).isSameAs(judgment.getApprovalAmount());
    }
}
