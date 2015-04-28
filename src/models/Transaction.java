
package models;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;




public class Transaction 
{
    private final long transactionID;
    private ObjectProperty<LocalDate> transactionDate = new SimpleObjectProperty<>();
    private StringProperty transactionNum = new SimpleStringProperty();
    private ObjectProperty<Payee> payee = new SimpleObjectProperty<>();
    private DoubleProperty amount = new SimpleDoubleProperty();
    private ObjectProperty<Account> fromAC = new SimpleObjectProperty<>();
    private ObjectProperty<Account> toAC = new SimpleObjectProperty<>();
    private StringProperty memo = new SimpleStringProperty();
    
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   

   // constructors
    
    public Transaction(long transactionID)
    {
        this.transactionID = transactionID;
        this.transactionNum.set("");
        this.amount.set(0);
        this.memo.set("");
    }
    
    //............................... get methods...............................
    public long getTransactionID()  {   return transactionID;   }
    public LocalDate getTransactionDate() {  return transactionDate.get(); }
    public String getTransactionNum() {return transactionNum.get(); }
    public Payee getPayee() {return payee.get(); }
    public Account getFromAC() {   return fromAC.get();  }
    public Account getToAC() {   return toAC.get();    }
    public String getMemo() {   return memo.get();  }
    public double getAmount(){   return amount.get();  }
    //..........................................................................
    
    //...........................set methods....................................
    public void setTransactionDate(LocalDate date) {transactionDate.set(date);}
    public void setTransactionNum(String num) {transactionNum.set(num);}
    public void setPayee(Payee p) {this.payee.set(p);}
    public void setFromAC(Account from){ fromAC.set(from); }
    public void setToAC(Account to) {toAC.set(to);}
    public void setMemo(String description) {this.memo.set(description);  }
    public void setAmount(double amount){this.amount.set(amount); }
    //..........................................................................
    
    //.............................property methods............................
    public ObjectProperty<LocalDate> transactionDateProperty() {return transactionDate; }
    public StringProperty transactionNumProperty() {return transactionNum; }
    public ObjectProperty<Payee> payeeProperty() {return payee; }
    public ObjectProperty<Account> fromACproperty() {return fromAC;}
    public ObjectProperty<Account> toACproperty() {return toAC; }
    public StringProperty memoProperty() {return memo;}
    public DoubleProperty amountProperty() {return amount;}
    //.........................................................................
    
    //..... enter the transaction via console...................................
    public static Transaction enterTransactionViaConsole(AbstractAccount fromAC,AbstractAccount toAC) throws IOException
    {
        BufferedReader consoleIN = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter transaction FROM : " +fromAC.getAccountName() +" TO : "+toAC.getAccountName());
        Transaction t = new Transaction(utilities.UniqueID.get());
        System.out.println("Enter Transaction Date : ");
        t.transactionDate.set(LocalDate.parse(consoleIN.readLine(), dateFormat));
        System.out.println("Enter the amount of the transaction : ");
        t.amount.set(Double.parseDouble(consoleIN.readLine()));
        System.out.println("Enter the description of the transaction : ");
        t.memo.set(consoleIN.readLine());
        
        return t;
    }
    
    //.......... static method to create a transaction.......................
    public static Transaction createTransaction(Account fromAC,Account toAC)
    {
        long tID = utilities.UniqueID.get();
        Transaction t = new Transaction(tID);
        t.fromAC.set(fromAC);
        t.toAC.set(toAC);
        return t;
    }
    
    public static void printTransactionHeader()
    {
        
        System.out.printf("%-15s%-10s%-25s%-25s%-10s%-20s\n","Transaction ID","Date","From AC","To AC","Amount","Description");
    }
    
    public void printTransactionToConsole()
    {
        System.out.println("Transaction ID: "+transactionID);
        System.out.println("Transaction Num : "+transactionNum.getValue());
        if(payee.getValue() != null)
             System.out.println("Payee : "+payee.getName());
        else
             System.out.println("Payee : NULL");
        System.out.println("From Account : "+getFromAC().getAccountName());
        System.out.println("To Account : "+getToAC().getAccountName());
        System.out.println("Memo : "+getMemo());
        System.out.println("Amount : "+getAmount());
        
    }
    
    public static void main(String [] args)
    {
      //  Transaction t1 = new Transaction(55684, new Date(), 1650.50, , null, null);
        
        
       // printTransactionHeader();
       // t1.printTransactionToConsole();
        
    }
     
    
}
