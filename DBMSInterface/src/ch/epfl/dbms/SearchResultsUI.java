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
    private JLabel followUpSearchInstructionLabel;
    private JList listResults;

    private ArrayList<ArrayList<String>> allResults;
    private String keywords;

    private boolean detailsMode = false;

    private SearchResultsUI(ArrayList<ArrayList<String>> allResults, String keywords) {

        listResults = new JList();

        this.allResults = allResults;
        this.keywords = keywords;

        populateList();
    }

    static void display(ArrayList<ArrayList<String>> allResults, String keywords) {
        JFrame frame = new JFrame("Search results");
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

        if(allResults.size() == 0) {
            listModel.addElement("No results for the given keywords");
        }

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
                        break; // Do not test the other fields of the same row. We only want 1 preview per row.
                    }
                }

            }
        }

        listResults.setModel(listModel);

        scrollPane.setViewportView(listResults);

        System.out.println(listModel.size());
        System.out.println(allResults.size());

        listResults.addListSelectionListener(
                e -> {
                    if (!detailsMode)
                        displayRowInformation(listResults.getSelectedIndex());
                }
        );

    }

    private void displayRowInformation(int rowIndex) {

        detailsMode = true;
        DefaultListModel newListModel = new DefaultListModel();

        ArrayList<String> row = allResults.get(rowIndex);

        newListModel.addElement("Details: ");

        // Blank line
        newListModel.addElement("");

        for(int i = 0; i < row.size(); i++) {
            if(i == 0) {
                newListModel.addElement(row.get(0));
                newListModel.addElement("");
            } else {
                newListModel.addElement(row.get(i));
            }
        }

        // Change the listener so that nothing happens when clicking on the details
        listResults.addListSelectionListener(
                e -> {
                    if(detailsMode) {
                        String selectedValue = listResults.getSelectedValue().toString();
                        System.out.println(selectedValue);
                    }
                }
        );
        listResults.setModel(newListModel);

    }

}
