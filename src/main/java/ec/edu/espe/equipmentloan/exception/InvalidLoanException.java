package ec.edu.espe.equipmentloan.exception;

/**
 * Excepción personalizada para validaciones de préstamos
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public class InvalidLoanException extends RuntimeException {
    
    public InvalidLoanException(String message) {
        super(message);
    }
}
