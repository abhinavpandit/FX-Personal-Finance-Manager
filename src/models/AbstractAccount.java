package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public abstract class AbstractAccount 
{
    private final long accountID;        // long to store account ID once set cannot be changed
    private StringProperty accountName = new SimpleStringProperty();  // name of the account
    private DoubleProperty accountBalance = new SimpleDoubleProperty(); // balance in the account  
    protected StringProperty fullName = new SimpleStringProperty();
    protected final char SAPERATOR = ':';
    private BooleanProperty removable = new SimpleBooleanProperty(true);
    

   
   // .....................constructors.......................................

    public AbstractAccount()
    {
        this.accountID = utilities.UniqueID.get();
        this.accountName.set("Unnamed");
        accountBalance.set(0);
    }

    public AbstractAccount(long accountID,String accountName)
    {
        this.accountID = accountID;
        accountBalance.set(0);
        this.accountName.setValue(accountName);
    }

    
    //..................... get methods........................................

    public long getAccountID()  {   return accountID;   }
    public String getAccountName()  {   return accountName.getValue(); }
    public double getAccountBalance() {   return accountBalance.getValue(); }
    public String getFullName() {return fullName.getValue();}
    public boolean isRemovable(){return removable.getValue(); }



    //........................set methods....................................

    public void setAccountName(String accountName) {   this.accountName.setValue(accountName); }
    public void setAccountBalance(Double balance) {this.accountBalance.setValue(balance);}
    public void setRemovable(Boolean removable) {this.removable.setValue(removable);}
   
    //.......................... properties.................................
    public StringProperty accountNameProperty() {return accountName; }
    public DoubleProperty accountBalanceProperty() { return accountBalance; }
    public StringProperty fullNameProperty() {return fullName;}
    public BooleanProperty removableProperty(){return removable;}
   
    
    public String toString()

    {
       return accountName.get();
    }

        public void printAccountStatusToConsole()
    {
    	System.out.println(".....................................................");
    	System.out.println("ABSTRACT ACCOUNT");
    	System.out.println("Account Name                   :"+accountNameProperty().get());
    	System.out.println("Account Balance                :"+accountBalanceProperty().get());
    
    	
    }

	
	

}
