package com.loan.dto;

import java.time.LocalDateTime;

import com.loan.constants.DocumentStatus;
import com.loan.constants.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {

    private Integer documentId;
    private DocumentType documentType;
    private String documentUrl;
    private DocumentStatus documentStatus;
    private String remarks;
    private LocalDateTime uploadedAt;
}
