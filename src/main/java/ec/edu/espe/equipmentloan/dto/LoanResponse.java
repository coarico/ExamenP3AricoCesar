package ec.edu.espe.equipmentloan.dto;

/**
 * DTO de respuesta para la creación de un préstamo
 * @author Cesar Arico - coarico@espe.edu.ec
 */
public class LoanResponse {
    
    private final String loanId;
    private final String approvalCode;

    /**
     * Constructor del DTO de respuesta
     * @param loanId ID del préstamo creado
     * @param approvalCode Código de aprobación generado
     */
    public LoanResponse(String loanId, String approvalCode) {
        this.loanId = loanId;
        this.approvalCode = approvalCode;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getApprovalCode() {
        return approvalCode;
    }
}
