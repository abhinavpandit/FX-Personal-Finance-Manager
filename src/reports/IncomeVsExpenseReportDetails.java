/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;
import models.RegisterEntry;
import models.RegisterEntryType;
import models.Transaction;
import ui.models.TransactionComparatorByDate;

/**
 *
 * @author HP
 */
public class IncomeVsExpenseReportDetails
{
    private DataModel dataModel;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate fromDate,toDate;
    ArrayList<String> incomes =  new ArrayList<>();
    ArrayList<String> expenses = new ArrayList<>();
    public IncomeVsExpenseReportDetails(DataModel dataModel) throws IOException 
    {
        this.dataModel = dataModel;
        file = new File("incomeVsExpenseDetailsReport.txt");
        if(!file.exists())
            file.createNewFile();
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
       // generateReport(dataModel.getAccountList().get(0));
        
    }
    public IncomeVsExpenseReportDetails() throws SQLException, IOException
    {
       dataModel = new DataModel(new SimpleStringProperty());
        file = new File("incomeVsExpenseDetailsReport.txt");
        if(!file.exists())
            file.createNewFile();
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter); 
        fromDate = LocalDate.now().minusYears(1);
        toDate = LocalDate.now();
                
        generateReport();
    }
    public void setDates(LocalDate fromDate , LocalDate toDate)
    {
        if(fromDate.compareTo(toDate)>0)
            return;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    
    public File generateReport() throws IOException
    {
        printHeader();
        generateStringArrays();
        String format = "|%10s%30s%15s|%10s%30s%15s|";
        String formattedAccountHeader = String.format(format,"Date","Description","Amount","Date","Description","Amount");
       bufferedWriter.write(formattedAccountHeader);
       bufferedWriter.newLine();
       bufferedWriter.write("---------------------------------------------------------------------------------------");
       bufferedWriter.newLine();
      
       Iterator<String> incomesIterator = incomes.iterator();
        Iterator<String> expensesIterator = expenses.iterator();
        while(incomesIterator.hasNext() || expensesIterator.hasNext())
        {
            String lhs,rhs;
            if(incomesIterator.hasNext())
                lhs = incomesIterator.next();
            else
                lhs = String.format("%55s","");
            if(expensesIterator.hasNext())
                rhs = expensesIterator.next();
            else
                rhs = String.format("%55s","");
            bufferedWriter.write("|"+lhs+"|"+rhs+"|");
            
            bufferedWriter.newLine();
        }
       
       bufferedWriter.write("---------------------------------------------------------------------------");
       
       printStringArrays();
       bufferedWriter.close();
       return file;
    }
    
    private void printHeader() throws IOException
    {
        System.out.println("writng header to file");
       String header = "INCOME VS EXPENSE REPORT  - DETAILS";
       bufferedWriter.write(header);
       bufferedWriter.newLine();
       
       String periodHeader = "\nFrom : \t"+new Date() +" TO \t" + new Date();
       bufferedWriter.write(periodHeader);
       bufferedWriter.newLine();
       bufferedWriter.newLine();
       
        String h1 = String.format("|%55s|%55s|", "INCOME","EXPENSE");
        bufferedWriter.write(h1);
        bufferedWriter.newLine();
        
        bufferedWriter.write("__________________________________________________________________________");
        bufferedWriter.newLine();
        
        
       
    }
    private void generateStringArrays()
    {
        SortedList<Transaction> sortedList = new SortedList<>(dataModel.getTransactionList(), new TransactionComparatorByDate());
        Iterator<Transaction> iterator = sortedList.iterator();
        while(iterator.hasNext())
        {
           
            Transaction t = iterator.next();
            if(t.getTransactionDate().compareTo(fromDate) <0 || t.getTransactionDate().compareTo(toDate) >0)
                continue;
            if(t.getFromAC().getAccountType() == ACCOUNT_TYPE.INCOME)
            {
                incomes.add(String.format("%10s%30s%15s", t.getTransactionDate().toString(),"By "+t.getMemo(),""+t.getAmount()));
            }
            if(t.getToAC().getAccountType() == ACCOUNT_TYPE.EXPENSE)
            {
                expenses.add(String.format("%10s%30s%15s", t.getTransactionDate().toString(),"By "+t.getMemo(),""+t.getAmount()));
            }
        }
    }
   
    
    private void printStringArrays()
    {
        System.out.println("Printing Income Arrays");
        for(String s : incomes)
            System.out.println(s);
        System.out.println("\nPrinting Expense Arrays");
        for(String s : expenses)
            System.out.println(s);
    }
    
    public static void main(String [] args)
    {
        try {
            new IncomeVsExpenseReportDetails();
        } catch (SQLException ex) {
            Logger.getLogger(IncomeVsExpenseReportDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IncomeVsExpenseReportDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
}
