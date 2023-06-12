package com.example.uasproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    public static Connection databaseLink;
    public static Connection getConnection(){
        String databaseName = "cinema";
        String databaseUser = "root";
        String databasePass = "";

        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
//            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePass);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
