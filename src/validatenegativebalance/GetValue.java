package validatenegativebalance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class GetValue {
    
    public static List<Double> getValuesNumerics(int pointsColumnIndex, Sheet sheet) throws FileNotFoundException, IOException {

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
    
    public static List<String> getValuesText(int columnIndex, Sheet sheet) throws FileNotFoundException, IOException {
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
}
