package ch.epfl.dbms;

import sun.applet.Main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.SystemFlavorMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by titoy on 5/28/16.
 */
public class InsertUI {

    private JSpinner table;
    private JPanel insertPanel;

    public InsertUI() {
        //sqlProvider = new SQLProvider();
        //insertButton.addActionListener(actionEvent -> {

       // });

        configureSpinner();
    }

    public static void display() {
            JFrame frame = new JFrame("Insert");
            InsertUI insertUI = new InsertUI();
            frame.setContentPane(insertUI.insertPanel);
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
                "Authors",
                "Publications"
        };
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        table .setModel(tableModel);

        table.addChangeListener(
                e -> initFormForTable((String)table.getValue())
        );
    }

    //Change the form in order to have the right fields and labels
    private void initFormForTable(String tableName) {

        // Get the number of columns in the table
        ResultSet resultSet = MainScreen.sqlProvider.query(
                /**
                "SELECT COUNT(*)" +
                "FROM " + tableName + ".COLUMNS" +
                "WHERE TABLE_NAME = " + tableName
                 **/
                "SELECT COUNT(*)"
                + "FROM Publications"
        );

        try {
            while (resultSet.next()) {
                //String numberColumns = resultSet.getString("COUNT(*)");
                String result = resultSet.getString("COUNT(*)");
                System.out.print("Number of rows in " + tableName + " = " + result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
