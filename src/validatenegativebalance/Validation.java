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
    private int index;

    public Validation() {
    }

    public Validation(double acumulate, String operation, String transaction, double point, int index) {
        this.acumulate = acumulate;
        this.operation = operation;
        this.transaction = transaction;
        this.point = point;
        this.index = index;
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

    public int getIndex() {
        return index;
    }
    
    
}
