package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccountGroup extends AbstractAccount 
{
	private ObservableList<AbstractAccount> childAccounts = FXCollections.observableArrayList();
	private ObjectProperty<AccountGroup> parentGroup = new SimpleObjectProperty<>();

	public void setParentGroup(AccountGroup parent)
	{
		this.parentGroup.setValue(parent);
		//parent.getChildAccounts().add(this);
	}
	public AccountGroup getParentGroup()
	{
		return this.parentGroup.getValue();
	}
	public ObjectProperty<AccountGroup> parentGroupProperty()
	{
		return this.parentGroup;
	}
	public AccountGroup()
	{
		super();
	}

	public AccountGroup(long accountID, String accountName)
	{
		super(accountID, accountName);
	}
	
	public String updateFullName()
	{
            if(this.parentGroup == null)
            {
                    this.fullName.setValue("");
                    return "";
            }
            else
            {
            String fullName="";
            try
            {
                    if(this.parentGroup.getValue().parentGroupProperty().getValue() == null)
                            fullName = this.getAccountName();
                    else
                            fullName = this.parentGroup.getValue().updateFullName() +" : "+this.getAccountName();
            this.fullName.setValue(fullName);

            }
            catch(NullPointerException ne)
            {
                    System.out.println(this.parentGroup);
                    System.out.println(this.parentGroup.getValue());
                    System.out.println(this.getAccountName());
            }
            return fullName;
        }		
		
	}
	public void addChildAccount(AbstractAccount account)
	{
		childAccounts.add(account);
		
	}
	public void removeChildAccount(AbstractAccount account)
	{
		if(childAccounts.contains(account))
			childAccounts.remove(account);
		
	}
	public ObservableList<AbstractAccount> getChildAccounts() {return childAccounts;}
	public void updateBalance()
	{
            //System.out.println("update balance called on : "+this.getAccountName());
            double balance = 0.0;
            for(AbstractAccount account : childAccounts)
            {
                    balance = balance + account.getAccountBalance();
                    this.accountBalanceProperty().setValue(balance);
            }
            if(this.parentGroup.getValue() != null)
                this.getParentGroup().updateBalance();
	}
	//public void addToBalance(double amount) {this.accountBalanceProperty().setValue(this.accountBalanceProperty().getValue() + amount);}
	//public void subtractFromBalance(double amount) {this.accountBalanceProperty().setValue(this.accountBalanceProperty().getValue() - amount);}
	 public void printAccountStatusToConsole()
	    {
	    	System.out.println(".....................................................");
	    	System.out.println(" ACCOUNT CATEGORY");
	    	System.out.println("Account Name                   :"+accountNameProperty().get());
	    	System.out.println("Account Balance                :"+accountBalanceProperty().get());
                System.out.println("REMOVABLE                      :"+isRemovable());
	    	System.out.print("Child Accounts : { ");
	    	for(AbstractAccount account : childAccounts)
	    		System.out.print(" "+account.getAccountName()+", ");
	    	System.out.println(" }");
	    }
}
