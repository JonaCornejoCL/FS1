package cl.tipum.blinblineo.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.tipum.blinblineo.ms_inventario.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    // método de Spring Data para buscar el stock de una prenda exacta
    Inventario findBySku(String sku);
}