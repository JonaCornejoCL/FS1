package cl.tipum.blinblineo.ms_ventas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// como ms_ventas no tiene el DTO del producto se crea una clase interna solo para recibir los datos que importan del catálogo (sku, nombre, precio, estado)
@FeignClient(name = "ms-catalogo", url = "http://localhost:8081/api/v1/productos")
public interface CatalogoClient {

    @GetMapping("/{sku}")
    ProductoResponse obtenerProductoPorSku(@PathVariable("sku") String sku);

    // Clase para mapear el JSON que responde el catálogo
    record ProductoResponse(String sku, String nombre, Integer precio, String estado) {}
}