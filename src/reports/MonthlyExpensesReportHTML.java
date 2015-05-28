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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;
import models.RegisterEntry;
import models.RegisterEntryType;

/**
 *
 * @author HP
 */
public class MonthlyExpensesReportHTML
{
    private DataModel dataModel;
    private Account forAccount;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BufferedWriter tempFileBufferedWriter;
    
    
    private VBox optionsPane;
    private ChoiceBox<Month> fromMonthChoiceBox = new ChoiceBox<Month>(FXCollections.observableArrayList(Month.values()));
    private ChoiceBox<Month> toMonthChoiceBox = new ChoiceBox<Month>(FXCollections.observableArrayList(Month.values()));
    private Spinner<Integer> fromYear = new Spinner<>(1900, 2100, LocalDate.now().getYear()-1, 1);
    private Spinner<Integer> toYear = new Spinner<>(1900, 2100, LocalDate.now().getYear(), 1);
    private Map<String,Double> totalsMap = new HashMap<String, Double>(12);
    public MonthlyExpensesReportHTML(DataModel dataModel) throws IOException 
    {
        this.dataModel = dataModel;
        
        file = new File("tempReport.html");
        if(!file.exists())
            file.createNewFile();
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        setUpOptionsPane();
        fromMonthChoiceBox.getSelectionModel().select(LocalDate.now().getMonth().plus(1));
        toMonthChoiceBox.getSelectionModel().select(LocalDate.now().getMonth());
        
        
    }
    
    
    public File generateReport() throws IOException
    {
        System.out.println("MonthlyExpensesReport : generate() called");
        if(!validateInterval(fromYear.getValue().intValue(),toYear.getValue().intValue(),fromMonthChoiceBox.getSelectionModel().getSelectedItem(),toMonthChoiceBox.getSelectionModel().getSelectedItem()))
        {
          
            System.out.println("MonthlyExpensesReport : Invalid Interval returning null");
            return null;
        }
        InputStream resourceAsStream  = this.getClass().getResourceAsStream("res/MonthlyExpensesSummaryReport.html");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader templateFileBufferedReader = new BufferedReader(inputStreamReader);
        
        FileWriter tempFileWriter = new FileWriter(file);
        tempFileBufferedWriter = new BufferedWriter(tempFileWriter);
        tempFileBufferedWriter.flush();
        setUpTotalsMap();
        
        int i=0;
        while(true)
        {
            String s = templateFileBufferedReader.readLine();
            if(s==null)
                break;
            if(s.trim().startsWith("<!--datamarker_a-->"))
            {
                String str = printMonthsHeader();
                tempFileBufferedWriter.write(str);
                
                for(Account a : dataModel.getAccountList())
                {
                    if(a.getAccountType() == ACCOUNT_TYPE.EXPENSE)
                    {
                       tempFileBufferedWriter.write(getExpenseHTMLString(a));
                    }
                }
                String str2 = printMonthsFooter();
                tempFileBufferedWriter.write(str2);
            }
            tempFileBufferedWriter.write(s);    
        }
        tempFileBufferedWriter.flush();
        
        return  file;
    }
    
    
    
  
 
    public static void main(String [] args)
    {
        
    }
    private Pane getOptionsPane()
    {
     return optionsPane;   
    }

    private void setUpOptionsPane() 
    {
        optionsPane = new VBox(10);
        Text fromMonthText = new Text("From Month");
        Text toMonthText = new Text("To Month");
        
        
        optionsPane.getChildren().add(fromMonthText);
        optionsPane.getChildren().add(fromMonthChoiceBox);
        optionsPane.getChildren().add(fromYear);
        optionsPane.getChildren().add(toMonthText);
        optionsPane.getChildren().add(toMonthChoiceBox);
        optionsPane.getChildren().add(toYear);
        
    }
    public Pane getOptionsPanel()
    {
        return optionsPane;
    }
    private boolean validateInterval(Integer fromYear,Integer toYear, Month fromMonth, Month toMonth)
    {
        if(fromYear > toYear)
        {
            //System.out.println("year " +fromYear +" > " +toYear +" returning ");
            return false;
        }
        else if(fromYear.intValue() ==  toYear.intValue())
        {
            //System.out.println("Condition fY == tY "+fromMonth.getValue() +" > " +toMonth.getValue() +" ; comparing");
            if(fromMonth.getValue() > toMonth.getValue())
            {
                System.out.println("Condition fY == tY "+fromMonth +" > " +toMonth +" ; returning false");
                return false;
            }
        }
        
        return true;
    }

    private String printMonthsHeader() 
    {
        Month iterMonth = fromMonthChoiceBox.getSelectionModel().getSelectedItem();
        int iterYear = fromYear.getValue();
        StringBuilder headerHTML = new StringBuilder("<tr>");
        headerHTML.append("<td> Expenses </td>");
        while(validateInterval(iterYear, toYear.getValue(), iterMonth, toMonthChoiceBox.getSelectionModel().getSelectedItem()))
        {
            System.out.println("MonthlyExpensesReport : ");
            System.out.println("Comparing : "+iterMonth +" "+iterYear +" AND " +toMonthChoiceBox.getSelectionModel().getSelectedItem() +" "+toYear.getValue());
            
            headerHTML.append("<td> " +iterMonth.name() +" "+iterYear +"</td>" );
            iterMonth = iterMonth.plus(1);
            if(iterMonth == Month.JANUARY)
                iterYear++;
        }  
        headerHTML.append("<td> Total </td></tr>");
        return headerHTML.toString();
    }
    private String printMonthsFooter() 
    {
        Month iterMonth = fromMonthChoiceBox.getSelectionModel().getSelectedItem();
        int iterYear = fromYear.getValue();
        StringBuilder headerHTML = new StringBuilder("<tr>");
        headerHTML.append("<td> Total </td>");
        while(validateInterval(iterYear, toYear.getValue(), iterMonth, toMonthChoiceBox.getSelectionModel().getSelectedItem()))
        {
                      
            headerHTML.append("<td> " +totalsMap.get(iterMonth.toString() +iterYear) +"</td>" );
            iterMonth = iterMonth.plus(1);
            if(iterMonth == Month.JANUARY)
                iterYear++;
        }  
        headerHTML.append("<td> Total </td></tr>");
        return headerHTML.toString();
    }
    
    private String getExpenseHTMLString(Account a) 
    {
        Month iterMonth = fromMonthChoiceBox.getSelectionModel().getSelectedItem();
        int iterYear = fromYear.getValue();
        StringBuilder headerHTML = new StringBuilder("<tr>");
        headerHTML.append("<td><b>" +a.getAccountName() +"</b> </td>");
        double expenseAccountTotal = 0.0;
        while(validateInterval(iterYear, toYear.getValue(), iterMonth, toMonthChoiceBox.getSelectionModel().getSelectedItem()))
        {
            double thisMonthTotal = getExpenseTotalForMonth(iterMonth, iterYear, a);
            expenseAccountTotal = expenseAccountTotal + thisMonthTotal;
                    
            headerHTML.append("<td>" +thisMonthTotal +"</td>" );
            iterMonth = iterMonth.plus(1);
            if(iterMonth == Month.JANUARY)
                iterYear++;
        }  
        headerHTML.append("<td>" +expenseAccountTotal +"</td></tr>");
        return headerHTML.toString();
    }
    private double getExpenseTotalForMonth(Month month, int year, Account a)
    {
        double total = 0.0;
        
        for(RegisterEntry re: a.getRegisterEntries())
        {
            if(re.getTransaction().getTransactionDate().getMonth() == month && re.getTransaction().getTransactionDate().getYear() == year)
            {
                total = total + re.getTransaction().getAmount();
            }
        }
        Double get = totalsMap.get(month.toString() +year);
        double newTotal = get + total;
        totalsMap.put(month.toString() +year, newTotal);
        return total;
    }
    private void setUpTotalsMap()
    {
        Month iterMonth = fromMonthChoiceBox.getSelectionModel().getSelectedItem();
        int iterYear = fromYear.getValue();
        
        while(validateInterval(iterYear, toYear.getValue(), iterMonth, toMonthChoiceBox.getSelectionModel().getSelectedItem()))
        {
           totalsMap.put(iterMonth.toString() +iterYear, 0.0);
            iterMonth = iterMonth.plus(1);
            if(iterMonth == Month.JANUARY)
                iterYear++;
        }  
    }
    
    
}
