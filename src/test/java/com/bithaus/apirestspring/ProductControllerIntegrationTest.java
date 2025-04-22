package com.bithaus.apirestspring;

import com.bithaus.apirestspring.adapter.in.rest.dto.AuthRequestDto;
import com.bithaus.apirestspring.adapter.in.rest.dto.AuthResponseDto;
import com.bithaus.apirestspring.adapter.in.rest.dto.ProductRequestDto;
import com.bithaus.apirestspring.adapter.in.rest.dto.ProductResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(
        webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.jpa.hibernate.ddl-auto=update",
                "spring.security.jwt.secret=bulbasaureselmejorinicialynolopiensodiscutirconnadie"
        }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;
    private static UUID createdId;

    @BeforeEach
    void authenticate() {
        // 1) Logueo y obtengo JWT
        AuthRequestDto loginReq = new AuthRequestDto();
        loginReq.setUsername("user");
        loginReq.setPassword("password123");

        ResponseEntity<AuthResponseDto> loginResp = restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginReq,
                AuthResponseDto.class
        );
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        jwtToken = loginResp.getBody().getToken();
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    void createProduct_returnsCreatedDto() {
        ProductRequestDto req = ProductRequestDto.builder()
                .name("Mouse")
                .description("Óptico USB")
                .price(new BigDecimal("19990"))
                .stock(15)
                .build();

        // 2) Incluyo el token en el header
        HttpEntity<ProductRequestDto> entity = new HttpEntity<>(req, authHeaders());
        ResponseEntity<ProductResponseDto> resp = restTemplate.exchange(
                "/api/v1/products",
                HttpMethod.POST,
                entity,
                ProductResponseDto.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProductResponseDto body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Mouse");
        createdId = body.id();
    }

    @Test
    @Order(2)
    void getById_returnsSameProduct() {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<ProductResponseDto> resp = restTemplate.exchange(
                "/api/v1/products/" + createdId,
                HttpMethod.GET,
                entity,
                ProductResponseDto.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(createdId);
        assertThat(body.name()).isEqualTo("Mouse");
    }

    @Test
    @Order(3)
    void updateProduct_changesFields() {
        ProductRequestDto update = ProductRequestDto.builder()
                .name("Mouse Pro")
                .description("Óptico USB RGB")
                .price(new BigDecimal("24990"))
                .stock(10)
                .build();

        HttpEntity<ProductRequestDto> entity = new HttpEntity<>(update, authHeaders());
        ResponseEntity<ProductResponseDto> resp = restTemplate.exchange(
                "/api/v1/products/" + createdId,
                HttpMethod.PUT,
                entity,
                ProductResponseDto.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto body = resp.getBody();
        assertThat(body.name()).isEqualTo("Mouse Pro");
        assertThat(body.stock()).isEqualTo(10);
    }

    @Test
    @Order(4)
    void deleteProduct_returnsNoContent() {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/v1/products/" + createdId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Confirmo que ya no existe (404)
        ResponseEntity<Map> after = restTemplate.exchange(
                "/api/v1/products/" + createdId,
                HttpMethod.GET,
                entity,
                Map.class
        );
        assertThat(after.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
