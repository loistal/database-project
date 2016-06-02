package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Gianni on 01.06.2016.
 */
public class DeleteUI {
    private JPanel mPanel;
    private JTextField descriptionText;
    private JSpinner tables;
    private JSpinner columns;
    private JTextField insertValueHereTextField;
    private JButton OKButton;


    public DeleteUI() {
        configureTableSpinner();
        configureDiscriminantSpinner();
        OKButton.addActionListener(e -> runQuery());
    }

    private void runQuery() {
        String column = insertValueHereTextField.getText();

    }

    private void configureTableSpinner() {
        String[] tableNames = {
                "Publication_series",
                "Publications"
        };
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        tables.setModel(tableModel);
    }

    private void configureDiscriminantSpinner() {
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
