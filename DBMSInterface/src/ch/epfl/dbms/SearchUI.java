package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

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
     * @param keywords The keyword which the user typed inside the text field
     */
    private void search(String keywords) {

        //for each table
        for (int t = 0; t < MainScreen.tableNames.length; t++)
            try {
                // Get number of columns
                ResultSet resultSet = MainScreen.sqlProvider.query(
                        "select * from " + MainScreen.tableNames[t]
                );
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int numberColumns = resultSetMetaData.getColumnCount();

                // Look for the keywords in each column
                for (int i = 0; i < numberColumns; i++) {

                    ResultSet resultSetColumn = null;
                    if (resultSetMetaData.getColumnType(i + 1) == Types.VARCHAR) {
                        resultSetColumn = MainScreen.sqlProvider.query(
                                "SELECT * FROM " + MainScreen.tableNames[t]
                                        + " WHERE " + MainScreen.tableNames[t] + "."
                                        + resultSetMetaData.getColumnName(i + 1) + " LIKE "
                                        + "'%" + keywords + "%'"
                        );
                    }

                    // Save the results for later use
                    ArrayList<ArrayList<String>> allResults = new ArrayList<>();
                    if (resultSetColumn != null) {
                        while (resultSetColumn.next()) {
                            ArrayList<String> row = new ArrayList<>();
                            for (int k = 0; k < numberColumns; k++) {
                                row.add(resultSetColumn.getString(k + 1));
                            }
                            allResults.add(row);
                        }
                    }

                    // do stuff with results
                    for(ArrayList<String> row: allResults) {
                        System.out.println(row);
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * Used to test whether the input provided in the search box is a number
     * @param str The string to be tested
     * @return Whether or not the string is a number
     */
    public static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
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
