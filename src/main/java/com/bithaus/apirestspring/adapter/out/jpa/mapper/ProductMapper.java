package com.bithaus.apirestspring.adapter.out.jpa.mapper;

import com.bithaus.apirestspring.adapter.out.jpa.entity.ProductJpaEntity;
import com.bithaus.apirestspring.domain.model.Product;


public final class ProductMapper {

    public static ProductJpaEntity toJpa(Product p) {
        return ProductJpaEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .build();
    }

    public static Product toDomain(ProductJpaEntity e) {
        return Product.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .stock(e.getStock())
                .build();
    }
}
