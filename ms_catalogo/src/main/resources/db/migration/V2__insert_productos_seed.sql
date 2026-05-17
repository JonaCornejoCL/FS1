-- limpieza preventiva (la técnica infalible para evitar conflictos con datos preexistentes)
TRUNCATE TABLE bd_catalogo.producto RESTART IDENTITY CASCADE;

-- insertar el catálogo oficial
INSERT INTO bd_catalogo.producto (sku, nombre, descripcion, precio, estado) VALUES
('POL-OVER-BLK', 'Polera Oversize Negra', 'Polera 100% algodón, corte oversize clásico de la marca.', 25000, 'ACTIVO'),
('POL-OVER-RED', 'Polera Oversize Roja', 'Edición limitada en color rojo, estilo urbano.', 30000, 'ACTIVO'),
('POL-URBANA-BLANCA', 'Polera Urbana Blanca', 'Diseño urbano minimalista, ideal para el día a día.', 35000, 'ACTIVO'),
('POL-URBANA-NEGRA', 'Polera Urbana Negra', 'El clásico infaltable con el logo frontal.', 20000, 'ACTIVO'),
('GORRA-BLIN', 'Gorra Premium', 'Gorra trucker con logo bordado en 3D.', 35000, 'ACTIVO'),
('GORRA-BLIN-BASIC', 'Gorra Básica', 'Gorra de algodón estilo dad hat.', 20000, 'ACTIVO'),
('HOODIE-URBANO-GRIS', 'Hoodie Gris Urbano', 'Polerón canguro grueso, perfecto para el invierno.', 45000, 'ACTIVO'),
('CALCETIN-BLIN', 'Calcetines Clásicos', 'Calcetines deportivos con detalles de la marca.', 10000, 'ACTIVO');