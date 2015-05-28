/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Account;
import models.DataModel;
import models.RegisterEntry;
import models.RegisterEntryType;

/**
 *
 * @author HP
 */
public class AccountStatementHTMLReport
{
    private DataModel dataModel;
    private Account forAccount;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BufferedWriter tempFileBufferedWriter;
    private LocalDate toDate = LocalDate.now();
    private LocalDate fromDate = LocalDate.now().minusMonths(1);
    
    private DatePicker fromDatePicker,toDatePicker;
    private VBox optionsPane;
    private ChoiceBox<Account> forAccountChoiceBox;
    
    
    public AccountStatementHTMLReport(DataModel dataModel) throws IOException 
    {
        this.dataModel = dataModel;
        file = new File("tempReport.html");
        if(!file.exists())
            file.createNewFile();
        setUpOptionsPanel();
        
    }
    
    
    public File generateReport() throws IOException
    {
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();
        forAccount = forAccountChoiceBox.getSelectionModel().getSelectedItem();
        if(fromDate.compareTo(toDate) > 0)
            return null;
        InputStream resourceAsStream  = this.getClass().getResourceAsStream("res/test.html");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader templateFileBufferedReader = new BufferedReader(inputStreamReader);
        
        FileWriter tempFileWriter = new FileWriter(file);
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
                writeAccountDetails(forAccount);
            }
            if(s.trim().startsWith("<!--datamarker_b-->"))
            {
                System.out.println("BalanceSheetHTMLReport: data marker encountered");
                writeAccountStatement(forAccount);
            }
            System.out.println("writing to file : "+s);
            tempFileBufferedWriter.write(s);
            
            
            
        }
        tempFileBufferedWriter.flush();
        
        return  file;
    }
    
    private void writeAccountDetails(Account forAccount) throws IOException
    {
        
        String str = "<td>" +forAccount.getAccountName() +"</td>"
                        +"<td>" +forAccount.getAccountType().toString() +"</td>"
                        +"<td>" +forAccount.getRegisterEntries().size() +" </td>"
                        +"<td>" +fromDate.format(dateFormat) +" TO " +toDate.format(dateFormat) +"</td>"
                        +"<td>" +forAccount.getAccountBalance() +" </td>"
                        + "</tr>";
                tempFileBufferedWriter.write(str);
       
    }
    
    private void writeAccountStatement(Account a) throws IOException
    {
        for(RegisterEntry entry : a.getRegisterEntries())
        {
            if(entry.getTransaction().getTransactionDate().compareTo(fromDate) <0 || entry.getTransaction().getTransactionDate().compareTo(toDate) > 0)
                continue;
            tempFileBufferedWriter.write(getHTMLStatementRow(entry));
        }
        
    }
    private String getHTMLStatementRow(RegisterEntry re)
    {
        String date,payee="",account,memo,debit,credit,balance;
        date = re.getTransaction().getTransactionDate().format(dateFormat);
        if(re.getTransaction().getPayee() !=null)
            payee = re.getTransaction().getPayee().getName();
        if(re.getTransaction().getFromAC() == forAccount)
            account = re.getTransaction().getToAC().getAccountName();
        else
            account = re.getTransaction().getFromAC().getAccountName();
        memo = re.getTransaction().getMemo();
        if(re.getEntryType() == RegisterEntryType.CREDIT)
        {
            debit ="";
            credit = "" +re.getTransaction().getAmount();
        }
        else
        {
            credit ="";
            debit = "" +re.getTransaction().getAmount();
        }
        balance = "" +re.getBalance();
        
        String str = "<tr>" +
                        "<td>" +date +"</td>\n" +
                        "<td>" +payee +"</td>\n" +
                        "<td>" +account +" </td>\n" +
                        "<td>" +memo +"</td>\n" +
                        "<td>" +debit +"</td>\n" +
                        "<td>" +credit +"</td>\n" +
                        "<td>" +balance +"</td>\n" +
                        "</tr>";
        return str;
    }
    private void setUpOptionsPanel()
    {
        optionsPane = new VBox(10);
        Text forAccountText = new Text ("Account");
        forAccountChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        forAccountChoiceBox.getSelectionModel().clearAndSelect(1);
        Text fromDateText = new Text("From Date");
        Text toDateText = new Text("To Date");
        fromDatePicker = new DatePicker(LocalDate.now().minusYears(1));
        toDatePicker = new DatePicker(LocalDate.now());
        optionsPane.getChildren().addAll(forAccountText,forAccountChoiceBox,fromDateText,fromDatePicker,toDateText,toDatePicker);
        
    }
    public Pane getOptionsPanel()
    {
        return optionsPane;
    }
    public static void main(String [] args)
    {
        
    }
    
    
}
