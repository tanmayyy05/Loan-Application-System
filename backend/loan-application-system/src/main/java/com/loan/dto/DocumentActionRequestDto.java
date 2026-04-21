package com.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentActionRequestDto {

    private Integer documentId;
    private String remarks; // optional for approve, required for reject/return
}
