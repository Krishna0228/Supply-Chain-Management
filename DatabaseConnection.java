package com.example.ecommerce;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    String dburl="jdbc:mysql://localhost:3306/ecomm";
    String userName="root";
    String password="Kala@123";

    public Statement getStatement(){
        try{
            Connection conn=DriverManager.getConnection(dburl,userName,password);
            return conn.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet getQueryTable(String query)
    {

        Statement statement=getStatement();
        try{
            return statement.executeQuery(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUpdate(String query)
    {

        Statement statement=getStatement();
        try{
            statement.executeUpdate(query);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        String query="select * from products";
        DatabaseConnection dbconn=new DatabaseConnection();
        ResultSet rs= dbconn.getQueryTable(query);
        if(rs!=null){
            System.out.println("Connected to Database");
        }
    }
}
