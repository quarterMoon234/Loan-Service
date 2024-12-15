package com.fastcampus.loan.dto;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JudgmentDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Request {

        private Long applicationId;

        private String name;

        private BigDecimal approvalAmount;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response implements Serializable {

        private Long judgmentId;

        private Long applicationId;

        private String name;

        private BigDecimal approvalAmount;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;


    }
}
