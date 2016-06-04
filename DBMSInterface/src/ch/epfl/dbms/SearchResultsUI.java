package ch.epfl.dbms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by titoy on 6/3/16.
 */
public class SearchResultsUI {
    private JPanel mPanel;
    private JScrollPane scrollPane;
    private JList listResults;

    private ArrayList<ArrayList<String>> allResults;
    private String keywords;

    private SearchResultsUI(ArrayList<ArrayList<String>> allResults, String keywords) {

        listResults = new JList();

        this.allResults = allResults;
        this.keywords = keywords;

        populateList();
    }

    static void display(ArrayList<ArrayList<String>> allResults, String keywords) {
        JFrame frame = new JFrame("Search previews");
        SearchResultsUI searchResultsUI = new SearchResultsUI(allResults, keywords);
        frame.setContentPane(searchResultsUI.mPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(720, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void populateList() {
        DefaultListModel listModel = new DefaultListModel();

        for(int i = 0; i < allResults.size(); i++) {
            for(int j = 0; j < allResults.get(i).size(); j++) {

                String field = allResults.get(i).get(j);

                if(field != null) { // Some fields are null and should not be tested otherwise NullPointerException!
                    if (field.contains(keywords)) {
                        // Shorten the preview in case it is too long
                        if(field.length() >= 40) {
                            field = field.substring(0, 40);
                            field = field.concat(" ...");
                        }
                        listModel.addElement(field);
                    }
                }

            }
        }

        System.out.print("Finished double loop");

        listResults.setModel(listModel);

        scrollPane.setViewportView(listResults);
    }

}
