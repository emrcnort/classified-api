package com.sahibinden.dto;

import com.sahibinden.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
public class StatusCountDto {
    private Status status;
    private Long count;
}
