package com.example.fxandmysql;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(String Url, String username,String password) {

        String databaseUser = username;
        String databasePassword = password;
        String url=Url;
        //String url = "jdbc:mysql://localhost:3306/kullanicilar";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}

