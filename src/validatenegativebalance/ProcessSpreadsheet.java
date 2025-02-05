package validatenegativebalance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

        List<Double> newColumnValues;

        if (filePath.endsWith(".csv")) {
            newColumnValues = calculatePointsToNewColumn(GetColumnValue.getColumnPointsValues(filePath));
            writeCsv(filePath, newColumnValues);

        } else {
            // Carrega a planilha do Excel
            FileInputStream inputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }

            newColumnValues = calculatePointsToNewColumn(GetColumnValue.getColumnPointsValues(filePath)); //Faz o acumulo de pontos

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

    private void writeCsv(String filePath, List<Double> newColumnValues) throws IOException {
        this.newPathSreadSheet = filePath.replace(".csv", "").concat("_processado.csv");

        // Abre o arquivo original para leitura
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
                FileWriter writer = new FileWriter(newPathSreadSheet)) {

            // Lê cada linha do arquivo original
            String line;
            int rowIndex = newColumnValues.size() - 1; // Para acompanhar o índice da linha e acessar o valor correspondente em newColumnValues
            while ((line = br.readLine()) != null) {
                // Escreve a linha original no novo arquivo
                writer.write(line);

                // Adiciona a vírgula e o valor da nova coluna
                writer.write("," + newColumnValues.get(rowIndex));
                writer.write(System.lineSeparator());

                rowIndex--;
            }
        }
        JOptionPane.showMessageDialog(null, "Criado novo arquivo CSV com a nova coluna acumulo!");
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
