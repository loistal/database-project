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

        //for(int i = 0; i < MainScreen.tableNames.length; i++) {

            ResultSet resultSet = MainScreen.sqlProvider.query(


                    "select count(*) from user_tab_columns where table_name='Publication_series' "


            );

        try {
            int numberColumns = 0;
            while(resultSet.next()){
                //TODO: how to get number of columns?
            }
            System.out.println("Number of columns: " + numberColumns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //}

    }






}
