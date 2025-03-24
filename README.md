# Postea

## Descripción
**Postea** es una aplicación Full Stack desarrollada con **Spring Boot 3.2.6** y **Angular 19**, que permite la gestión de usuarios y publicaciones. Utiliza **Hibernate (JPA)** para la persistencia en **SQL Server** y sigue principios de **arquitectura limpia**.

---

## Características Principales
### Backend (Spring Boot 3.2.6)
- **Autenticación con JWT** (Login, Registro, Renovación de Token, Spring Security).
- **CRUD de Usuarios** con roles `ADMIN` y `USER`.
- **CRUD de Posts**, con permisos según roles.
- **Conexión a SQL Server con Hibernate (JPA)**.
- **Migraciones con Flyway o Liquibase**.
- **Arquitectura en capas (Controller, Service, Repository)**.
- **Uso de DTOs** para una mejor separación de datos.
- **Pruebas Unitarias** con JUnit y Mockito.

### Frontend (Angular 19)
- **Autenticación con JWT** (Login, Registro, Gestor de Token).
- **Panel de Administración** para usuarios con rol `ADMIN`.
- **Sección Pública** para listar posts con paginación.
- **Lazy Loading y modularización** con `AdminModule`, `PublicModule`, `AuthModule`.
- **Gestor de Estado con NgRx o Signals**.
- **Pruebas Unitarias** con Jasmine/Karma.

---

## Requisitos Previos
Antes de iniciar, asegúrate de tener instalado:
- **Git**
- **Docker & Docker Compose & Docker CLI**
- **Node.js y Angular CLI** (En caso de incopatibilidad usar las versiones Angular CLI: 19.2.4 Node: 22.13.1)
- **MSSQL-CLI**

---

## Instalación y Ejecución
### 1. Clonar el Proyecto
```sh
git clone https://github.com/Joel-x-x/prueba-tecnica-shenzhen.git
cd prueba-tecnica-shenzhen
```

### 2. Levantar la Base de Datos
```sh
docker-compose up -d sqlserver
```
Una vez que la base de datos esté lista, abre la terminal de SQL Server y ejecuta:
```sql
CREATE DATABASE shenzhen;
GO
```

### 3. Ejecutar el Backend
```sh
docker-compose up -d back
```

### 4. Ejecutar el Frontend
```sh
cd front
ng serve -o
```
### 5. Cuenta Admin
#### Tanto para correo como para password
```sh
admin@gmail.com
```

---

## Documentación Adicional
- **API Docs:** Swagger disponible en `http://localhost:8080/swagger-ui.html`
- **Colección de Postman:** Incluida en la carpeta `docs/postman`

