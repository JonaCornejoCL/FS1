package cl.tipum.blinblineo.ms_catalogo.service;

import java.util.List;

import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Se cambió Iterable a List para que el .stream() del controlador funcione sin problemas
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        log.info("Listando todos los productos del catálogo");
        return (List<Producto>) productoRepository.findAll();
    }

    // Se renombró a obtenerPorSku para mantener coherencia con el Controlador
    @Transactional(readOnly = true)
    public Producto obtenerPorSku(String sku) {
        log.info("Buscando producto con SKU: {}", sku);

        return productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con SKU: " + sku));
    }

    public Producto crear(Producto producto) {
        log.info("Intentando crear producto con SKU: {}", producto.getSku());

        // Manejo correcto asumiendo que findBySku retorna un Optional<Producto>
        if (productoRepository.findBySku(producto.getSku()).isPresent()) {
            log.warn("SKU duplicado detectado: {}", producto.getSku());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un producto con el SKU: " + producto.getSku());
        }

        return guardarSeguro(producto);
    }

    public Producto actualizar(String sku, Producto producto) {
        log.info("Actualizando producto SKU: {}", sku);

        Producto existente = obtenerPorSku(sku);

        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        // existente.setSku(producto.getSku()); // Omitido: No es buena práctica permitir cambiar la llave primaria/SKU

        return guardarSeguro(existente);
    }

    // Se cambió Long id por String sku
    public void eliminar(String sku) {
        log.info("Eliminando producto SKU: {}", sku);

        Producto producto = obtenerPorSku(sku);
        productoRepository.delete(producto);
    }

    private Producto guardarSeguro(Producto producto) {
        Producto guardado = java.util.Objects.requireNonNull(
                productoRepository.save(producto),
                "Error crítico: save() de JPA retornó null");

        log.info("Producto guardado exitosamente: {} - {}", guardado.getSku(), guardado.getNombre());
        return guardado;
    }
}