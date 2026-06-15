package cl.tipum.blinblineo.ms_catalogo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.tipum.blinblineo.ms_catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> { // <-- Volvimos a Long
    
    Optional<Producto> findBySku(String sku);
    
}