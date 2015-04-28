package models;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account extends AbstractAccount
{
	//......................... class fields
	private ObservableList<RegisterEntry> entries = FXCollections.observableArrayList();
	private IntegerProperty noOfEntries = new SimpleIntegerProperty();
	//private final double openingBalance;
	//private final LocalDate openingDate;
	private ObjectProperty<AccountGroup> parentGroup = new SimpleObjectProperty<AccountGroup>();
	private StringProperty accountCode = new SimpleStringProperty();
	private StringProperty accountDescription = new SimpleStringProperty();
	private StringProperty accountNotes = new SimpleStringProperty();
	private ObjectProperty<ACCOUNT_TYPE> accountType = new SimpleObjectProperty<>(); // type of account
	
	// constructors     .............................................................................
        public Account()
        {
                super();
        }
               
		
        public Account(long accountID, String accountName)
        {
            super(accountID,accountName);
        }
	//.................................................................................................
		
	
	//.......................set methods......................
	public void setAccountCode(String code){this.accountCode.setValue(code);}
	public void setAccountDescription(String desc){this.accountDescription.setValue(desc);}
	public void setAccountNotes(String notes){this.accountNotes.setValue(notes);}
        public void setParentGroup(AccountGroup group)
        {	this.parentGroup.setValue(group);
            //group.getChildAccounts().add(this);
            updateFullName();
        }
        public void setAccountType(ACCOUNT_TYPE type) {this.accountType.setValue(type);;}
    
    //...........................get methods..............................
	public String getAccountCode(){return accountCode.getValue();}
	public String getAccountDescription(){return accountDescription.getValue();}
	public String getAccountNotes(){return accountNotes.getValue();}
	public AccountGroup getParentGroup()
        {	return parentGroup.getValue(); }
	public ObservableList<RegisterEntry> getRegisterEntries()
        {
    	return entries;
         }
	public ACCOUNT_TYPE getAccountType() {return accountType.getValue(); }
	
	public String updateFullName()
	{
		String fullName = parentGroup.getValue().updateFullName()+" : "+this.getAccountName();
		this.fullName.setValue(fullName);
		return fullName;
	}
	
	//...............................property methods..........
	public StringProperty accountCodeProperty(){return accountCode;}
	public StringProperty accountDescriptionProperty(){return accountDescription;}
	public StringProperty accountNotesProperty(){return accountNotes;}
	public ObjectProperty<AccountGroup> parentGroupProperty()
        {	return parentGroup; }
	public IntegerProperty noOfEntriesProperty() {return noOfEntries;}
	public ObjectProperty<ACCOUNT_TYPE> accountTypeProperty() {return accountType; }
	
        // >>>>>>>>>>>>> ADD A NEW REGISTER ENTRY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        public void addEntry(Transaction t)
        {
            // insert into entry ArrayList at appropriate position
            int insertionIndex;
            for(insertionIndex = entries.size()-1;insertionIndex>=0;insertionIndex--)
            {
                LocalDate tDate = t.getTransactionDate();
                LocalDate indexDate = entries.get(insertionIndex).getTransaction().getTransactionDate();
                if(tDate.compareTo(indexDate) >0)
                    break;
            }
            insertionIndex++;
            RegisterEntry rEntry = null;
            if(t.getFromAC() == this)
                rEntry = new RegisterEntry(t, RegisterEntryType.DEBIT,t.getToAC());
            else if(t.getToAC() == this)
                rEntry = new RegisterEntry(t, RegisterEntryType.CREDIT,t.getFromAC());
            
            entries.add(insertionIndex, rEntry);
            noOfEntries.setValue(noOfEntries.getValue() + 1);
            
            // update this accounts balance property and parent group balance
            if(t.getFromAC() == this)
            {
            super.accountBalanceProperty().setValue(super.accountBalanceProperty().get() - t.getAmount());
            this.parentGroup.getValue().updateBalance();

            }
            else if(t.getToAC() == this)
            { 
            accountBalanceProperty().set(accountBalanceProperty().get() + t.getAmount()); 
            this.parentGroup.getValue().updateBalance();
            }
            
            // update serial no's and entry balances
            for(int i=insertionIndex;i<entries.size();i++)
            {
                if(i==0)
                { 
                    entries.get(i).setSerial(1);
                    if(entries.get(i).getEntryType() == RegisterEntryType.CREDIT)
                        entries.get(i).setBalance(entries.get(i).getTransaction().getAmount());
                    else if(entries.get(i).getEntryType() == RegisterEntryType.DEBIT)
                        entries.get(i).setBalance(0 - entries.get(i).getTransaction().getAmount());      
                    continue;
                }
                if(entries.get(i).getEntryType() == RegisterEntryType.CREDIT)
                {
                    entries.get(i).setSerial(entries.get(i-1).getSerial() + 1);
                    entries.get(i).setBalance(entries.get(i-1).getBalance() + entries.get(i).getTransaction().getAmount());
                }
                else if(entries.get(i).getEntryType() == RegisterEntryType.DEBIT)
                {
                    entries.get(i).setSerial(entries.get(i-1).getSerial() + 1);
                    entries.get(i).setBalance(entries.get(i-1).getBalance() - entries.get(i).getTransaction().getAmount());
                }
            }
            
        }
   
    //.....................remove a transaction............................
    
     public void removeEntry(Transaction t)
        {
            // insert into entry ArrayList at appropriate position
            int removalIndex;
            for(removalIndex = 0;removalIndex<entries.size();removalIndex++)
            {
                if(entries.get(removalIndex).getTransaction() == t)
                    break;
            }
           
            entries.remove(removalIndex);
            noOfEntries.setValue(noOfEntries.getValue() - 1);
            
            // update this accounts balance property and parent group balance
            if(t.getFromAC() == this)
            {
            super.accountBalanceProperty().setValue(super.accountBalanceProperty().get() + t.getAmount());
            this.parentGroup.getValue().updateBalance();

            }
            else if(t.getToAC() == this)
            { 
            accountBalanceProperty().set(accountBalanceProperty().get() - t.getAmount()); 
            this.parentGroup.getValue().updateBalance();
            }
            
            // update serial no's and entry balances
            for(int i=removalIndex;i<entries.size();i++)
            {
                if(i==0)
                { 
                    entries.get(i).setSerial(1);
                    if(entries.get(i).getEntryType() == RegisterEntryType.CREDIT)
                        entries.get(i).setBalance(entries.get(i).getTransaction().getAmount());
                    else if(entries.get(i).getEntryType() == RegisterEntryType.DEBIT)
                        entries.get(i).setBalance(0 - entries.get(i).getTransaction().getAmount());      
                    continue;
                }
                if(entries.get(i).getEntryType() == RegisterEntryType.CREDIT)
                {
                    entries.get(i).setSerial(entries.get(i-1).getSerial() + 1);
                    entries.get(i).setBalance(entries.get(i-1).getBalance() + entries.get(i).getTransaction().getAmount());
                }
                else if(entries.get(i).getEntryType() == RegisterEntryType.DEBIT)
                {
                    entries.get(i).setSerial(entries.get(i-1).getSerial() + 1);
                    entries.get(i).setBalance(entries.get(i-1).getBalance() - entries.get(i).getTransaction().getAmount());
                }
            }
            
        }
     public double getBalanceAtDate(LocalDate date)
     {
         double bal = 0.0;
         for(RegisterEntry entry : entries)
         {
            if(date.compareTo(entry.getTransaction().getTransactionDate()) <0)
                break;
            bal = entry.getBalance();
         }
        return bal;
     }
    
    public void printAccountStatusToConsole()
    {
    	System.out.println(".....................................................");
    	System.out.println("REAL ACCOUNT");
    	System.out.println("Account Name                   :"+accountNameProperty().get());
    	System.out.println("Account Balance                :"+accountBalanceProperty().get());
    	System.out.println("No. of Entries                 :"+noOfEntries.get());
    	System.out.println("Size of Transaction List       :"+entries.size());
        System.out.println("REMOVABLE                      :"+isRemovable());
    	
    }
}
