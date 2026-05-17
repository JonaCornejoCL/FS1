-- limpieza preventiva
TRUNCATE TABLE bd_inventario.inventario RESTART IDENTITY CASCADE;

--poblado inicial
INSERT INTO bd_inventario.inventario (sku, cantidad) VALUES
('POL-OVER-BLK', 50),
('POL-OVER-RED', 20),
('POL-URBANA-BLANCA', 35),
('POL-URBANA-NEGRA', 40),
('GORRA-BLIN', 15),
('GORRA-BLIN-BASIC', 30),
('HOODIE-URBANO-GRIS', 10),
('CALCETIN-BLIN', 100);