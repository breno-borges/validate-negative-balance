package validatenegativebalance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            listAcumulate = getColumnNumericsValueCSV(filePath, 3);
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
        if (filePath.endsWith("processado.csv")) {
            operations = getColumnTextsValueCSV(filePath, 1);
        } else if (filePath.endsWith("csv")) {
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
        if (filePath.endsWith("processado.csv")) {
            transactions = getColumnNumericsValueCSV(filePath, 0);
        } else if (filePath.endsWith(".csv")) {
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
        if (filePath.endsWith("processado.csv")) {
            points = getColumnNumericsValueCSV(filePath, 2);
        } else if (filePath.endsWith(".csv")) {
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            String valueString;
            double value = 0.0;
            while ((line = br.readLine()) != null) {
                String[] valuesSplit = line.split(",");
                if (valuesSplit.length > index) {
                    try {
                        if (valuesSplit.length == 13 && index == 7) {
                            // Remove as aspas duplas antes da conversão
                            valueString = valuesSplit[index].trim().replace("\"", "");
                            value = Double.parseDouble(valueString);
                        } else if (valuesSplit.length == 14 && index == 7) {
                            valueString = valuesSplit[index].trim().replace("\"", "");
                            if (valueString.equalsIgnoreCase("TROCA")
                                    || valueString.equalsIgnoreCase("CRÉDITO")
                                    || valueString.equalsIgnoreCase("TRANSFERÊNCIA")
                                    || valueString.equalsIgnoreCase("ESTORNO DE CRÉDITO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE EXPIRAÇÃO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE VENCIMENTO")
                                    || valueString.equalsIgnoreCase("CR�DITO")
                                    || valueString.equalsIgnoreCase("TRANSFER�NCIA")
                                    || valueString.equalsIgnoreCase("ESTORNO DE CR�DITO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE EXPIRA��O")){
                                valueString = valuesSplit[index + 1].trim().replace("\"", "");
                                value = Double.parseDouble(valueString);
                            } else {
                                value = Double.parseDouble(valueString);
                            }
                        } else if(index == 1) {
                            // Remove as aspas duplas antes da conversão
                            valueString = valuesSplit[index].trim().replace("\"", "");
                            valueString = valueString.replace("EXT", "");
                            value = Double.parseDouble(valueString);
                        } else {
                            // Remove as aspas duplas antes da conversão
                            valueString = valuesSplit[index].trim().replace("\"", "");
                            if (valueString.equalsIgnoreCase("TROCA")
                                    || valueString.equalsIgnoreCase("CRÉDITO")
                                    || valueString.equalsIgnoreCase("TRANSFERÊNCIA")
                                    || valueString.equalsIgnoreCase("ESTORNO DE CRÉDITO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE EXPIRAÇÃO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE VENCIMENTO")
                                    || valueString.equalsIgnoreCase("CR�DITO")
                                    || valueString.equalsIgnoreCase("TRANSFER�NCIA")
                                    || valueString.equalsIgnoreCase("ESTORNO DE CR�DITO")
                                    || valueString.equalsIgnoreCase("ESTORNO DE EXPIRA��O")) {
                                valueString = valuesSplit[index + 1].trim().replace("\"", "");
                                value = Double.parseDouble(valueString);
                            } else {
                                value = Double.parseDouble(valueString);
                            }
                        }
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            String valueString;
            while ((line = br.readLine()) != null) {
                String[] valuesSplit = line.split(",");
                if (valuesSplit.length > index) {
                    try {
                        if (valuesSplit.length == 13) {
                            valueString = valuesSplit[index].trim().replace("\"", "");
                        } else {
                            valueString = valuesSplit[index].trim().replace("\"", "");
                            if (valueString.contains("00")) {
                                valueString = valuesSplit[index + 1].trim().replace("\"", "");
                            }
                        }
                        values.add(valueString);
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
