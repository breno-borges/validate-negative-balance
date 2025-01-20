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
        List<Double> pointsValues = getPointsValues(pointsColumnIndex, sheet); //Pega os valores da coluna pontos
        List<Double> newColumnValues = calculatePointsToNewColumn(pointsValues); //Faz o acumulo de pontos

        //Grava o acumulo encontrato e cria uma nova planilha com a nova coluna
        writeDataToNewFile(filePath, inputStream, workbook, sheet, newColumnValues); 
    }
    
    private int findColumn(Sheet sheet, String column) throws FileNotFoundException, IOException {

        // Encontrar a coluna pontos
        int pointsColumnIndex = -1;
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(column)) {
                pointsColumnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (pointsColumnIndex == -1) {
            JOptionPane.showMessageDialog(null, "Coluna não encontrada");
            return 0;
        }

        return pointsColumnIndex;
    }

    private List<Double> getPointsValues(int pointsColumnIndex, Sheet sheet) throws FileNotFoundException, IOException {

        // Coletar os valores da coluna pontos
        List<Double> pointsValues = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell pointsCell = row.getCell(pointsColumnIndex);
                if (pointsCell != null && pointsCell.getCellType() == CellType.NUMERIC) {
                    pointsValues.add(pointsCell.getNumericCellValue());
                }
            }
        }

        return pointsValues;
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
