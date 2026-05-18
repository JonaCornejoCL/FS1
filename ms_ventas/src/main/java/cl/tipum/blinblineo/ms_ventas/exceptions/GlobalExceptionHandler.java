package cl.tipum.blinblineo.ms_ventas.exceptions; 

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // errores genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("mensaje", "Ocurrió un error inesperado: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Atrapa los errores de OpenFeign (como la falta de stock), los limpia y los maquilla
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value()); // 400 Bad Request
        
        String mensajeLimpio = "Error en comunicación de servicios";
        
        try {
            String content = ex.contentUTF8();
            if (content != null && content.contains("mensaje\":\"")) {
                int inicio = content.indexOf("mensaje\":\"") + 10;
                int fin = content.indexOf("\"", inicio);
                mensajeLimpio = content.substring(inicio, fin);
            } else if (ex.getMessage().contains("Stock insuficiente")) {
                mensajeLimpio = ex.getMessage().substring(ex.getMessage().indexOf("Stock insuficiente"));
            } else {
                mensajeLimpio = ex.getMessage(); // <-- ¡Aquí estaba el error arreglado!
            }
        } catch (Exception e) {
            mensajeLimpio = "No se pudo procesar la operación en el inventario.";
        }

        error.put("mensaje", mensajeLimpio);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}