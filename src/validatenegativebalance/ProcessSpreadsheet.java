package validatenegativebalance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class ProcessSpreadsheet {

    private String newPathSreadSheet;

    public String getNewPathSreadSheet() {
        return newPathSreadSheet;
    }

    public void processSpreadsheet(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "PONTOS"; //Informa qual coluna procurar

        int pointsColumnIndex = findColumn(sheet, column); //Traz o índice da coluna pontos
        List<Double> pointsValues = getValuesNumerics(pointsColumnIndex, sheet); //Pega os valores da coluna pontos
        List<Double> newColumnValues = calculatePointsToNewColumn(pointsValues); //Faz o acumulo de pontos

        //Grava o acumulo encontrato e cria uma nova planilha com a nova coluna
        writeDataToNewFile(filePath, inputStream, workbook, sheet, newColumnValues);
    }

    public Validation validateNegativeColumnAcumulo(String filePath) throws IOException {
        // Traz os valores das colunas que preciso para validação
        List<Double> listAcumulate = getColumnAcumulo(filePath);
        List<String> operations = getColumnOperacaoValues(filePath);
        List<Double> transactions = getColumnTransacaoValues(filePath);
        List<Double> points = getColumnPointsValues(filePath);
        int indexNegativeValueFounded = findNegativeBalance(filePath);

        DecimalFormat decimalFormat = new DecimalFormat("#"); // Converte a notação cientifica

        // Pensar em uma forma de exibir todas as transações
        return new Validation(listAcumulate.get(indexNegativeValueFounded),
                operations.get(indexNegativeValueFounded),
                decimalFormat.format(transactions.get(indexNegativeValueFounded)),
                points.get(indexNegativeValueFounded),
                indexNegativeValueFounded);
    }

    private int findNegativeBalance(String filePath) throws IOException {
        List<Double> listAcumulate = getColumnAcumulo(filePath);
        List<String> operations = getColumnOperacaoValues(filePath);
        List<Double> points = getColumnPointsValues(filePath);
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

    private List<Double> reloadBalance(List<Double> acumulate, List<Double> points, int index) {
        List<Double> newBalance = new ArrayList<>(acumulate); // Cria uma cópia do List acumulo para simulação
        newBalance.set(index, newBalance.get(index) - points.get(index)); // Zera o valor da coluna PONTOS na linha especificada

        // Recalcula o acumulo a partir do índice modificado até o início
        for (int i = index - 1; i >= 0; i--) {
            newBalance.set(i, newBalance.get(i + 1) + points.get(i));
        }

        return newBalance;
    }

    public List<Double> getColumnAcumulo(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "acumulo"; //Informa qual coluna procurar
        int columnIndex = findColumn(sheet, column); //Traz o índice da coluna acumulo

        inputStream.close();
        workbook.close();
        return getValuesNumerics(columnIndex, sheet); //Retorna uma lista    
    }

    private List<String> getColumnOperacaoValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "OPERACAO"; //Informa qual coluna procurar
        int columnIndex = findColumn(sheet, column); //Traz o índice da coluna operacao
        List<String> operations = getValuesText(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return operations;
    }

    private List<Double> getColumnTransacaoValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "TRANSACAO"; //Informa qual coluna procurar
        int columnIndex = findColumn(sheet, column); //Traz o índice da coluna transacao
        List<Double> transactions = getValuesNumerics(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return transactions;
    }

    private List<Double> getColumnPointsValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "PONTOS"; //Informa qual coluna procurar
        int columnIndex = findColumn(sheet, column); //Traz o índice da coluna pontos
        List<Double> points = getValuesNumerics(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return points;
    }

    private List<String> getValuesText(int columnIndex, Sheet sheet) throws FileNotFoundException, IOException {
        // Coletar os valores da coluna a partir do indice
        List<String> textValues = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell textCell = row.getCell(columnIndex);
                if (textCell != null && textCell.getCellType() == CellType.STRING) {
                    textValues.add(textCell.getStringCellValue());
                }
            }
        }

        return textValues;
    }

    private int findColumn(Sheet sheet, String column) throws FileNotFoundException, IOException {

        // Encontrar a coluna informada nos parametros
        int columnIndex = -1;
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(column)) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Coluna não encontrada");
            return 0;
        }

        return columnIndex;
    }

    private List<Double> getValuesNumerics(int pointsColumnIndex, Sheet sheet) throws FileNotFoundException, IOException {

        // Coletar os valores da coluna a partir do indice
        List<Double> numericValues = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell numericCell = row.getCell(pointsColumnIndex);
                if (numericCell != null && numericCell.getCellType() == CellType.NUMERIC) {
                    numericValues.add(numericCell.getNumericCellValue());
                }
            }
        }

        return numericValues;
    }

    private List<Double> calculatePointsToNewColumn(List<Double> pointsValues) throws FileNotFoundException, IOException {

        // Calcular os valores da nova coluna
        List<Double> newColumnValues = new ArrayList<>();
        double lastValue = pointsValues.get(pointsValues.size() - 1);
        newColumnValues.add(lastValue);

        for (int i = pointsValues.size() - 2; i >= 0; i--) {
            double sum = pointsValues.get(i) + newColumnValues.get(pointsValues.size() - i - 2);
            newColumnValues.add(sum);
        }

        return newColumnValues;
    }

    private void writeDataToNewFile(String filePath, FileInputStream inputStream, Workbook workbook, Sheet sheet, List<Double> newColumnValues) throws FileNotFoundException, IOException {

        // Criar a nova coluna que faz o acumulo de pontos
        int newColumnAccumulation = sheet.getRow(0).getLastCellNum();
        sheet.getRow(0).createCell(newColumnAccumulation).setCellValue("acumulo");

        // Preencher a nova coluna na planilha
        for (int i = newColumnValues.size() - 1; i >= 0; i--) {
            Row row = sheet.getRow(newColumnValues.size() - i);
            if (row == null) {
                row = sheet.createRow(newColumnValues.size() - i);
            }
            Cell newColumnCell = row.createCell(newColumnAccumulation);
            newColumnCell.setCellValue(newColumnValues.get(i));
        }

        // Salvar a planilha modificada
        inputStream.close();
        this.newPathSreadSheet = filePath.replace(".xls", "").concat("_processada.xls");
        FileOutputStream outputStream = new FileOutputStream(newPathSreadSheet);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        JOptionPane.showMessageDialog(null, "Criada nova planilha com a nova coluna acumulo!");
    }

}
