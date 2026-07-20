package ec.edu.espe.equipmentloan.service;

import ec.edu.espe.equipmentloan.dto.LoanResponse;
import ec.edu.espe.equipmentloan.exception.InvalidLoanException;
import ec.edu.espe.equipmentloan.model.EquipmentLoan;
import ec.edu.espe.equipmentloan.repository.LoanRepository;

import java.util.UUID;

/**
 * Servicio principal para gestionar préstamos de equipos
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final EquipmentPolicyClient policyClient;

    /**
     * Constructor con inyección de dependencias
     * @param loanRepository Repositorio de préstamos
     * @param policyClient Cliente de políticas institucionales
     */
    public LoanService(LoanRepository loanRepository, EquipmentPolicyClient policyClient) {
        this.loanRepository = loanRepository;
        this.policyClient = policyClient;
    }

    /**
     * Crea un nuevo préstamo de equipo validando reglas de negocio
     * @param equipmentCode Código del equipo (ej: LAPTOP-ARICO)
     * @param borrowerEmail Correo del solicitante
     * @param loanDays Días de préstamo (1-15)
     * @return Respuesta con ID del préstamo y código de aprobación
     * @throws InvalidLoanException Si alguna validación falla
     */
    public LoanResponse createLoan(String equipmentCode, String borrowerEmail, int loanDays) {
        // Validación 1: El código del equipo no puede ser nulo ni vacío
        if (equipmentCode == null || equipmentCode.trim().isEmpty()) {
            throw new InvalidLoanException("El código del equipo no puede ser nulo ni vacío");
        }

        // Validación 2: El correo electrónico debe tener un formato válido
        if (borrowerEmail == null || !borrowerEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidLoanException("El correo electrónico debe tener un formato válido");
        }

        // Validación 3: loanDays debe estar entre 1 y 15 días
        if (loanDays < 1 || loanDays > 15) {
            throw new InvalidLoanException("Los días de préstamo deben estar entre 1 y 15");
        }

        // Regla de negocio 1: No se permitirá prestar equipos a usuarios bloqueados
        if (policyClient.isUserBlocked(borrowerEmail)) {
            throw new InvalidLoanException("Usuario bloqueado por políticas institucionales");
        }

        // Regla de negocio 2: No se podrá aprobar un préstamo si el equipo ya se encuentra prestado
        if (loanRepository.isEquipmentLoaned(equipmentCode)) {
            throw new InvalidLoanException("El equipo ya se encuentra prestado");
        }

        // Crear el préstamo
        EquipmentLoan loan = new EquipmentLoan(equipmentCode, borrowerEmail, loanDays);
        
        // Aprobar el préstamo
        loan.approve();
        
        // Guardar en el repositorio
        EquipmentLoan savedLoan = loanRepository.save(loan);
        
        // Generar código de aprobación único
        String approvalCode = "APPR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Retornar respuesta
        return new LoanResponse(savedLoan.getId(), approvalCode);
    }
}
