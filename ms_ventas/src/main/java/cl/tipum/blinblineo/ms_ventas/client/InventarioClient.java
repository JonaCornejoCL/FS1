package cl.tipum.blinblineo.ms_ventas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-inventario", url = "http://localhost:8082/api/v1/inventario")
public interface InventarioClient {

    @PutMapping("/{sku}/reducir")
    InventarioResponse reducirStock(@PathVariable("sku") String sku, @RequestParam("cantidad") Integer cantidad);

    // clase para mapear el JSON que responde el inventario
    record InventarioResponse(String sku, Integer cantidad) {}
}
