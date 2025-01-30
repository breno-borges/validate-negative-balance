package validatenegativebalance;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class ResultField {

    private double acumulate;
    private String operation;
    private String transaction;
    private double point;
    private int index;

    private ResultField(Builder builder) {
        this.acumulate = builder.acumulate;
        this.operation = builder.operation;
        this.transaction = builder.transaction;
        this.point = builder.point;
        this.index = builder.index;
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

    public static class Builder {
        private double acumulate;
        private String operation;
        private String transaction;
        private double point;
        private int index;

        public Builder acumulate(double acumulate) {
            this.acumulate = acumulate;
            return this;
        }

        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }

        public Builder transaction(String transaction) {
            this.transaction = transaction;
            return this;
        }

        public Builder point(double point) {
            this.point = point;
            return this;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public ResultField build() {
            return new ResultField(this);
        }
    }
}