package cl.tipum.blinblineo.ms_catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.tipum.blinblineo.ms_catalogo.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Spring Boot crea la consulta SQL automáticamente solo con leer el nombre del método
    Producto findBySku(String sku);
}