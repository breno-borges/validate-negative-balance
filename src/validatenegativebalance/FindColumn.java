package validatenegativebalance;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class FindColumn {
    public static int findColumn(Sheet sheet, String column) throws FileNotFoundException, IOException {

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
}
