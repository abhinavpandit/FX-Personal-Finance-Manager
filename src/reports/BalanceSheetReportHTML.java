/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;

/**
 *
 * @author HP
 */
public class BalanceSheetReportHTML 
{
    private File templateFile;
    private final DataModel dataModel;
    private File tempReportFile;
    
    BufferedWriter tempFileBufferedWriter;

    public BalanceSheetReportHTML(DataModel dataModel) throws FileNotFoundException, IOException 
    {
        this.dataModel = dataModel;
        
        InputStream resourceAsStream = this.getClass().getResourceAsStream("res/bst.html");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader templateFileBufferedReader = new BufferedReader(inputStreamReader);
        
        
        tempReportFile = new File("tempreport.html");
        if(!tempReportFile.exists())
            tempReportFile.createNewFile();
        
        FileWriter tempFileWriter = new FileWriter(tempReportFile);
        tempFileBufferedWriter = new BufferedWriter(tempFileWriter);
        tempFileBufferedWriter.flush();
        
        
        
        int i=0;
        while(true)
        {
            String s = templateFileBufferedReader.readLine();
            if(s==null)
                break;
            if(s.trim().startsWith("<!--datamarker_a-->"))
            {
                System.out.println("BalanceSheetHTMLReport: data marker encountered");
                writeAssetTable();
            }
            if(s.trim().startsWith("<!--datamarker_l-->"))
            {
                System.out.println("BalanceSheetHTMLReport: data marker encountered");
                writeLiabilityTable();
            }
            System.out.println("writing to file : "+s);
            tempFileBufferedWriter.write(s);
            
            
            
        }
        tempFileBufferedWriter.flush();
               
    }
    
    private void writeAssetTable() throws IOException
    {
        for(Account account : dataModel.getAccountList())
        {
            ACCOUNT_TYPE accountType = account.getAccountType();
            if(accountType == ACCOUNT_TYPE.ASSET || accountType == ACCOUNT_TYPE.BANK || accountType == ACCOUNT_TYPE.CASH)
            {
                String str = "<tr>"
                        + "<td>" +account.getAccountName() +"</td>"
                        +"<td>" +account.getAccountBalance() +"</td>"
                        +"<td> </td>"
                        +"<td> </td>"
                        + "</tr>";
                tempFileBufferedWriter.write(str);
            }
        }
    }
    private void writeLiabilityTable() throws IOException
    {
        for(Account account : dataModel.getAccountList())
        {
            ACCOUNT_TYPE accountType = account.getAccountType();
            if(accountType == ACCOUNT_TYPE.LIABILITY)
            {
                String str = "<tr>"
                        +"<td> </td>"
                        +"<td> </td>"
                        + "<td>" +account.getAccountName() +"</td>"
                        +"<td>" +account.getAccountBalance() +"</td>"
                        
                        + "</tr>";
                tempFileBufferedWriter.write(str);
            }
        }
    }
    
    public File getReportFile()
    {
        return tempReportFile;
    }
    
    public static void main(String [] args) throws SQLException, IOException
    {
        new BalanceSheetReportHTML(new DataModel(new SimpleStringProperty()));
    }
    
    
    
}
