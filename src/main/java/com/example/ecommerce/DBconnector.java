package com.example.ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBconnector {
    private final String url = "jdbc:mysql//localhost:3306/ecom";
    private final String db = "ecom";
    private final String username = "root";
    private final String password = "7044348131@as";

    public DBconnector() {
    }

    private Statement getStatement() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ecom?user=root&password=7044348131@as");
            return connection.createStatement();
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public ResultSet getQueryTable(String query) {
        try {
            Statement statement = this.getStatement();
            return statement.executeQuery(query);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public int execute(String query) {
        try {
            Statement statement = this.getStatement();
            return statement.executeUpdate(query);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        DBconnector conn = new DBconnector();
        ResultSet res = conn.getQueryTable("show tables");
        if (res != null) {
            System.out.println(res);
        } else {
            System.out.println("Connection failed");
        }

    }
}

