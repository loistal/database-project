package ch.epfl.dbms;

import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
        for (int i = 0; i < MainScreen.tableNames.length; i++) {

            try {
                // do a query to get the resultSet
                ResultSet resultSet = MainScreen.sqlProvider.query(
                        "select count(*) from " + MainScreen.tableNames[i]
                );

                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int numberColumns = resultSetMetaData.getColumnCount();

                try {
                    // for each column of the table, look for the keywords
                    for (int j = 0; j < numberColumns; j++) {

                        ResultSet resultSetColumn = MainScreen.sqlProvider.query(
                                "SELECT * FROM " + MainScreen.tableNames[i] + " WHERE "
                                        + resultSetMetaData.getColumnName(j + 1) + " = " + keywords
                        );

                    }
                } catch (SQLException e) {
                    // The exceptions will be due to querying for keywords which don't
                    // correspond the the type of that column.
                    // In this case do nothing (could check type of input vs column type but
                    // let's save time).
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }


    public static void main(String[] args) {
        SearchUI.display();
    }






}
