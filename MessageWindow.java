package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Gianni on 05.06.2016.
 */
public class MessageWindow {
    private JPanel panel1;
    private JButton button1;
    private JLabel msg;
    private JLabel noUpdate;


    public MessageWindow() {

    }

    public static void display(boolean success) {
        JFrame frame;
        if(success)
            frame = new JFrame("Success");
        else
            frame = new JFrame("No Update");

        MessageWindow messageWindow = new MessageWindow();
        frame.setContentPane(messageWindow.panel1);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(200, 100));
        frame.pack();
        frame.setLocationRelativeTo(null);
        if(success) {
            messageWindow.msg.setText("Delete completed successfully");
        } else {
            messageWindow.msg.setText("No row to delete");
        }
        frame.setVisible(true);

        messageWindow.button1.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }

}
