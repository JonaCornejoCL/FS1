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

    // 1. Obtiene la vitrina (Para mostrar en la página principal)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. Busca un producto específico por su SKU (Esto lo usará Feign para mostrar el detalle del producto)
    public Producto obtenerPorSku(String sku) {
        Producto producto = productoRepository.findBySku(sku);
        
        // Pequeña validación de seguridad para evitar que se retorne un producto nulo
        if (producto == null) {
            throw new IllegalArgumentException("No se encontró ningún producto con el SKU: " + sku);
        }
        
        return producto;
    }

    // 3. Guarda un nuevo producto en la base de datos (¡Para destrabar el POST!)
    public Producto guardar(Producto producto) {
        // Validación extra: Evita duplicar un SKU si ya existe en la vitrina
        Producto existente = productoRepository.findBySku(producto.getSku());
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un producto registrado con el SKU: " + producto.getSku());
        }
        return productoRepository.save(producto);
    }

    // 4. Actualiza los datos de un producto existente por su SKU
    public Producto actualizar(String sku, Producto productoActualizado) {
        // Reutiliza tu validación: si no existe, lanza la excepción de inmediato
        Producto productoExistente = obtenerPorSku(sku);
        
        // Actualiza los datos con lo que viene del JSON de Postman
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setEstado(productoActualizado.getEstado());
        
        return productoRepository.save(productoExistente);
    }

    // 5. Elimina un producto físico de la base de datos por su SKU
    public void eliminar(String sku) {
        // Si no existe, pincha antes de intentar borrar
        Producto producto = obtenerPorSku(sku);
        productoRepository.delete(producto);
    }
}