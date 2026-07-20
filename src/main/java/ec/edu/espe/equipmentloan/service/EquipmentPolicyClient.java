package ec.edu.espe.equipmentloan.service;

/**
 * Cliente para consultar políticas institucionales de préstamo
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public interface EquipmentPolicyClient {
    
    /**
     * Verifica si un usuario está bloqueado por políticas institucionales
     * @param borrowerEmail Correo del usuario
     * @return true si el usuario está bloqueado, false en caso contrario
     */
    boolean isUserBlocked(String borrowerEmail);
}
