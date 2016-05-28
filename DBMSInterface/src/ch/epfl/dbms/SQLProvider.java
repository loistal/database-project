package ch.epfl.dbms;

import java.sql.*;

public class SQLProvider {
    private static String JDBC_URL = "jdbc:oracle:thin:@diassrv2.epfl.ch:1521:orcldias";
    private static String USERNAME = "DB2016_G23";
    private static String PASSWORD = "DB2016_G23";

    static {
        try {
            DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;

    public SQLProvider() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("Invalid Query");
        }

        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        SQLProvider pr = new SQLProvider();
        ResultSet rs = pr.query("");
        while (rs.next()) {
            System.out.println(rs.getFetchSize());
        }
        pr.close();
    }
}