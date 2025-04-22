package com.bithaus.apirestspring.adapter.in.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(
        @NotBlank String name,
        String description,
        @DecimalMin("0.0") BigDecimal price,
        @Min(0) Integer stock
) { }

