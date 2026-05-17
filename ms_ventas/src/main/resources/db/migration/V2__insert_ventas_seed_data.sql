TRUNCATE TABLE bd_ventas.detalle_venta RESTART IDENTITY CASCADE;
TRUNCATE TABLE bd_ventas.venta RESTART IDENTITY CASCADE;

-- (Aquí abajo dejas los INSERT INTO que ya tenías guardados...)

-- 10 ventas de prueba para Blinblineo
INSERT INTO bd_ventas.venta (id, folio_boleta, id_cliente, monto_total, fecha_venta, estado) VALUES
(1, 'BLIN-001', 101, 50000, '2026-05-10 10:30:00', 'COMPLETADO'),
(2, 'BLIN-002', 102, 35000, '2026-05-11 11:15:00', 'COMPLETADO'),
(3, 'BLIN-003', 103, 75000, '2026-05-12 14:20:00', 'PAGADA'),
(4, 'BLIN-004', 104, 25000, '2026-05-13 16:45:00', 'PENDIENTE'),
(5, 'BLIN-005', 105, 90000, '2026-05-14 09:10:00', 'COMPLETADO'),
(6, 'BLIN-006', 106, 45000, '2026-05-15 12:30:00', 'COMPLETADO'),
(7, 'BLIN-007', 107, 60000, '2026-05-15 15:00:00', 'PAGADA'),
(8, 'BLIN-008', 108, 20000, '2026-05-16 10:05:00', 'PENDIENTE'),
(9, 'BLIN-009', 109, 110000, '2026-05-16 17:50:00', 'COMPLETADO'),
(10, 'BLIN-010', 110, 35000, '2026-05-17 08:00:00', 'COMPLETADO');

-- detalles de venta asociados a las ventas (relacionadas por el venta_id)
INSERT INTO bd_ventas.detalle_venta (id_venta, sku_producto, cantidad, precio_unitario) VALUES
(1, 'POL-OVER-BLK', 2, 25000),
(2, 'POL-URBANA-BLANCA', 1, 35000),
(3, 'POL-URBANA-NEGRA', 2, 20000),
(3, 'GORRA-BLIN', 1, 35000),
(4, 'POL-OVER-BLK', 1, 25000),
(5, 'HOODIE-URBANO-GRIS', 2, 45000),
(6, 'POL-URBANA-BLANCA', 1, 35000),
(6, 'CALCETIN-BLIN', 1, 10000),
(7, 'POL-OVER-RED', 2, 30000),
(8, 'GORRA-BLIN-BASIC', 1, 20000),
(9, 'HOODIE-URBANO-GRIS', 2, 45000),
(9, 'POL-URBANA-NEGRA', 1, 20000),
(10, 'GORRA-BLIN', 1, 35000);

-- actualizar la secuencia de IDs para evitar que postman choque al crear la venta número 11
SELECT setval(pg_get_serial_sequence('bd_ventas.venta', 'id'), COALESCE(MAX(id), 1) + 1, false) FROM bd_ventas.venta;
SELECT setval(pg_get_serial_sequence('bd_ventas.detalle_venta', 'id'), COALESCE(MAX(id), 1) + 1, false) FROM bd_ventas.detalle_venta;