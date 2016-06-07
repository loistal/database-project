package ch.epfl.dbms;

import javax.swing.*;
import java.util.HashMap;

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

    static  String[] tableNames = {
            "AUTHORS",
            "AWARD_CATEGORIES",
            "AWARD_TYPES",
            "AWARDS",
            "LANGUAGES",
            "NOTES",
            "PUBLICATION_AUTHORS",
            "PUBLICATION_CONTENT",
            "PUBLICATION_SERIES",
            "PUBLICATIONS",
            "PUBLISHER",
            "REVIEWS",
            "TAGS",
            "TITLE",
            "TITLE_AWARD",
            "TITLE_SERIES",
            "TITLE_TAGS",
            "WEB_PAGES"
    };

    public MainScreen() {

        sqlProvider = new SQLProvider();

        queriesButton.addActionListener(actionEvent -> QueryUI.display());
        insertButton.addActionListener(actionEvent -> InsertUI.display());
        searchButton.addActionListener(actionEvent -> SearchUI.display());
        deleteButton.addActionListener(actionEvent -> DeleteUI.display());

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
