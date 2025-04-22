package com.bithaus.apirestspring.domain.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Product {
    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private OffsetDateTime createdAt;
}
