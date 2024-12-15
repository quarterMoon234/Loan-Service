package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.CounselRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import com.fastcampus.loan.dto.CounselDTO.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {

    @Mock
    private CounselRepository counselRepository;

    @InjectMocks
    private CounselServiceImpl counselService;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void create_success() {
        Counsel entity = Counsel.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("저는 대출을 받고 싶어요. 연락을 주세요")
                .zipCode("12345")
                .address("서울특별시 어딘구 모른동")
                .addressDetail("101동 101호")
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("저는 대출을 받고 싶어요. 연락을 주세요")
                .zipCode("12345")
                .address("서울특별시 어딘구 모른동")
                .addressDetail("101동 101호")
                .build();

        when(counselRepository.save(any(Counsel.class))).thenReturn(entity);

        Response result = counselService.create(request);

        assertThat(result.getName()).isSameAs(request.getName());
    }

    @Test
    void get_success() {
        Long findId = 1L;
        Counsel entity = Counsel.builder()
                .counselId(findId)
                .build();

        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response result = counselService.get(findId);

        assertThat(result.getCounselId()).isSameAs(findId);
    }

    @Test
    void get_fail() {
        Long findId = 2L;

        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        Assertions.assertThrows(BaseException.class, () -> counselService.get(findId));
    }

    @Test
    void update_success() {
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .name("kim")
                .build();

        Request request = Request.builder()
                .name("sang")
                .build();

        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response result = counselService.update(findId, request);

        assertThat(result.getName()).isSameAs(request.getName());
    }

    @Test
    void delete_success() {
        //given
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        //when
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        //then
        counselService.delete(findId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }
}
