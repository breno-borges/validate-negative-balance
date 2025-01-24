package validatenegativebalance;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class Validation {
    
    private double acumulate;
    private String operation;
    private String transaction;
    private double point;

    public Validation(double acumulate, String operation, String transaction, double point) {
        this.acumulate = acumulate;
        this.operation = operation;
        this.transaction = transaction;
        this.point = point;
    }

    public double getAcumulate() {
        return acumulate;
    }

    public String getOperation() {
        return operation;
    }

    public String getTransaction() {
        return transaction;
    }

    public double getPoint() {
        return point;
    }
    
    
}
