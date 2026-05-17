package cl.tipum.blinblineo.ms_ventas.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.tipum.blinblineo.ms_ventas.dto.ErrorDTO;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    // 1. Excepción del profe cuando no se encuentra algo (Devuelve ErrorDTO)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorDTO> handleRecursoNoEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    // 2. Excepción del profe cuando algo ya existe (Devuelve ErrorDTO)
    @ExceptionHandler(RecursoYaExisteException.class)
    public ResponseEntity<ErrorDTO> handleRecursoYaExiste(RecursoYaExisteException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    // 3. Excepción del profe caídas generales (Devuelve ErrorDTO)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExcepcionGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado: " + e.getMessage()));
    }

    // 4. validación de DTOs para mantener los errores limpios en JSON
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });
        
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }
}