package cl.tipum.blinblineo.ms_inventario.exception;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import cl.tipum.blinblineo.ms_inventario.dto.ErrorDTO;
 
import java.util.HashMap;
import java.util.Map;
 
/**
 * CORRECCIONES APLICADAS:
 * - Manejo de IllegalStateException para stock insuficiente (409 Conflict)
 * - Manejo de excepciones de validación Bean Validation (400 Bad Request)
 * - Logs de errores para trazabilidad
 * - Cobertura completa de excepciones (IE 2.3.1)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
 
    /**
     * Maneja errores cuando un recurso no existe (404)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorDTO error = ErrorDTO.builder()
                .mensaje(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
                
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
 
    /**
     * Maneja errores de estado ilegal (stock insuficiente, etc.) - 409 Conflict
     * CORRECCIÓN: Ahora captura IllegalStateException que lanza reducirStock
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException ex) {
        log.error("Error de estado: {}", ex.getMessage());
        
        ErrorDTO error = ErrorDTO.builder()
                .mensaje(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();
                
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
 
    /**
     * Maneja errores de validación Bean Validation (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });
        
        log.warn("Errores de validación: {}", errores);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Errores de validación en la solicitud");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errores", errores);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
 
    /**
     * Maneja cualquier otra excepción no contemplada (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: ", ex);
        
        ErrorDTO error = ErrorDTO.builder()
                .mensaje("Error interno del servidor: " + ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
                
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}