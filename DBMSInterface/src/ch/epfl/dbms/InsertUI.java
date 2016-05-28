package ch.epfl.dbms;

import javax.swing.*;
import java.sql.SQLException;

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

        String[] tableNames = {
                "Authors",
                "Publications"
        };
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        table .setModel(tableModel);
    }

    public static void display() {
            JFrame frame = new JFrame("Insert");
            InsertUI insertUI = new InsertUI();
            frame.setContentPane(insertUI.insertPanel);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }

}
