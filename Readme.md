# Gestión de Productos - API REST Spring Boot

Este repositorio contiene una **API RESTful** para gestionar productos, construida desde cero con las mejores prácticas de calidad de software, principios SOLID, y un pipeline completo de integración y despliegue continuo (CI/CD).

---

## 🚀 Tecnologías Utilizadas

- **Java** 21
- **Spring Boot** 3.4.4
- **PostgreSQL** 15
- **Hibernate ORM** 6.6
- **Testcontainers** para pruebas de integración
- **Docker** & **Docker Compose** para contenerización
- **GitHub Actions** para CI/CD
- **Maven** para gestión de dependencias y construcción

---

## 🛠️ Requisitos Previos

- JDK 21 instalado
- Docker & Docker Compose
- Maven (opcional, el contenedor Docker ya incluye Maven)

---

## ⚙️ Configuración del Proyecto

1. **Clona** el repositorio:
   ```bash
   git clone git@github.com:baravenp/API-REST-Spring-Boot-Docker-CI-CD.git
   cd API-REST-Spring-Boot-Docker-CI-CD
   ```

2. **Variables de Entorno** (opcional):
   Configuradas por defecto en `src/main/resources/application.yml`. Puedes sobreescribir con Docker Compose o env vars:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://db:5432/productosdb
       username: postgres
       password: postgres
     jpa:
       hibernate:
         ddl-auto: update
     security:
       jwt:
         secret: bulbasaureselmejorinicialynolopiensodiscutirconnadie
         expiration-ms: 3600000
   ```

---

## ▶️ Ejecución Local

```bash
# Levantar base de datos externa (PostgreSQL local en 5432)
# o contenedor Docker

# Ejecutar la aplicación con Maven:
./mvnw spring-boot:run

# La API estará en http://localhost:8080/api/v1/
```

---

## 🐳 Docker & Docker Compose

```bash
# Construir y levantar los contenedores
docker-compose up --build

# Servicios:
# - db: PostgreSQL en el puerto 5432
# - app: Spring Boot expuesto en el puerto 8080
```

---

## 📄 API Endpoints

### Autenticación

| Método | Ruta                 | Descripción              |
|--------|----------------------|--------------------------|
| POST   | `/api/v1/auth/login` | Obtiene JWT (user/password) |

### Productos

| Método  | Ruta                       | Descripción                 |
|---------|----------------------------|-----------------------------|
| POST    | `/api/v1/products`         | Crea un nuevo producto      |
| GET     | `/api/v1/products`         | Lista todos los productos   |
| GET     | `/api/v1/products/{id}`    | Obtiene producto por ID     |
| PUT     | `/api/v1/products/{id}`    | Actualiza producto por ID   |
| DELETE  | `/api/v1/products/{id}`    | Elimina producto por ID     |

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

---

## 📦 CI/CD con GitHub Actions

Cada push a `main` o Pull Request dispara el workflow:

1. **Compila** y **ejecuta tests** (JUnit + Testcontainers).
2. **Construye** la imagen Docker con JDK 21.
3. **Taggea** con `latest` y el SHA del commit.
4. **Pushea** al registry (Docker Hub o GHCR).

Archivo: `.github/workflows/ci-cd.yml`

---

## 📁 Estructura del Proyecto

```plaintext
├── src
│   ├── main
│   │   ├── java/com/bithaus/apirestspring
│   │   │   ├── config          # Configuraciones (Security, JWT, Swagger)
│   │   │   ├── adapter
│   │   │   │   ├── in/rest     # Controllers & DTOs
│   │   │   │   └── out/jpa     # Entidades, repositorios y mapeadores
│   │   │   └── domain         # Modelos, puertos y casos de uso
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java              # Pruebas unitarias e integración
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── .github/workflows/ci-cd.yml
```

---

## 🙋‍♂️ Autor

- **Benjamín Aravena** (`@baravenp`)

---

## ⚖️ Licencia

Este proyecto está bajo la **Licencia Apache 2.0**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

