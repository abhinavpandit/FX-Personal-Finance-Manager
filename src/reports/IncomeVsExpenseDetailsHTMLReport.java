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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;
import models.RegisterEntry;
import models.RegisterEntryType;
import models.Transaction;

/**
 *
 * @author HP
 */
public class IncomeVsExpenseDetailsHTMLReport
{
    private DataModel dataModel;
    private Account forAccount;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BufferedWriter tempFileBufferedWriter;

    private double incomesTotal = 0.0;
    private double expensesTotal = 0.0;
    private ArrayList<String> incomesArray = new ArrayList<String>(10);
    private ArrayList<String> expensesArray = new ArrayList<String>(10) ;
    private VBox optionsPane;
    private LocalDate toDate = LocalDate.now();
    private LocalDate fromDate = LocalDate.now().minusMonths(1);
    
    private DatePicker fromDatePicker,toDatePicker;
    
    public IncomeVsExpenseDetailsHTMLReport(DataModel dataModel) throws IOException 
    {
        this.dataModel = dataModel;
        
        file = new File("tempReport.html");
        if(!file.exists())
            file.createNewFile();
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        setUpOptionsPanel();
       
        
        
        
    }
    
    
    public File generateReport() throws IOException
    {
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();
        if(fromDate.compareTo(toDate) > 0)
            return null;
        InputStream resourceAsStream  = this.getClass().getResourceAsStream("res/IncomeVsExpense_Details_template.html");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader templateFileBufferedReader = new BufferedReader(inputStreamReader);
        
        FileWriter tempFileWriter = new FileWriter(file);
        tempFileBufferedWriter = new BufferedWriter(tempFileWriter);
        tempFileBufferedWriter.flush();
        
        generateExpenseDetails();
        generateIncomeDetails();
        fillGaps();
        int i=0;
        while(true)
        {
            String s = templateFileBufferedReader.readLine();
            if(s==null)
            {
                System.out.println("EOF encountered -> breaking");
                break;
            }
            System.out.println("writing to file : "+s);
            tempFileBufferedWriter.write(s);
            if(s.trim().startsWith("<!--datamarker_a-->"))
            {
                System.out.println("IncomeVsExpenseSummaryHTMLReport:  data marker encountered");
                for(int iter=0;iter<incomesArray.size();iter++)
                {
                    tempFileBufferedWriter.write("\n<tr> "+incomesArray.get(iter) +expensesArray.get(iter) +"</tr>\n");
                }
            }
            
            
            
        }
            
        
        tempFileBufferedWriter.flush();
        
        return  file;
    }
    
    private void generateIncomeDetails()
    {
        for(Transaction t : dataModel.getTransactionList())
        {
            if(t.getFromAC().getAccountType() == ACCOUNT_TYPE.INCOME)
            {
                String str ="<td> " +t.getTransactionDate().format(dateFormat) +"</td>"
                            +"<td>" +t.getMemo() +"</td>"
                            +"<td> Rs. " +t.getAmount() +"</td>";
                incomesArray.add(str);
                incomesTotal = incomesTotal + t.getAmount();
                        
            }
        }
    }
    
    private void generateExpenseDetails() throws IOException
    {
        for(Transaction t : dataModel.getTransactionList())
        {
            if(t.getToAC().getAccountType() == ACCOUNT_TYPE.EXPENSE)
            {
                String str ="<td> " +t.getTransactionDate().format(dateFormat) +"</td>"
                            +"<td>" +t.getMemo() +"</td>"
                            +"<td> Rs. " +t.getAmount() +"</td>";
                expensesArray.add(str);
                expensesTotal = expensesTotal + t.getAmount();
                        
            }
        }
        
    }
    private void fillGaps()
    {
        while(incomesArray.size() != expensesArray.size())
        {
            if(incomesArray.size() < expensesArray.size())
            {
                incomesArray.add("<td>"  +"</td><td></td>"
                        +"<td> " +"</td>");
            }
            else if(incomesArray.size() > expensesArray.size())
            {
                expensesArray.add("<td>"  +"</td><td></td>"
                        +"<td> " +"</td>");
            }
            else
                break;
        }
        incomesArray.add("<td></td><td><b> TOTAL INCOME </b></td><td> <b>Rs. " +incomesTotal +"</b></td>");
        expensesArray.add("<td></td><td><b> TOTAL EXPENSES </b></td><td> <b>Rs. " +expensesTotal +"</b></td>");
            
    }
    public static void main(String [] args)
    {
        
    }
     private void setUpOptionsPanel()
    {
        optionsPane = new VBox(10);
       
        Text fromDateText = new Text("From Date");
        Text toDateText = new Text("To Date");
        fromDatePicker = new DatePicker(LocalDate.now().minusYears(1));
        toDatePicker = new DatePicker(LocalDate.now());
        optionsPane.getChildren().addAll(fromDateText,fromDatePicker,toDateText,toDatePicker);
        
    }
    public Pane getOptionsPanel()
    {
        return optionsPane;
    }
    
    
    
}
