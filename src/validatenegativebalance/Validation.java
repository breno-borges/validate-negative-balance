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

    public List<ResultField> validateNegativeBalance(String filePath) throws IOException {
        List<Double> listAcumulate = GetColumnValue.getColumnAcumuloValues(filePath);
        List<String> operations = GetColumnValue.getColumnOperacaoValues(filePath);
        List<Double> points = GetColumnValue.getColumnPointsValues(filePath);
        List<Double> transactions = GetColumnValue.getColumnTransacaoValues(filePath);
        int indexNegativeValueFound = -1;
        int index = listAcumulate.size() - 1;
        boolean positiveBalance = false;

        ReloadBalance reloadBalance = new ReloadBalance(listAcumulate);
        List<ResultField> results = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#");
        
        while (!positiveBalance) {
            // Encontrar valor negativo na coluna acumulo desde que a operação não sejam estorno de credito
            for (int i = index; i >= 0; i--) { // Começa da última linha e ignora o cabeçalho
                if (listAcumulate.get(i) < 0 && !operations.get(i).equals("ESTORNO DE CRÉDITO")) {
                    indexNegativeValueFound = i;
                    break;
                }
            }

            //Verifica a sequencia da lista após o primeiro valor negativo
            boolean hasRefund = false;
            for (int i = indexNegativeValueFound - 1; i >= 0; i--) { // Começa ddo próximo índice após encontrar o negativo
                // Verifica se houve algum estorno do valor negativo encontrado
                if (operations.get(i).equals("ESTORNO DE CRÉDITO")
                        || operations.get(i).equals("ESTORNO DE EXPIRAÇÃO")
                        || operations.get(i).equals("ESTORNO DE VENCIMENTO")
                        && points.get(i) == (points.get(indexNegativeValueFound) * -1)) {
                    hasRefund = true;
                    index = i - 1;
                    break;
                }
            }

            if (operations.get(indexNegativeValueFound).equals("TRANSFERÊNCIA")
                    && points.get(indexNegativeValueFound) == (points.get(indexNegativeValueFound + 2) * -1)) {
                hasRefund = true;
                index = indexNegativeValueFound - 1;
            }

            // Verifica se não houve estorno do valor negativo encontrado
            if (!hasRefund) {
                
                // Simula a transformação da coluna PONTOS em zero
                List<Double> newBalance = reloadBalance.reloadBalance(points, indexNegativeValueFound);
                
                // Verifica se o saldo ficará positivo após a simulação
                if (newBalance.get(0) >= 0) {
                    ResultField resultField = new ResultField.Builder()
                            .index(indexNegativeValueFound)
                            .operation(operations.get(indexNegativeValueFound))
                            .transaction(decimalFormat.format(transactions.get(indexNegativeValueFound)))
                            .acumulate(listAcumulate.get(indexNegativeValueFound))
                            .point(points.get(indexNegativeValueFound))
                            .build();
                    results.add(resultField);
                    return results; // Retorna um array com o que deixou a pessoa com saldo negativo
                } else {
                    ResultField resultField = new ResultField.Builder()
                            .index(indexNegativeValueFound)
                            .operation(operations.get(indexNegativeValueFound))
                            .transaction(decimalFormat.format(transactions.get(indexNegativeValueFound)))
                            .acumulate(listAcumulate.get(indexNegativeValueFound))
                            .point(points.get(indexNegativeValueFound))
                            .build();
                    results.add(resultField);
                    index = indexNegativeValueFound - 1;
                }
            }
        }
        return null; // Retorna null se não encontrar nenhuma linha que se encaixe nas situações
    }
}
