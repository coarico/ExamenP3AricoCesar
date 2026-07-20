package ec.edu.espe.equipmentloan.repository;

import ec.edu.espe.equipmentloan.model.EquipmentLoan;

/**
 * Interfaz del repositorio para gestionar préstamos de equipos
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public interface LoanRepository {
    
    /**
     * Guarda un préstamo en el repositorio
     * @param loan Préstamo a guardar
     * @return Préstamo guardado
     */
    EquipmentLoan save(EquipmentLoan loan);
    
    /**
     * Verifica si un equipo ya está prestado
     * @param equipmentCode Código del equipo
     * @return true si el equipo está prestado, false en caso contrario
     */
    boolean isEquipmentLoaned(String equipmentCode);
}
