package com.bithaus.apirestspring.adapter.out.jpa.repository;
import com.bithaus.apirestspring.adapter.out.jpa.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID>{}
