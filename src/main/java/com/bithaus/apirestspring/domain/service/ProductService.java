package com.bithaus.apirestspring.domain.service;
import com.bithaus.apirestspring.domain.model.Product;
import com.bithaus.apirestspring.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService{
    private final ProductRepository repository;

    public Product create(Product product) {
        return repository.save(product);
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public Product update(Product p) {
        return repository.save(p);
    }

    @Transactional
    public void delete(UUID id) {
        // 1) Verificar que existe (si no, arroja IllegalArgumentException → 404)
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // 2) Borrarlo
        repository.deleteById(id);

    }
}
