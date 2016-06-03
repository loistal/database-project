package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by titoy on 6/2/16.
 */
public class SearchUI {

    private JPanel mPanel;
    private JLabel searchTextLabel;
    private JTextField searchBox;
    private JButton goButton;

    private SearchUI() {

        goButton.addActionListener(actionEvent -> search(searchBox.getText()));

    }

    static void display() {
        JFrame frame = new JFrame("Search");
        SearchUI searchUI = new SearchUI();
        frame.setContentPane(searchUI.mPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(720, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Searches the keywords in every single column of every single table
     * @param keywords
     */
    private void search(String keywords) {

        // for each table
        //for (int i = 0; i < MainScreen.tableNames.length; i++) {

            try {
                // do a query to get the resultSet
                ResultSet resultSet = MainScreen.sqlProvider.query(
                        "select * from Publication_series"
                );

                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int numberColumns = resultSetMetaData.getColumnCount();

                System.out.println("Number of columns in Publication_series is " + numberColumns);

                // for all the columns in the table
                for(int i = 0; i < numberColumns; i++) {
                    ResultSet resultSetColumn = null;

                    String query = "SELECT * FROM Publication_series WHERE Publication_series."
                            + resultSetMetaData.getColumnName(i + 1) + " LIKE "
                            + "'%" + keywords + "%'";

                    //int type = resultSetMetaData.getColumnType(1);

                    if (resultSetMetaData.getColumnType(i + 1) == Types.VARCHAR) {
                        resultSetColumn = MainScreen.sqlProvider.query(
                                query
                        );
                    }

                    //ResultSet resultSet1 = resultSetColumn;

                    // display result
                    if (resultSetColumn != null) {
                        while (resultSetColumn.next()) {
                            for (int k = 0; k < numberColumns; k++) {
                                System.out.print(resultSetColumn.getString(k + 1) + " ");
                            }
                            System.out.println();
                        }
                    } else {
                        System.out.print("result set is null");
                    }
                }

                /**
                    // for each column of the table, look for the keywords
                    for (int j = 0; j < numberColumns; j++) {

                        ResultSet resultSetColumn = null;

                        if(keywords instanceof Integer && resultSetMetaData.getColumnType(j + 1) == Types.INTEGER) {
                            resultSetColumn = MainScreen.sqlProvider.query(
                                    "SELECT * FROM Publication_series WHERE "
                                            + resultSetMetaData.getColumnName(j + 1) + " = " + keywords
                            );
                        } else if(keywords instanceof String && (resultSetMetaData.getColumnType(j + 1) == Types.VARCHAR
                        || resultSetMetaData.getColumnType(j + 1) == Types.CHAR)) {
                            resultSetColumn = MainScreen.sqlProvider.query(
                                    "SELECT * FROM Publication_series WHERE "
                                            + resultSetMetaData.getColumnName(j + 1) + " = " + "\'" + keywords + "\'"
                            );
                        }

                        if(resultSetColumn != null) {
                            while (resultSetColumn.next()) {
                                for (int k = 0; k < numberColumns; k++) {
                                    System.out.print(resultSetColumn.getString(k + 1) + " ");
                                }
                                System.out.println();
                            }
                        }

                    }
                 **/


            } catch (SQLException e) {
                e.printStackTrace();
            }


        //}
    }

    /**
     * Used to test whether the input provided in the search box is a number
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        SearchUI.display();
    }






}
