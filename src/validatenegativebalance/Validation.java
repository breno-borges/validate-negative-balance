package validatenegativebalance;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class Validation {
    
    public static ResultField validateNegativeColumnAcumulo(String filePath) throws IOException {
        // Traz os valores das colunas que preciso para validação
        List<Double> listAcumulate = GetColumn.getColumnAcumuloValues(filePath);
        List<String> operations = GetColumn.getColumnOperacaoValues(filePath);
        List<Double> transactions = GetColumn.getColumnTransacaoValues(filePath);
        List<Double> points = GetColumn.getColumnPointsValues(filePath);
        int indexNegativeValueFounded = findNegativeBalance(filePath);

        DecimalFormat decimalFormat = new DecimalFormat("#"); // Converte a notação cientifica

        // Pensar em uma forma de exibir todas as transações
        return new ResultField(listAcumulate.get(indexNegativeValueFounded),
                operations.get(indexNegativeValueFounded),
                decimalFormat.format(transactions.get(indexNegativeValueFounded)),
                points.get(indexNegativeValueFounded),
                indexNegativeValueFounded);
    }

    private static int findNegativeBalance(String filePath) throws IOException {
        List<Double> listAcumulate = GetColumn.getColumnAcumuloValues(filePath);
        List<String> operations = GetColumn.getColumnOperacaoValues(filePath);
        List<Double> points = GetColumn.getColumnPointsValues(filePath);
        int indexNegativeValueFounded = -1;
        int index = listAcumulate.size() - 1;
        boolean foundNegativeWithoutRefund = false;

        // Verificar condição para repetir loop e para quebrar o loop
        while (!foundNegativeWithoutRefund) {
            // Encontrar valor negativo na coluna acumulo desde que a operação não sejam estorno de credito
            for (int i = index; i >= 0; i--) { // Começa da última linha e ignora o cabeçalho
                if (listAcumulate.get(i) < 0 && !operations.get(i).equals("ESTORNO DE CRÉDITO")) {
                    indexNegativeValueFounded = i;
                    break;
                }
            }

            //Verifica a sequencia da lista após o primeiro valor negativo
            boolean hasRefund = false;
            for (int i = indexNegativeValueFounded - 1; i >= 0; i--) { // Começa ddo próximo índice após encontrar o negativo
                // Verifica se houve algum estorno do valor negativo encontrado
                if (points.get(i) == (points.get(indexNegativeValueFounded) * -1)
                        && operations.get(i).equals("ESTORNO DE CRÉDITO")
                        || operations.get(i).equals("ESTORNO DE EXPIRAÇÃO")) {
                    hasRefund = true;
                    index = i - 1;
                }
            }

            // Verifica se não houve estorno do valor negativo encontrado
            if (!hasRefund) {
                for (int i = indexNegativeValueFounded - 1; i >= 0; i--) {
                    foundNegativeWithoutRefund = false;
                    // Simula a transformação da coluna PONTOS em zero
                    List<Double> newBalance = reloadBalance(listAcumulate, points, indexNegativeValueFounded);

                    // Verifica se o saldo ficará positivo após a simulação
                    if (newBalance.getFirst() >= 0) {
                        return indexNegativeValueFounded; // Retorna o índice da linha onde a pessoa ficou com saldo negativo
                    }
                }
            }
        }
        return -1; // Retorna -1 se não encontrar nenhuma linha que se encaixe nas situações
    }
    
    private static List<Double> reloadBalance(List<Double> acumulate, List<Double> points, int index) {
        List<Double> newBalance = new ArrayList<>(acumulate); // Cria uma cópia do List acumulo para simulação
        newBalance.set(index, newBalance.get(index) - points.get(index)); // Zera o valor da coluna PONTOS na linha especificada

        // Recalcula o acumulo a partir do índice modificado até o início
        for (int i = index - 1; i >= 0; i--) {
            newBalance.set(i, newBalance.get(i + 1) + points.get(i));
        }

        return newBalance;
    }
}
