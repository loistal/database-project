package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by titoy on 5/28/16.
 */
public class InsertUI {

    private JSpinner table;
    private JPanel mPanel;
    private JTextField value;
    private JLabel columnName;
    private JButton OKButton;

    private ResultSetMetaData resultSetMetaData;

    // the index of the field the user needs to enter
    private int currentField = 1;

    // number of columns of the current table
    private int numberOfColumns;

    private ArrayList<String> fields;

    private InsertUI() {

        fields = new ArrayList();

        configureSpinner();
        initFormForTable((String)table.getValue());
        OKButton.addActionListener(l -> nextEntryOrValidate());

    }

    static void display() {
            JFrame frame = new JFrame("Insert");
            InsertUI insertUI = new InsertUI();
            frame.setContentPane(insertUI.mPanel);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(720, 480));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }

    /**
     * Sets the possible values for the spinner, and sets a listener
     */
    private void configureSpinner() {
        String[] tableNames = {
                "Publication_series",
                "Publications",
                "Authors",
                "Languages",
                "Tags",
                "Notes",
                "Title_series",
                "Reviews",
                "Title_awards",
                "Title_tags",
                "Publications",
                "Publication_authors",
                "Publication_content",
                "Publication_series",
                "Publisher",
                "Awards",
        };
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        table .setModel(tableModel);

        table.addChangeListener(
                e -> initFormForTable((String)table.getValue())
        );

    }

    //Change the form in order to have the right fields and labels
    private void initFormForTable(String tableName) {

        String query = "SELECT *" +
                " FROM " + tableName;

        ResultSet resultSet = MainScreen.sqlProvider.query(query);

        try {

            resultSetMetaData = resultSet.getMetaData();
            numberOfColumns = resultSetMetaData.getColumnCount();
            System.out.println("Number of columns: " + numberOfColumns);

            // Column indexes start from 1 for oracle databases (not from 0!)
            columnName.setText("Enter the " + resultSetMetaData.getColumnName(1));


        } catch (SQLException e ) {
            e.printStackTrace();
        }

    }

    /**
     * When the user presses the OK button, either :
     * 1. empty the JTextField and let the user enter the next field
     * 2. or if this was the last field, send insert command and confirm to user
     */
    private void nextEntryOrValidate() {

        // The user just finished entering all the fields
        if(currentField == numberOfColumns) {

            fields.add(value.getText());

            String query = "INSERT INTO " + table.getValue() + " VALUES (";

            for(int i = 0; i < fields.size(); i++) {

                if(i == fields.size() - 1) {
                    query += "\'"+ fields.get(i) + "\'" + ")";
                } else {
                    query += "\'"+ fields.get(i) + "\'" + ",";
                }
            }

            try {
                MainScreen.sqlProvider.update(query);
                columnName.setText("Insertion was performed successfully!");
            } catch (SQLException e) {
                columnName.setText("Error while updating the database: " + e.toString());
            }

            //debug
            System.out.println("Inputs: ");
            System.out.println(fields);
            System.out.println(query);

        } else {

            currentField++;
            try {

                fields.add(value.getText());

                // Clear the text field
                value.setText("");

                columnName.setText("Enter the " + resultSetMetaData.getColumnName(currentField));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

}
