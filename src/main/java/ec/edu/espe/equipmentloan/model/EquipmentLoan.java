package ec.edu.espe.equipmentloan.model;

import java.util.UUID;

/**
 * Entidad que representa un préstamo de equipo tecnológico
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public class EquipmentLoan {
    
    private final String id;
    private final String equipmentCode;
    private final String borrowerEmail;
    private final int loanDays;
    private LoanStatus status;

    /**
     * Constructor que inicializa un préstamo con estado CREATED
     * @param equipmentCode Código del equipo (ej: LAPTOP-ARICO)
     * @param borrowerEmail Correo del solicitante
     * @param loanDays Días de préstamo (1-15)
     */
    public EquipmentLoan(String equipmentCode, String borrowerEmail, int loanDays) {
        this.id = UUID.randomUUID().toString();
        this.equipmentCode = equipmentCode;
        this.borrowerEmail = borrowerEmail;
        this.loanDays = loanDays;
        this.status = LoanStatus.CREATED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public LoanStatus getStatus() {
        return status;
    }

    /**
     * Aprueba el préstamo cambiando su estado a APPROVED
     */
    public void approve() {
        this.status = LoanStatus.APPROVED;
    }

    /**
     * Enum que representa los estados posibles de un préstamo
     */
    public enum LoanStatus {
        CREATED,
        APPROVED
    }
}
