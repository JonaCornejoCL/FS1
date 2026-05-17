package cl.tipum.blinblineo.ms_catalogo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.tipum.blinblineo.ms_catalogo.dto.ErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // método que funciona con la excepción en el ProductoService
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        
        ErrorDTO error = ErrorDTO.builder()
                .mensaje(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
                
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}