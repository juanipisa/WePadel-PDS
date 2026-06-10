-- =============================================================================
-- WePadel-PDS — Inicialización de base de datos
-- =============================================================================
-- Requisitos: MySQL 8+, Java 17, Maven.
--
-- Pasos para clonar el repo y levantar desde cero:
--
--   1. Crear la base de datos (este script también lo hace al inicio):
--      mysql -u root -p < sql/init_wepadel_pds.sql
--      (Si las tablas aún no existen, el script fallará en el DELETE: es normal.)
--
--   2. Primera ejecución de Spring Boot (Hibernate crea tablas con ddl-auto=update):
--      export BD_PASSWORD=          # contraseña MySQL si aplica
--      mvn spring-boot:run
--      Detener con Ctrl+C cuando la app arranque.
--
--   3. Cargar datos de demostración (ejecutar de nuevo):
--      mysql -u root -p wepadel_pds < sql/init_wepadel_pds.sql
--
--   4. Ejecutar la aplicación:
--      mvn spring-boot:run
--
-- Usuarios demo (contraseña en texto plano, solo para desarrollo):
--   Admin:   admin@wepadel.com  / admin123
--   Cliente: cliente@wepadel.com / cliente123  (500 puntos, carrito con 1 ítem)
--
-- Catálogo: 4 categorías raíz (Paletas, Pelotas, Accesorios, Calzado) y subcategorías.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS wepadel_pds
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE wepadel_pds;

SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- Reinicialización idempotente (requiere tablas creadas por Hibernate)
DELETE FROM item_pedido WHERE id > 0;
DELETE FROM pedido WHERE id > 0;
DELETE FROM carrito_item WHERE id > 0;
DELETE FROM carrito WHERE id > 0;
DELETE FROM sistema_puntos WHERE id > 0;
DELETE FROM paleta WHERE id > 0;
DELETE FROM pelota WHERE id > 0;
DELETE FROM accesorio WHERE id > 0;
DELETE FROM calzado WHERE id > 0;
DELETE FROM producto WHERE id > 0;
DELETE FROM categoria WHERE id > 0;
DELETE FROM usuario WHERE id > 0;
DELETE FROM configuracion_sistema WHERE id > 0;

SET FOREIGN_KEY_CHECKS = 1;

-- -----------------------------------------------------------------------------
-- Configuración global
-- -----------------------------------------------------------------------------
INSERT INTO configuracion_sistema (costo_envio, canal_notificacion_default, pesos_por_punto_generado, conversion_puntos)
VALUES (1500.00, 'EMAIL', 10.00, 100);

-- -----------------------------------------------------------------------------
-- Categorías (4 raíces + subcategorías)
-- -----------------------------------------------------------------------------
INSERT INTO categoria (nombre, parent_id) VALUES ('Paletas', NULL);
SET @paletas = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Pelotas', NULL);
SET @pelotas = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Accesorios', NULL);
SET @accesorios = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Calzado', NULL);
SET @calzado = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Control', @paletas);
SET @pal_control = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Potencia', @paletas);
SET @pal_potencia = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Hibrida', @paletas);
SET @pal_hibrida = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Bolsos', @accesorios);
SET @acc_bolsos = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Overgrips', @accesorios);
SET @acc_overgrips = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Protectores', @accesorios);
SET @acc_protectores = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Hombre', @calzado);
SET @cal_hombre = LAST_INSERT_ID();

INSERT INTO categoria (nombre, parent_id) VALUES ('Mujer', @calzado);
SET @cal_mujer = LAST_INSERT_ID();

-- -----------------------------------------------------------------------------
-- Usuarios
-- -----------------------------------------------------------------------------
INSERT INTO usuario (nombre_apellido, mail, password, rol, canal_notificacion, fecha_creacion)
VALUES ('Admin Sistema', 'admin@wepadel.com', 'admin123', 'ADMINISTRADOR', NULL, NOW());

INSERT INTO usuario (nombre_apellido, mail, password, rol, canal_notificacion, fecha_creacion)
VALUES ('Juan Cliente', 'cliente@wepadel.com', 'cliente123', 'CLIENTE', 'EMAIL', NOW());
SET @cliente_id = LAST_INSERT_ID();

INSERT INTO carrito (usuario_id, ultima_modificacion)
VALUES (@cliente_id, NOW());
SET @carrito_id = LAST_INSERT_ID();

INSERT INTO sistema_puntos (usuario_id, cantidad, conversion)
VALUES (@cliente_id, 500, 100);

-- -----------------------------------------------------------------------------
-- Paletas
-- -----------------------------------------------------------------------------
INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PALETA', 'Bullpadel Vertex 04 Comfort', 'Paleta de control, balance medio, fibra de carbono', 89900.00, 15, 1, @pal_control);
SET @id = LAST_INSERT_ID();
INSERT INTO paleta (id, peso_gramos, balance, forma, material)
VALUES (@id, 365, 'Medio', 'Redonda', 'Fibra de carbono');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PALETA', 'Nox AT10 Genius 18K', 'Paleta de control para jugadores avanzados', 75000.00, 8, 1, @pal_control);
SET @id = LAST_INSERT_ID();
INSERT INTO paleta (id, peso_gramos, balance, forma, material)
VALUES (@id, 355, 'Bajo', 'Lágrima', 'Carbono 18K');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PALETA', 'Siux Trilogy 3', 'Paleta de potencia, salida explosiva', 105000.00, 6, 1, @pal_potencia);
SET @id = LAST_INSERT_ID();
INSERT INTO paleta (id, peso_gramos, balance, forma, material)
VALUES (@id, 370, 'Alto', 'Diamante', 'Fibra de vidrio y carbono');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PALETA', 'Head Delta Pro', 'Paleta híbrida versátil para todos los niveles', 92000.00, 10, 1, @pal_hibrida);
SET @id = LAST_INSERT_ID();
INSERT INTO paleta (id, peso_gramos, balance, forma, material)
VALUES (@id, 360, 'Medio-Alto', 'Híbrida', 'Graphene');

-- -----------------------------------------------------------------------------
-- Pelotas
-- -----------------------------------------------------------------------------
INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PELOTA', 'Head Pro S', 'Pelotas oficiales de competición, tubo x3', 12000.00, 50, 1, @pelotas);
SET @id = LAST_INSERT_ID();
INSERT INTO pelota (id, presion, unidades_por_tubo)
VALUES (@id, 'Alta', 3);

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('PELOTA', 'Bullpadel Premium', 'Pelotas de entrenamiento, tubo x4', 9500.00, 80, 1, @pelotas);
SET @id = LAST_INSERT_ID();
INSERT INTO pelota (id, presion, unidades_por_tubo)
VALUES (@id, 'Media', 4);

-- -----------------------------------------------------------------------------
-- Accesorios
-- -----------------------------------------------------------------------------
INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('ACCESORIO', 'Overgrip Wilson Pro', 'Pack x3 overgrips antideslizantes', 4500.00, 120, 1, @acc_overgrips);
SET @id = LAST_INSERT_ID();
INSERT INTO accesorio (id, tipo, material)
VALUES (@id, 'Overgrip', 'Poliuretano');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('ACCESORIO', 'Protector Bullpadel', 'Protector de marco transparente', 8900.00, 60, 1, @acc_protectores);
SET @id = LAST_INSERT_ID();
INSERT INTO accesorio (id, tipo, material)
VALUES (@id, 'Protector', 'Silicona');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('ACCESORIO', 'Mochila Nox Pro', 'Mochila padel con compartimento térmico', 45999.00, 25, 1, @acc_bolsos);
SET @id = LAST_INSERT_ID();
INSERT INTO accesorio (id, tipo, material)
VALUES (@id, 'Mochila', 'Nylon');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('ACCESORIO', 'Bolso Bullpadel Tour', 'Bolso racket bag 3 paletas', 38999.00, 18, 1, @acc_bolsos);
SET @id = LAST_INSERT_ID();
INSERT INTO accesorio (id, tipo, material)
VALUES (@id, 'Bolso', 'Poliéster');

-- -----------------------------------------------------------------------------
-- Calzado
-- -----------------------------------------------------------------------------
INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('CALZADO', 'Bullpadel Hybrid Fly 24', 'Zapatilla de padel amortiguación alta', 119999.00, 20, 1, @cal_hombre);
SET @id = LAST_INSERT_ID();
INSERT INTO calzado (id, talle, color, genero)
VALUES (@id, 42, 'Blanco/Azul', 'Hombre');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('CALZADO', 'Adidas Courtjam Control', 'Zapatilla indoor padel', 99999.00, 18, 1, @cal_hombre);
SET @id = LAST_INSERT_ID();
INSERT INTO calzado (id, talle, color, genero)
VALUES (@id, 40, 'Negro', 'Hombre');

INSERT INTO producto (dtype, nombre, descripcion, precio, stock, habilitado, categoria_id)
VALUES ('CALZADO', 'Asics Gel Padel Pro', 'Zapatilla mujer padel', 109999.00, 15, 1, @cal_mujer);
SET @id = LAST_INSERT_ID();
INSERT INTO calzado (id, talle, color, genero)
VALUES (@id, 38, 'Rosa', 'Mujer');

-- -----------------------------------------------------------------------------
-- Carrito demo del cliente (1 paleta)
-- -----------------------------------------------------------------------------
SELECT id INTO @prod_carrito FROM producto WHERE nombre = 'Bullpadel Vertex 04 Comfort' LIMIT 1;

INSERT INTO carrito_item (carrito_id, producto_id, cantidad)
VALUES (@carrito_id, @prod_carrito, 1);

-- -----------------------------------------------------------------------------
-- Pedido de ejemplo en historial del cliente
-- -----------------------------------------------------------------------------
INSERT INTO pedido (cliente_id, total, costo_envio, descuento_por_puntos, fecha_compra, estado, tipo_pago,
                    codigo_transaccion, usa_puntos, puntos_usados, puntos_generados)
VALUES (@cliente_id, 13500.00, 1500.00, 0.00, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENTREGADO', 'TARJETA',
        'TX-DEMO-001', 0, 0, 120);
SET @pedido_id = LAST_INSERT_ID();

SELECT id INTO @prod_pedido FROM producto WHERE nombre = 'Head Pro S' LIMIT 1;

INSERT INTO item_pedido (pedido_id, producto_id, cantidad, precio_unitario_historico, descripcion_producto)
VALUES (@pedido_id, @prod_pedido, 1, 12000.00, 'Head Pro S');

SET SQL_SAFE_UPDATES = 1;

-- Verificación rápida
SELECT 'Categorías' AS seccion, COUNT(*) AS total FROM categoria
UNION ALL SELECT 'Productos', COUNT(*) FROM producto
UNION ALL SELECT 'Usuarios', COUNT(*) FROM usuario;
