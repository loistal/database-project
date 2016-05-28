package ch.epfl.dbms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tobias on 11/05/16.
 */
public class MainScreen {
    private JPanel mainPanel;
    private JButton queriesButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton insertButton;


    public MainScreen() {
        queriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                QueryUI.display();
            }
        });
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //meh
        }

        new Thread(() -> {
            JFrame frame = new JFrame("DMBS");
            MainScreen ms = new MainScreen();
            frame.setContentPane(ms.mainPanel);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }).start();
    }

}
