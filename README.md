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
- **Docker y Docker Compose**
- **Node.js y Angular CLI**
- **Java 17+ y Maven**
- **SQL Server Management Studio (SSMS) o Azure Data Studio**

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

---

## Documentación Adicional
- **API Docs:** Swagger disponible en `http://localhost:8080/swagger-ui.html`
- **Colección de Postman:** Incluida en la carpeta `docs/postman`

---

## Contribuciones
Cualquier contribución es bienvenida. Para contribuir:
1. Haz un fork del repositorio.
2. Crea una rama (`git checkout -b feature-nueva`).
3. Realiza tus cambios y haz un commit (`git commit -m 'Nueva funcionalidad'`).
4. Sube tu rama (`git push origin feature-nueva`).
5. Abre un Pull Request.

---

## Licencia
Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

