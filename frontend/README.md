# Sistema de Gestión de Donaciones - Frontend Angular

Frontend desarrollado en Angular 17 para el sistema de gestión de donaciones.

## Requisitos

- Node.js 18+
- Angular CLI 17+

## Instalación

```bash
npm install
```

## Desarrollo

```bash
npm start
```

La aplicación estará disponible en `http://localhost:4200`

## Pantallas Implementadas

### Pantallas Públicas
1. **Home** (`/home`) - Pantalla inicial con formulario de donación
2. **Confirmación** (`/confirmacion`) - Confirmación de donación registrada
3. **Registro Beneficiario** (`/registro-beneficiario`) - Formulario de registro
4. **Login** (`/login`) - Inicio de sesión para beneficiarios y administradores

### Pantallas de Beneficiario (requieren autenticación)
5. **Búsqueda de Ayudas** (`/busqueda-ayudas`) - Listar y solicitar ayudas disponibles
6. **Mis Solicitudes** (`/mis-solicitudes`) - Ver estado de solicitudes

### Panel de Administrador (requiere autenticación)
7. **Panel Admin** (`/panel-admin`) - Dashboard y gestión completa

## Estructura del Proyecto

```
frontend/src/app/
├── components/           # Componentes de pantallas
│   ├── home/
│   ├── confirmacion/
│   ├── registro-beneficiario/
│   ├── login/
│   ├── busqueda-ayudas/
│   ├── mis-solicitudes/
│   └── panel-admin/
├── services/            # Servicios para API
│   ├── donacion.service.ts
│   ├── beneficiario.service.ts
│   └── asignacion.service.ts
├── models/              # Interfaces TypeScript
│   └── models.ts
├── guards/              # Guards de autenticación
├── interceptors/        # HTTP Interceptors
└── app-routing.module.ts
```

## API Backend

El frontend se conecta al backend Spring Boot en `http://localhost:8080`

Endpoints principales:
- `POST /api/donaciones` - Crear donación
- `POST /api/beneficiarios/registro` - Registrar beneficiario
- `GET /api/donaciones/disponibles` - Listar donaciones disponibles
- `POST /api/asignaciones` - Asignar donación a beneficiario

## Build para Producción

```bash
npm run build
```

Los archivos compilados estarán en `dist/`
