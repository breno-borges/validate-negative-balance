package validatenegativebalance;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class ReloadBalance {
    
    private List<Double> newBalance;

    public ReloadBalance(List<Double> acumulate) {
        this.newBalance = new ArrayList<>(acumulate);// Cria uma cópia do List acumulo para simulação
    }
    
    public List<Double> reloadBalance(List<Double> points, int index) {
        
        newBalance.set(index, newBalance.get(index) - points.get(index)); // Zera o valor da coluna PONTOS na linha especificada

        // Recalcula o acumulo da próxima linha até o início
        for (int i = index - 1; i >= 0; i--) {
            newBalance.set(i, newBalance.get(i + 1) + points.get(i));
        }

        return newBalance;
    }
}
