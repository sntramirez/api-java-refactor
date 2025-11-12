# API Java Refactor

Proyecto Spring Boot para refactorización de API en Java.

## Requisitos

- Java 17 o superior
- Maven 3.6+

## Instalación y Ejecución

### Compilar el proyecto

```bash
mvn clean install
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints Disponibles

### Health Check
```
GET /api/health
```

### Hello
```
GET /api/hello?name=TuNombre
```

## Base de Datos

El proyecto está configurado con H2 Database (en memoria) para desarrollo.

- Console H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Password: (vacío)

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/api/refactor/
│   │       ├── ApiRefactorApplication.java
│   │       └── controller/
│   │           └── HelloController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/api/refactor/
```

## Tecnologías

- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Maven
