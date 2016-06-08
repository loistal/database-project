package ch.epfl.dbms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryUI extends JFrame{

    private JPanel mainPanel;
    private JTextField queryField;
    private JLabel queryLabel;
    private JButton executeButton;
    private JTable resultsTable;
    private JScrollPane resultsContainer;
    private JTextField searchBox;
    private JButton searchButton;
    private JSpinner querySpinner;
    private DefaultTableModel table_model;
    private TableRowSorter sorter;
    private SQLProvider sp;
    private Query query = new Query();
    private String[] tableNames = query.getDescription();
    private String[] queries = query.getQueries();
    private ChooseUI chooseUI;

    public QueryUI() {
        sp = new SQLProvider();
        setSpinner();
        executeButton.addActionListener(actionEvent -> {
            String choice = (String) querySpinner.getValue();
            String query = "";

            switch(choice) {
                case "Number of Publications per year":
                    query = queries[0];
                    break;

                case "Ten authors with most publications":
                    query = queries[1];
                    break;

                case "Names of the youngest and oldest authors to publish something in 2010":
                    query = queries[2];
                    break;


                case "Comics with publications with less than 50 pages":
                    query = queries[3];
                    break;


                case "Comics with publications with less than 100 pages":
                    query = queries[4];
                    break;

                case "Comics with publications with more (or equal) than 100 pages":
                    query = queries[5];
                    break;


                case "Average price of Author's published novels (the ones that have a dollar price":
                    query = queries[6];
                    break;


                case "Name of the author with the highest number of titles tagged as science fiction":
                    query = queries[7];
                    break;

                case "Three most popular titles":
                    query = queries[8];
                    break;

                case "Average price per currency of the publications of the most popular title":
                    query = queries[9];
                    break;


                case "Names of the top ten title series with most awards":
                    query = queries[10];
                    break;


                case "Name of the author who has received the most awards after his/her death":
                    query = queries[11];
                    break;


                case "Three publishers that published the most publications for a given year":
                    chooseUI = new ChooseUI(true);
                    String year = chooseUI.getValue();
                    query = "SELECT *\n" +
                            "FROM (\n" +
                            "  SELECT Publisher.PUBLISHER_ID, COUNT(Publications.publication_id) as number_publications\n" +
                            "  FROM Publications, Publisher\n" +
                            "  WHERE EXTRACT ( YEAR FROM Publications.publication_date ) = "+year+" AND\n" +
                            "        Publications.publisher_id = Publisher.publisher_id\n" +
                            "  GROUP BY Publisher.publisher_id\n" +
                            "  ORDER BY number_publications DESC\n" +
                            ")\n" +
                            "WHERE ROWNUM <= 3";

                    break;



                case "Most reviewed title for a given author":
                    chooseUI = new ChooseUI(false);
                    String name = chooseUI.getValue();
                    query = "SELECT *\n" +
                            "FROM (\n" +
                            "SELECT Title.title, COUNT(Reviews.title_id) as number_reviews\n" +
                            "FROM Title, Reviews, Publications, Publication_authors, Authors, Publication_content\n" +
                            "WHERE Authors.a_name = '"+name+"' AND\n" +
                            "      Reviews.title_id = Title.title_id AND\n" +
                            "      Publication_authors.publication_id = Publications.publication_id AND\n" +
                            "      Publication_authors.AUTHOR_ID = Authors.author_id AND\n" +
                            "      Publication_content.title_id = Title.TITLE_ID AND\n" +
                            "      Publication_content.publication_id = Publications.PUBLICATION_ID\n" +
                            "GROUP BY Title.title\n" +
                            "ORDER BY number_reviews DESC\n" +
                            ") WHERE ROWNUM = 2";
                    break;


                case "Top three title types with most translations for every language":
                    query = queries[14];
                    break;


                case "Average number of authors per publisher for each year":
                    query = queries[15];
                    break;



                case "Publication series with most titles that have been given awards of “World Fantasy Award” type":
                    query = queries[16];
                    break;



                case "Names of the three most awarded authors for every award category":
                    query = queries[17];
                    break;



                case "Names of all living authors that have published at least one anthology from youngest to oldest.":
                    query = queries[18];
                    break;


                case "Author who has reviewed the most titles.":
                    query = queries[19];
                    break;


                case "Three authors with the most translated titles of “novel” type for every language":
                    query = queries[20];
                    break;


                case "Top ten authors whose publications have  the largest pages per dollar ratio":
                    query = queries[21];
                    break;


                case "Top 10 publications that have been awarded the Nebula award with the most extensive web presence":
                    query = queries[22];
                    break;



                default:
                    break;


            }

            if (!query.isEmpty()) {
                searchButton.setEnabled(false);
                searchBox.setEnabled(false);
                try {
                    populateResultsTable(sp.query(query));
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

        });
        searchButton.addActionListener(actionEvent -> {
            String search = searchBox.getText();

            if (search.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(search));
            }
        });
    }


    private void setSpinner() {
        SpinnerListModel tableModel = new SpinnerListModel(tableNames);
        querySpinner.setModel(tableModel);
    }

    private void populateResultsTable(ResultSet rs) throws SQLException {
        if (rs == null) return;

        table_model.setNumRows(0);

        int columns = rs.getMetaData().getColumnCount();

        String[] column_names = new String[columns];

        for (int i = 1; i <= columns; i++) {
            column_names[i - 1] = rs.getMetaData().getColumnName(i);
        }

        table_model.setColumnCount(columns);
        table_model.setColumnIdentifiers(column_names);

        while (rs.next()) {
            Object[] row = new Object[columns];
            for (int i = 1; i <= columns; ++i) {
                row[i - 1] = rs.getObject(i);
            }
            table_model.addRow(row);
        }

        searchBox.setEnabled(true);
        searchButton.setEnabled(true);
        System.out.println("DONE");

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

        display();
    }

    public static void display() {
        new Thread(() -> {
            JFrame frame = new JFrame("Queries");
            QueryUI queryUI = new QueryUI();
            frame.setContentPane(queryUI.mainPanel);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }).start();
    }

    private void createUIComponents() {
        table_model = new DefaultTableModel();
        sorter = new TableRowSorter<TableModel>(table_model);
        resultsTable = new JTable(table_model);
        resultsTable.setRowSorter(sorter);
        resultsContainer = new JScrollPane(resultsTable);
    }
}
