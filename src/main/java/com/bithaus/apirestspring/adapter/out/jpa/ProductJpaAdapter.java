package com.bithaus.apirestspring.adapter.out.jpa;
import com.bithaus.apirestspring.adapter.out.jpa.mapper.ProductMapper;
import com.bithaus.apirestspring.adapter.out.jpa.repository.ProductJpaRepository;
import  com.bithaus.apirestspring.domain.model.Product;
import  com.bithaus.apirestspring.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductRepository {
    private final ProductJpaRepository jpaRepo;

    @Override
    public Product save(Product product){
        return ProductMapper.toDomain(jpaRepo.save(ProductMapper.toJpa(product)));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepo.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepo.findAll().stream().map(ProductMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
        jpaRepo.flush();   // ← fuerza el DELETE SQL inmediatamente
    }
}
