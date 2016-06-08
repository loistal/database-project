package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Gianni on 08.06.2016.
 */
public class ChooseUI extends JDialog {
    private JLabel enterLabel;
    private JTextField value;
    private JButton OKButton;
    private JPanel mPanel;

    public ChooseUI(boolean isYear) {
        setModal(true);
        this.setContentPane(mPanel);
        setPreferredSize(new Dimension(200, 200));
        if(isYear) this.setTitle("Choose the year");
        else this.setTitle("Choose the author's name");

        if(isYear)
            enterLabel.setText("Enter the year:");
        else
            enterLabel.setText("Enter the author's name:");

        OKButton.addActionListener(e -> {
            if(!value.getText().equals("")) {
                dispose();
            }
        });
        pack();
        setVisible(true);

    }


    public String getValue() {
        return value.getText();
    }

}
