package validatenegativebalance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class GetColumn {
    private static Workbook workbook;
    
    public static List<Double> getColumnAcumuloValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        if (filePath.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "acumulo"; //Informa qual coluna procurar
        int columnIndex = FindColumn.findColumn(sheet, column); //Traz o índice da coluna acumulo

        inputStream.close();
        workbook.close();
        return GetValue.getValuesNumerics(columnIndex, sheet); //Retorna uma lista    
    }

    public static List<String> getColumnOperacaoValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        if (filePath.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "OPERACAO"; //Informa qual coluna procurar
        int columnIndex = FindColumn.findColumn(sheet, column); //Traz o índice da coluna operacao
        List<String> operations = GetValue.getValuesText(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return operations;
    }

    public static List<Double> getColumnTransacaoValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        if (filePath.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "TRANSACAO"; //Informa qual coluna procurar
        int columnIndex = FindColumn.findColumn(sheet, column); //Traz o índice da coluna transacao
        List<Double> transactions = GetValue.getValuesNumerics(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return transactions;
    }

    public static List<Double> getColumnPointsValues(String filePath) throws FileNotFoundException, IOException {
        // Carrega a planilha do Excel
        FileInputStream inputStream = new FileInputStream(filePath);
        if (filePath.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira linha
        String column = "PONTOS"; //Informa qual coluna procurar
        int columnIndex = FindColumn.findColumn(sheet, column); //Traz o índice da coluna pontos
        List<Double> points = GetValue.getValuesNumerics(columnIndex, sheet);
        inputStream.close();
        workbook.close();

        return points;
    }
}
