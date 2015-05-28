/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
    public static void main(String [] args) throws SQLException, IOException
    {
        Desktop desktop = Desktop.getDesktop();
        desktop.print(new File("F:/ap/temp.txt"));
    }
    
}
