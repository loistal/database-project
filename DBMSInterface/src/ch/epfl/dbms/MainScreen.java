package ch.epfl.dbms;

import javax.swing.*;

/**
 * Created by tobias on 11/05/16.
 */
public class MainScreen {

    // provider used throughout the program
    public static SQLProvider sqlProvider;

    private JPanel mainPanel;
    private JButton queriesButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton insertButton;

    static String[] tableNames = {
            "Publication_series",
            "Publications",
            "Authors",
            "Languages",
            "Tags",
            "Notes",
            "Title_series",
            "Reviews",
            "Title_awards",
            "Title_tags",
            "Publications",
            "Publication_authors",
            "Publication_content",
            "Publication_series",
            "Publisher",
            "Awards",
    };

    public MainScreen() {

        sqlProvider = new SQLProvider();

        queriesButton.addActionListener(actionEvent -> QueryUI.display());
        insertButton.addActionListener(actionEvent -> InsertUI.display());
        searchButton.addActionListener(actionEvent -> SearchUI.display());
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
            e.printStackTrace();
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
