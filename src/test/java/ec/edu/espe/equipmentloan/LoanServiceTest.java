package ec.edu.espe.equipmentloan;

import ec.edu.espe.equipmentloan.dto.LoanResponse;
import ec.edu.espe.equipmentloan.exception.InvalidLoanException;
import ec.edu.espe.equipmentloan.model.EquipmentLoan;
import ec.edu.espe.equipmentloan.repository.LoanRepository;
import ec.edu.espe.equipmentloan.service.EquipmentPolicyClient;
import ec.edu.espe.equipmentloan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio de préstamos
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public class LoanServiceTest {

    private LoanRepository loanRepository;
    private EquipmentPolicyClient policyClient;
    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        loanRepository = Mockito.mock(LoanRepository.class);
        policyClient = Mockito.mock(EquipmentPolicyClient.class);
        loanService = new LoanService(loanRepository, policyClient);
    }

    /**
     * Caso 1: Creación exitosa de un préstamo
     * Validar: respuesta obtenida, código de aprobación, almacenamiento del préstamo
     */
    @Test
    void createLoan_validData_shouldCreateSuccessfully() {
        // Arrange
        String equipmentCode = "LAPTOP-ARICO";
        String borrowerEmail = "coarico@espe.edu.ec";
        int loanDays = 7;

        when(policyClient.isUserBlocked(borrowerEmail)).thenReturn(false);
        when(loanRepository.isEquipmentLoaned(equipmentCode)).thenReturn(false);
        when(loanRepository.save(any(EquipmentLoan.class))).thenAnswer(i -> i.getArguments()[0]);

        ArgumentCaptor<EquipmentLoan> captor = ArgumentCaptor.forClass(EquipmentLoan.class);

        // Act
        LoanResponse response = loanService.createLoan(equipmentCode, borrowerEmail, loanDays);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getLoanId());
        assertNotNull(response.getApprovalCode());
        assertTrue(response.getApprovalCode().startsWith("APPR-"));

        verify(loanRepository).save(captor.capture());
        EquipmentLoan savedLoan = captor.getValue();
        assertEquals(equipmentCode, savedLoan.getEquipmentCode());
        assertEquals(borrowerEmail, savedLoan.getBorrowerEmail());
        assertEquals(loanDays, savedLoan.getLoanDays());
        assertEquals(EquipmentLoan.LoanStatus.APPROVED, savedLoan.getStatus());
    }

    /**
     * Caso 2: Correo electrónico inválido
     * Validar: excepción, verifyNoInteractions()
     */
    @Test
    void createLoan_invalidEmail_shouldThrowException() {
        // Arrange
        String equipmentCode = "LAPTOP-ARICO";
        String invalidEmail = "correo-invalido";
        int loanDays = 5;

        // Act & Assert
        InvalidLoanException exception = assertThrows(
            InvalidLoanException.class,
            () -> loanService.createLoan(equipmentCode, invalidEmail, loanDays)
        );

        assertEquals("El correo electrónico debe tener un formato válido", exception.getMessage());
        verifyNoInteractions(loanRepository, policyClient);
    }

    /**
     * Caso 3: Cantidad de días fuera del rango permitido
     * Validar: assertThrows(), verifyNoInteractions()
     */
    @Test
    void createLoan_invalidLoanDays_shouldThrowException() {
        // Arrange
        String equipmentCode = "LAPTOP-ARICO";
        String borrowerEmail = "coarico@espe.edu.ec";
        int invalidDays = 20; // Fuera del rango 1-15

        // Act & Assert
        InvalidLoanException exception = assertThrows(
            InvalidLoanException.class,
            () -> loanService.createLoan(equipmentCode, borrowerEmail, invalidDays)
        );

        assertEquals("Los días de préstamo deben estar entre 1 y 15", exception.getMessage());
        verifyNoInteractions(loanRepository, policyClient);
    }

    /**
     * Caso 4: Equipo ya prestado
     * Validar: verify(), never()
     */
    @Test
    void createLoan_equipmentAlreadyLoaned_shouldThrowException() {
        // Arrange
        String equipmentCode = "LAPTOP-ARICO";
        String borrowerEmail = "coarico@espe.edu.ec";
        int loanDays = 10;

        when(policyClient.isUserBlocked(borrowerEmail)).thenReturn(false);
        when(loanRepository.isEquipmentLoaned(equipmentCode)).thenReturn(true);

        // Act & Assert
        InvalidLoanException exception = assertThrows(
            InvalidLoanException.class,
            () -> loanService.createLoan(equipmentCode, borrowerEmail, loanDays)
        );

        assertEquals("El equipo ya se encuentra prestado", exception.getMessage());
        verify(policyClient).isUserBlocked(borrowerEmail);
        verify(loanRepository).isEquipmentLoaned(equipmentCode);
        verify(loanRepository, never()).save(any());
    }
}
