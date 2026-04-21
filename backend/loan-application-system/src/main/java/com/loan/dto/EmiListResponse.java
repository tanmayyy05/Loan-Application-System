package com.loan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmiListResponse {

    private boolean hasActiveLoan;
    private String message;
    private List<EmiScheduleResponse> emis;
}
