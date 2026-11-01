# MÓDULO 3 – Conocimientos en BBDD

## Escenario

Optimización de Base de Datos (SQL / PL/SQL) Tienes una tabla `orders` con 10 millones de registros y una tabla `customers` con 500,000 registros. Necesitas ejecutar frecuentemente la siguiente consulta para encontrar los pedidos de clientes de 'México', pero es muy lenta.

```sql
SELECT 
o.order_id, 
o.order_date, 
c.customer_name, 
o.total_amount 
FROM 
orders o 
JOIN 
customers c ON o.customer_id = c.customer_id 
WHERE 
c.country = 'México';
```

Describe al menos tres estrategias diferentes que implementarías para optimizar el rendimiento de esta consulta.

---

## Respuesta

### Estrategia 1: Índice en `customers.country`

Crear un índice en la columna `country` de la tabla `customers` para filtrar rápidamente los clientes de México:

```sql
CREATE INDEX idx_customers_country ON customers(country);
```

Si la consulta también usa `customer_id` para el JOIN, un índice compuesto sería más eficiente:

```sql
CREATE INDEX idx_customers_country_id ON customers(country, customer_id);
```

### Estrategia 2: Índice en `orders.customer_id`

Crear un índice en la columna foránea de `orders` para acelerar el JOIN:

```sql
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
```

### Estrategia 3: Estadísticas actualizadas

Ejecutar `ANALYZE` en ambas tablas para que el optimizador de PostgreSQL elija el mejor plan de ejecución:

```sql
ANALYZE customers;
ANALYZE orders;
```