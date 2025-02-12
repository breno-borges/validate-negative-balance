package validatenegativebalance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class ProcessSpreadsheet {

    private String newPathSreadSheet;
    private Workbook workbook;

    public String getNewPathSreadSheet() {
        return newPathSreadSheet;
    }

    public void processSpreadsheet(String filePath) throws FileNotFoundException, IOException {

        
        if (filePath.endsWith(".csv")) {
            writeCsv(filePath);
        } else {
            // Carrega a planilha do Excel
            FileInputStream inputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }

            List<Double> newColumnValues = calculatePointsToNewColumn(GetColumnValue.getColumnPointsValues(filePath)); //Faz o acumulo de pontos

            //Grava o acumulo encontrato e cria uma nova planilha com a nova coluna
            writeXlsOrXlsx(filePath, inputStream, workbook, newColumnValues);
        }
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

    private void writeCsv(String filePath) throws IOException {
        this.newPathSreadSheet = filePath.replace(".csv", "").concat("_processado.csv");
        
        List<Double> transactions = GetColumnValue.getColumnTransacaoValues(filePath);
        List<String> operations = GetColumnValue.getColumnOperacaoValues(filePath);
        List<Double> points = GetColumnValue.getColumnPointsValues(filePath);
        List<Double> newColumnValues = calculatePointsToNewColumn(points);
        
        // Abre o arquivo original para leitura
        try (FileWriter writer = new FileWriter(newPathSreadSheet)) {

            // Verifica se as listas são válidas e têm o mesmo tamanho
            if (transactions != null && operations != null && transactions.size() == transactions.size()) {

                int newColumnIndex = newColumnValues.size() - 1;
                for (int i = 0; i < transactions.size(); i++) {
                    writer.write(transactions.get(i) + "," + operations.get(i) + "," + points.get(i) + "," + newColumnValues.get(newColumnIndex));
                    writer.write(System.lineSeparator());
                    newColumnIndex--;
                }

                JOptionPane.showMessageDialog(null, "Criado novo arquivo CSV!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro: Problema ao ler colunas do arquivo CSV.");
            }
        }
    }

    private void writeXlsOrXlsx(String filePath, FileInputStream inputStream, Workbook workbook, List<Double> newColumnValues) throws FileNotFoundException, IOException {

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha

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
        if (filePath.endsWith(".xls")) {
            this.newPathSreadSheet = filePath.replace(".xls", "").concat("_processada.xls");
        } else if (filePath.endsWith(".xlsx")) {
            this.newPathSreadSheet = filePath.replace(".xlsx", "").concat("_processada.xlsx");
        }
        FileOutputStream outputStream = new FileOutputStream(newPathSreadSheet);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        JOptionPane.showMessageDialog(null, "Criada nova planilha com a nova coluna acumulo!");
    }

}
