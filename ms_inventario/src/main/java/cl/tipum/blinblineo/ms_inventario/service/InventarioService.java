package cl.tipum.blinblineo.ms_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    // 1. Ver todo el stock
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    // 2. Buscar stock de un SKU específico
    public Inventario obtenerPorSku(String sku) {
        Inventario inventario = inventarioRepository.findBySku(sku);
        if (inventario == null) {
            throw new IllegalArgumentException("No hay registro de inventario para el SKU: " + sku);
        }
        return inventario;
    }

    // 3. Inicializar stock de una prenda nueva
    public Inventario inicializarStock(Inventario inventario) {
        // Validación de seguridad para no duplicar filas del mismo producto
        Inventario existente = inventarioRepository.findBySku(inventario.getSku());
        if (existente != null) {
            throw new IllegalArgumentException("El SKU " + inventario.getSku() + " ya existe en bodega.");
        }
        return inventarioRepository.save(inventario);
    }

    // 4. Restar stock cuando hay una venta
    public Inventario reducirStock(String sku, Integer cantidadComprada) {
        // Reutiliza tu propia validación
        Inventario inventario = obtenerPorSku(sku);
        
        // Verifica que haya suficiente ropa en bodega
        if (inventario.getCantidad() < cantidadComprada) {
            throw new IllegalStateException("Stock insuficiente. Solo quedan " + inventario.getCantidad() + " unidades del SKU: " + sku);
        }
        
        inventario.setCantidad(inventario.getCantidad() - cantidadComprada);
        return inventarioRepository.save(inventario);
    }
}