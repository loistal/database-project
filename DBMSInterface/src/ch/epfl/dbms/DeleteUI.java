package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Gianni on 01.06.2016.
 */
public class DeleteUI {
    private JPanel mPanel;
    private JSpinner tables;
    private JSpinner columns;
    private JTextField insertValueHereTextField;
    private JButton OKButton;
    private JButton OkButton2;
    private JButton OkButton3;
    private String table = "";
    private String column = "";
    private ResultSetMetaData resultSetMetaData;


    public DeleteUI() {
        columns.setEnabled(false);
        OKButton.setEnabled(false);
        OkButton3.setEnabled(false);
        insertValueHereTextField.setEnabled(false);

        configureTableSpinner();
        OkButton2.addActionListener(e -> {
            confirmTableAndEnable();
            configureDiscriminantSpinner();
        });
        OkButton3.addActionListener(e -> confirmColAndEnable());
        OKButton.addActionListener(e -> {
            try {
                runQuery();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void confirmTableAndEnable() {
        table = (String) tables.getValue();
        tables.setEnabled(false);
        columns.setEnabled(true);
        OkButton3.setEnabled(true);

    }

    private void confirmColAndEnable() {
        column = (String) columns.getValue();
        columns.setEnabled(false);
        insertValueHereTextField.setEnabled(true);
        OKButton.setEnabled(true);

    }

    private void runQuery() throws SQLException {
        String delValue = insertValueHereTextField.getText();
        System.out.println(delValue);
        if(delValue.equals("") || delValue.equals("Insert value here")) {
            showErrorMsg();
        } else {
            String query = "DELETE " +
                    "FROM " + table + " WHERE " + column + " = '" + delValue + "'";

            String select = "SELECT * FROM " + table + " WHERE " + column + " = '" + delValue + "'";
            System.out.println(select);
            System.out.println(query);

            int updatedRows = MainScreen.sqlProvider.update(query);

            if(updatedRows == 0 || updatedRows == -1) {
                System.out.println("No rows updated " + updatedRows);
            } else {
                System.out.println(updatedRows + " row(s) updated");
            }
        }

    }

    private void showErrorMsg() {
        //TODO
    }

    private void configureTableSpinner() {
        String[] tableNames = {
                "AUTHORS",
                "AWARD_CATEGORIES",
                "AWARD_TYPES",
                "AWARDS",
                "LANGUAGES",
                "NOTES",
                "PUBLICATION_AUTHORS",
                "PUBLICATION_CONTENT",
                "PUBLICATION_SERIES",
                "PUBLICATIONS",
                "PUBLISHER",
                "REVIEWS",
                "TAGS",
                "TITLE",
                "TITLE_AWARDS",
                "TITLE_SERIES",
                "TITLE_TAGS",
                "WEB_PAGES"

        };
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        tables.setModel(tableModel);
    }

    private void configureDiscriminantSpinner() {
        System.out.println("Table " + table);
        String query = "SELECT *" +
                " FROM " + table;

        try {
            ResultSet resultSet = MainScreen.sqlProvider.query(query);

            resultSetMetaData = resultSet.getMetaData();
            int numberOfColumns = resultSetMetaData.getColumnCount();
            String[] col = new String[numberOfColumns];

            for (int i = 1; i < numberOfColumns + 1; i++) {
                col[i-1] = resultSetMetaData.getColumnName(i);
            }

            SpinnerListModel colModel = new SpinnerListModel(col);
            columns.setModel(colModel);

        } catch (SQLException e ) {
            e.printStackTrace();
        }
    }

    

    public static void display() {
        JFrame frame = new JFrame("Delete");
        DeleteUI deleteUI = new DeleteUI();
        deleteUI.insertValueHereTextField.setHorizontalAlignment(JTextField.CENTER);
        frame.setContentPane(deleteUI.mPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(720, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
}
}
