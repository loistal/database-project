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

            // get number of columns
            ResultSet resultSet = MainScreen.sqlProvider.query(


                    "select count(*) from Publications "


            );




            try {
                boolean goFirstRowSuccess = resultSet.first();

                if(!goFirstRowSuccess) {
                    throw new SQLException("Could not move to the first row!");
                }

                int numberColumns = resultSet.getInt("count(*)");

                //debug
                System.out.println("Number of columns: " + numberColumns);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }


    public static void main(String[] args) {
        SearchUI.display();
    }






}
