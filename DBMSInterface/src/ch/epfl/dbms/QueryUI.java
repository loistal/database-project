package ch.epfl.dbms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryUI {

    private JPanel mainPanel;
    private JTextField queryField;
    private JLabel queryLabel;
    private JButton executeButton;
    private JTable resultsTable;
    private JScrollPane resultsContainer;
    private JTextField searchBox;
    private JButton searchButton;
    private DefaultTableModel table_model;
    private TableRowSorter sorter;
    private SQLProvider sp;

    public QueryUI() {
        sp = new SQLProvider();
        executeButton.addActionListener(actionEvent -> {
            String query = queryField.getText();

            if (!query.isEmpty()) {
                searchButton.setEnabled(false);
                searchBox.setEnabled(false);
                try {
                    populateResultsTable(sp.query(query));
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

        });
        searchButton.addActionListener(actionEvent -> {
            String search = searchBox.getText();

            if (search.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(search));
            }
        });
    }

    private void populateResultsTable(ResultSet rs) throws SQLException {
        if (rs == null) return;

        table_model.setNumRows(0);

        int columns = rs.getMetaData().getColumnCount();

        String[] column_names = new String[columns];

        for (int i = 1; i <= columns; i++) {
            column_names[i - 1] = rs.getMetaData().getColumnName(i);
        }

        table_model.setColumnCount(columns);
        table_model.setColumnIdentifiers(column_names);

        while (rs.next()) {
            Object[] row = new Object[columns];
            for (int i = 1; i <= columns; ++i) {
                row[i - 1] = rs.getObject(i);
            }
            table_model.addRow(row);
        }

        searchBox.setEnabled(true);
        searchButton.setEnabled(true);

    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //meh
        }

        display();
    }

    public static void display() {
        new Thread(() -> {
            JFrame frame = new JFrame("Queries");
            QueryUI queryUI = new QueryUI();
            frame.setContentPane(queryUI.mainPanel);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }).start();
    }

    private void createUIComponents() {
        table_model = new DefaultTableModel();
        sorter = new TableRowSorter<TableModel>(table_model);
        resultsTable = new JTable(table_model);
        resultsTable.setRowSorter(sorter);
        resultsContainer = new JScrollPane(resultsTable);
    }
}
