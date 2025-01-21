package validatenegativebalance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public void validateNegativeColumnAcumulo(String filePath) throws IOException {
        // Traz os valores das colunas que preciso para validação
        List<Double> listAcumulate = getColumnAcumulo(filePath);
        List<String> operations = getColumnOperacaoValues(filePath);
        List<Double> transactions = getColumnTransacaoValues(filePath); // Tratar as transações
        
        
        /*for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(i + " pos " + transactions.get(i));
        }
        
        
        for (int i = listAcumulate.size() - 1; i >= 0; i--) {
            
            System.out.println(listAcumulate.get(i) + " pos " + i + " " + operations.get(i) + " " + transactions.get(i));
        }*/
    }
    
    private List<String> getColumnOperacaoValues(String filePath) throws FileNotFoundException, IOException{
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
    
    private List<Double> getColumnTransacaoValues(String filePath) throws FileNotFoundException, IOException{
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
    
    private List<String> getValuesText(int columnIndex, Sheet sheet) throws FileNotFoundException, IOException{
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
