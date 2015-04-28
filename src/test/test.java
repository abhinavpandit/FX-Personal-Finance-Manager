/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class test 
{
    public static void main(String [] args) throws SQLException
    {
        String path = System.getProperty("user.dir");
        String dbPath = "jdbc:h2:" +path +"\\" +"database";
        System.out.println("db path is : "+dbPath);
        Connection connection = DriverManager.getConnection(dbPath);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ACCOUNTGROUP");
        while(rs.next())
        {
            System.out.println("record :");
            System.out.println(rs.getLong(1));
            System.out.println(rs.getString(2));
                    
        }
    }
    
}
