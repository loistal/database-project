package ch.epfl.dbms;

/**
 * Created by titoy on 6/3/16.
 */
public class SearchPreview {

    private String preview;
    private int rowNumber;
    private int tableIndex;

    public SearchPreview(String preview, int rowNumber, int tableIndex) {
        this.preview = preview;
        this.rowNumber = rowNumber;
        this.tableIndex = tableIndex;
    }

    public String getPreview(){
        return preview;
    }

    public int getRowNumber(){
        return rowNumber;
    }

    public int getTableIndex(){
        return tableIndex;
    }

}
