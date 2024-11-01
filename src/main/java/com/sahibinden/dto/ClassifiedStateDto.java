package com.sahibinden.dto;

import com.sahibinden.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassifiedStateDto {
    private Status oldStatus;
    private Status newStatus;
    private LocalDateTime changeDate;
}
