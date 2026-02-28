package utils;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Utility class for table formatting.
 * Provides reusable methods for aligning JTable content.
 */
public class TableUtils {

    // Method to center-align all cells in a JTable
    public static void centerAlignTable(JTable table) {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

    TableColumnModel columnModel = table.getColumnModel();

    for (int i = 0; i < columnModel.getColumnCount(); i++) {

        //  To NOT override the Status column renderer
        // (column 4 = "Status")
        if (i == 4) continue;

        columnModel.getColumn(i).setCellRenderer(centerRenderer);
    }
}

}
