# Gestor de Pólizas – API REST (Módulo 2)

API REST para la gestión de pólizas de arrendamiento de inmuebles (Individuales y Colectivas), construida con **Java 17 + Spring Boot 3 + PostgreSQL + Maven**.

---

## Requisitos Previos

- Java 17 (JDK 17)
- Maven 3.8+
- PostgreSQL (base de datos creada para la aplicación)
- Postman (para probar los endpoints)

---

## Configuración de Base de Datos

Crear en PostgreSQL una base de datos llamada `gestor_polizas`:

```sql
CREATE DATABASE gestor_polizas;
```

Configurar las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestor_polizas
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Cómo Ejecutar

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

O con Maven instalado:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## Seguridad

Todos los endpoints requieren el header obligatorio:

```
x-api-key: 123456
```

Si falta o es incorrecto, la respuesta será `401 Unauthorized`.

---

## Endpoints Disponibles

### 1. Crear una póliza
```
POST /api/v1/polizas
Content-Type: application/json
x-api-key: 123456

{
  "tipo": "COLECTIVA",
  "fechaInicio": "2026-01-01",
  "fechaFin": "2026-12-31",
  "canonMensual": 1000000,
  "mesesVigencia": 12,
  "tomadorId": 101,
  "beneficiarioId": 201
}
```
- El estado inicial es `ACTIVA`.
- La prima se calcula automáticamente: `canon mensual × meses de vigencia`.

### 2. Listar pólizas por tipo y estado
```
GET /api/v1/polizas?tipo=INDIVIDUAL&estado=ACTIVA
```
Parámetros opcionales: `tipo` (INDIVIDUAL / COLECTIVA), `estado` (ACTIVA / CANCELADA / RENOVADA).

### 3. Obtener una póliza por ID
```
GET /api/v1/polizas/{id}
```

### 4. Listar riesgos de una póliza
```
GET /api/v1/polizas/{id}/riesgos
```

### 5. Renovar póliza (+IPC, estado RENOVADA)
```
POST /api/v1/polizas/{id}/renovar
x-api-key: 123456
```
- Incrementa canon y prima según el % de IPC configurado (por defecto 5%).
- El estado pasa a `RENOVADA`.
- **No se puede renovar una póliza cancelada.**

### 6. Cancelar póliza (y todos sus riesgos)
```
POST /api/v1/polizas/{id}/cancelar
x-api-key: 123456
```
- Cambia el estado de la póliza a `CANCELADA`.
- Cancela todos los riesgos asociados.

### 7. Agregar riesgo a una póliza (solo Colectiva)
```
POST /api/v1/polizas/{id}/riesgos
Content-Type: application/json
x-api-key: 123456

{
  "descripcion": "Daños por agua en inmueble arrendado",
  "montoAsegurado": 5000000
}
```
- **Solo si tipo = COLECTIVA.**
- Una póliza Individual solo puede tener 1 riesgo.

### 8. Cancelar un riesgo
```
POST /api/v1/riesgos/{id}/cancelar
x-api-key: 123456
```

### 9. Mock CORE – Intento de envío al CORE legado
```
POST /api/v1/core-mock/evento
Content-Type: application/json
x-api-key: 123456

{
  "evento": "ACTUALIZACION",
  "polizaId": 555
}
```
Su único propósito: registrar en logs que la operación se intentó enviar al CORE.

---

## Ejemplos para Postman

### Crear una póliza Colectiva
```
POST /api/v1/polizas
Content-Type: application/json
x-api-key: 123456

{
  "tipo": "COLECTIVA",
  "fechaInicio": "2026-01-01",
  "fechaFin": "2026-12-31",
  "canonMensual": 1000000,
  "mesesVigencia": 12,
  "tomadorId": 101,
  "beneficiarioId": 201
}
```

### Renovar póliza
```
POST /api/v1/polizas/1/renovar
x-api-key: 123456
```

### Agregar riesgo a póliza colectiva
```
POST /api/v1/polizas/1/riesgos
Content-Type: application/json
x-api-key: 123456

{
  "descripcion": "Incendio en inmueble",
  "montoAsegurado": 8000000
}
```

---

## Reglas de Negocio Implementadas

1. Una póliza **Individual** solo puede tener **1 riesgo**.
2. No se puede renovar una póliza **cancelada**.
3. Cancelar una póliza cancela **todos sus riesgos** asociados.
4. Agregar riesgo exige que el tipo de póliza sea **Colectiva**.

---

## Supuestos

- El CORE legado se simula mediante `/api/v1/core-mock/evento`, el cual solo registra en logs que la operación se intentó enviar.
- Las notificaciones (correo/SMS) se implementan como eventos registrados en logs.
- El porcentaje de IPC es configurable vía propiedad `poliza.ipc.porcentaje` (por defecto 5.0).
- El header de seguridad `x-api-key: 123456` se valida en un filtro que intercepta todas las peticiones bajo `/api/v1/*`.