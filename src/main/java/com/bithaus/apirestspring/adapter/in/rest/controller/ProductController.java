package com.bithaus.apirestspring.adapter.in.rest.controller;

import com.bithaus.apirestspring.adapter.in.rest.dto.ProductRequestDto;
import com.bithaus.apirestspring.adapter.in.rest.dto.ProductResponseDto;
import com.bithaus.apirestspring.domain.model.Product;
import com.bithaus.apirestspring.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@Validated @RequestBody ProductRequestDto dto) {
        Product created = service.create(toDomain(dto));
        return toResponse(created);
    }

    @GetMapping
    public List<ProductResponseDto> getAll() {
        return service.getAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable UUID id) {   // <-- UUID aquí
        return toResponse(service.getById(id));
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable UUID id,
            @Validated @RequestBody ProductRequestDto dto
    ) {
        // Construimos el dominio incluyendo el ID
        Product toUpdate = Product.builder()
                .id(id)
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stock(dto.stock())
                .build();
        Product updated = service.update(toUpdate);
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }


    // ----------  helpers ----------
    private Product toDomain(ProductRequestDto d) {
        return Product.builder()
                .name(d.name())
                .description(d.description())
                .price(d.price())
                .stock(d.stock())
                .build();
    }
    private ProductResponseDto toResponse(Product p) {
        return ProductResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .build();
    }



}
