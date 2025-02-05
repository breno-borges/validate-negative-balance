package validatenegativebalance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class GetColumnValue {

    private static Workbook workbook;

    public static List<Double> getColumnAcumuloValues(String filePath) throws FileNotFoundException, IOException {
        List<Double> listAcumulate;

        if (filePath.endsWith(".csv")) {
            listAcumulate = getColumnNumericsValueCSV(filePath, 14);
        } else {
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
            listAcumulate = GetValue.getValuesNumerics(columnIndex, sheet);
            inputStream.close();
            workbook.close();
        }

        return listAcumulate;
    }

    public static List<String> getColumnOperacaoValues(String filePath) throws FileNotFoundException, IOException {
        List<String> operations;
        if (filePath.endsWith(".csv")) {
            operations = getColumnTextsValueCSV(filePath, 6);
        } else {
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
            operations = GetValue.getValuesText(columnIndex, sheet);
            inputStream.close();
            workbook.close();
        }

        return operations;
    }

    public static List<Double> getColumnTransacaoValues(String filePath) throws FileNotFoundException, IOException {
        List<Double> transactions;
        if (filePath.endsWith(".csv")) {
            transactions = getColumnNumericsValueCSV(filePath, 1);
        } else {
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
            transactions = GetValue.getValuesNumerics(columnIndex, sheet);
            inputStream.close();
            workbook.close();
        }
        return transactions;
    }

    public static List<Double> getColumnPointsValues(String filePath) throws FileNotFoundException, IOException {
        List<Double> points;
        if (filePath.endsWith(".csv")) {
            points = getColumnNumericsValueCSV(filePath, 7);
        } else {
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
            points = GetValue.getValuesNumerics(columnIndex, sheet);
            inputStream.close();
            workbook.close();
        }

        return points;
    }

    private static List<Double> getColumnNumericsValueCSV(String filePath, int index) throws FileNotFoundException, IOException {
        List<Double> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] valuesSplit = line.split(",");
                if (valuesSplit.length > index) {
                    try {
                        // Remove as aspas duplas antes da conversão
                        String valueString = valuesSplit[index].trim().replace("\"", "");
                        double value = Double.parseDouble(valueString);
                        values.add(value);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao converter valor para número na linha: " + line);
                        return null;
                    }
                } else {
                    System.err.println("Linha com formato inválido: " + line);
                }
            }
        }
        return values;
    }

    private static List<String> getColumnTextsValueCSV(String filePath, int index) throws FileNotFoundException, IOException {
        List<String> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] valuesSplit = line.split(",");
                if (valuesSplit.length > index) {
                    try {
                        // Remove as aspas duplas antes da conversão
                        String value = valuesSplit[index].trim().replace("\"", "");
                        values.add(value);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao converter valor para número na linha: " + line);
                        return null;
                    }
                } else {
                    System.err.println("Linha com formato inválido: " + line);
                }
            }
        }
        return values;
    }
}
