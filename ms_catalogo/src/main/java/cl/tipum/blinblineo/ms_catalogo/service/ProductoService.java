package cl.tipum.blinblineo.ms_catalogo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // 1. obtiene la vitrina (Para mostrar en la página principal)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. busca un producto específico por su SKU (Esto lo usará Feign para mostrar el detalle del producto)
    public Producto obtenerPorSku(String sku) {
        Producto producto = productoRepository.findBySku(sku);
        
        // pqueña validación de seguridad para evitar que se retorne un producto nulo
        if (producto == null) {
            throw new IllegalArgumentException("No se encontró ningún producto con el SKU: " + sku);
        }
        
        return producto;
    }
}