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

    // Used for follow-up search in searchUI
    static HashMap<String, String> keys;

    public MainScreen() {

        sqlProvider = new SQLProvider();

        queriesButton.addActionListener(actionEvent -> QueryUI.display());
        insertButton.addActionListener(actionEvent -> InsertUI.display());
        searchButton.addActionListener(actionEvent -> SearchUI.display());
        deleteButton.addActionListener(actionEvent -> DeleteUI.display());

        keys = new HashMap<>();
        keys.put("AUTHORS", "AUTHOR_ID");
        keys.put("AWARD_CATEGORIES", "AC_ID");
        keys.put("AWARD_TYPES", "AT_ID");
        keys.put("AWARDS", "AWARD_ID");
        keys.put("LANGUAGES", "LANGUAGE_ID");
        keys.put("NOTES", "NOTE_ID");
        keys.put("PUBLICATION_AUTHORS", "PA_ID");
        keys.put("PUBLICATION_CONTENT", "PUBC_ID");
        keys.put("PUBLICATION_SERIES", "PS_ID");
        keys.put("PUBLICATIONS", "PUBLICATION_ID");
        keys.put("PUBLISHER", "PUBLISHER_ID");
        keys.put("REVIEWS", "ID");
        keys.put("TAGS", "TAG_ID");
        keys.put("TITLE", "TITLE_ID");
        keys.put("TITLE_AWARD", "TAW_ID");
        keys.put("TITLE_SERIES", "ID");
        keys.put("TITLE_TAGS", "TAGMAP_ID");
        keys.put("WEB_PAGES", "WB_ID");

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
