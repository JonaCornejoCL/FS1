package cl.tipum.blinblineo.ms_ventas.exceptions;

public class RecursoYaExisteException extends RuntimeException {
    public RecursoYaExisteException(String mensaje) {
        super(mensaje);
    }
}