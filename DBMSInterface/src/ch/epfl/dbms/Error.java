package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by Gianni on 05.06.2016.
 */
public class Error extends JFrame {
    private JPanel panel1;
    private JButton OKButton;


    public Error() {
    }

    public static void display() {
        JFrame frame = new JFrame("Error");
        Error error = new Error();
        frame.setContentPane(error.panel1);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(200, 100));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        error.OKButton.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }

}
