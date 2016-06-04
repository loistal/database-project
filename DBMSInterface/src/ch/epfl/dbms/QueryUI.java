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
    private JSpinner querySpinner;
    private DefaultTableModel table_model;
    private TableRowSorter sorter;
    private SQLProvider sp;
    private String[] queries = {"SELECT authors_number_publications.a_name\n" +
            "FROM (\n" +
            "    SELECT Authors.author_id, Authors.a_name, COUNT(*) as pubs\n" +
            "    FROM Authors, Publications, Publication_authors\n" +
            "    WHERE Publication_authors.author_id = Authors.author_id AND\n" +
            "          Publication_authors.publication_id = Publications.publication_id\n" +
            "    GROUP BY Authors.author_id, Authors.a_name\n" +
            "    ORDER BY pubs DESC\n" +
            ") authors_number_publications\n" +
            "WHERE ROWNUM <= 10",

            "SELECT Authors.a_name\n" +
            "FROM Authors\n" +
            "WHERE Authors.birthdate IN (\n" +
            "        SELECT MAX(Authors.birthdate)\n" +
            "        FROM Authors, Publications, Publication_authors\n" +
            "        WHERE Publication_authors.author_id = Authors.author_id AND\n" +
            "        Publication_authors.publication_id = Publications.publication_id AND\n" +
            "        EXTRACT (YEAR FROM Publications.publication_date) = 2010\n" +
            "\n" +
            "    ) OR\n" +
            "    Authors.birthdate IN (\n" +
            "      SELECT MIN(Authors.birthdate)\n" +
            "        FROM Authors, Publications, Publication_authors\n" +
            "        WHERE Publication_authors.author_id = Authors.author_id AND\n" +
            "        Publication_authors.publication_id = Publications.publication_id AND\n" +
            "        EXTRACT (YEAR FROM Publications.publication_date) = 2010\n" +
            ")"};

    public QueryUI() {
        sp = new SQLProvider();
        setSpinner();
        executeButton.addActionListener(actionEvent -> {
            String choice = (String) querySpinner.getValue();
            String query = "";

            switch(choice) {
                case "Number of Publications per year":
                    query = queries[0];
                    break;

                case "Ten authors with most publications":
                    query = queries[1];
                    break;

                default:
                    break;

            }

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


    private void setSpinner() {
        String[] tableNames = {
                "Number of Publications per year",
                "Ten authors with most publications",
                "Names of the youngest and oldest authors to publish something in 2010",
                "Comics whith publications with less than 50 pages, less than 100 pages, and more (or equal) than 100 pages?"
        };


        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        querySpinner.setModel(tableModel);
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
            e.printStackTrace();
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
