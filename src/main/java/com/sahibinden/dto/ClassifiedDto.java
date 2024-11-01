package com.sahibinden.dto;

import com.sahibinden.enums.Category;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ClassifiedDto {
    @Size(min = 10, max = 50)
    private String title;
    @Size(min = 20, max = 200)
    private String detail;
    private Category category;
    @Nullable
    private LocalDateTime createDate;
    private LocalDateTime endDate;

    public LocalDateTime calculateEndDate(Category category) {
        switch (category) {
            case EMLAK:
                return createDate != null ? createDate.plusWeeks(4) : LocalDateTime.now().plusWeeks(4);
            case VASITA:
                return createDate != null ? createDate.plusWeeks(3) : LocalDateTime.now().plusWeeks(3);
            case ALISVERIS:
            case DIGER:
                return createDate != null ? createDate.plusWeeks(8) : LocalDateTime.now().plusWeeks(8);
            default:
                throw new IllegalArgumentException("Geçersiz kategori");
        }}
    }
