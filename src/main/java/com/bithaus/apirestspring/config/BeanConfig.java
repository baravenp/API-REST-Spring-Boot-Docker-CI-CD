package com.bithaus.apirestspring.config;

import com.bithaus.apirestspring.adapter.out.jpa.ProductJpaAdapter;
import com.bithaus.apirestspring.domain.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    ProductService productService(ProductJpaAdapter adapter) {
        return new ProductService(adapter);
    }
}
