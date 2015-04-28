/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author HP
 */
public class RegisterEntry
{
    private final Transaction transaction;
    private IntegerProperty serial = new SimpleIntegerProperty();
    private DoubleProperty balance = new SimpleDoubleProperty();
    private final RegisterEntryType entryType;
    private final Account transferAccount;

    public RegisterEntry(Transaction transaction, RegisterEntryType type,Account trAccount) 
    {
        this.transaction = transaction;
        this.entryType= type;
        this.transferAccount = trAccount;
    }
    
    //....settter methods
    public void setSerial(int serial)
    {this.serial.setValue(serial); }
    public void incrementSerial()
    {this.serial.setValue(this.serial.getValue() + 1);}
    public void decrementSerial()
    {this.serial.setValue(this.serial.getValue() - 1);}
    public void setBalance(double bal)
    {this.balance.setValue(bal);}
    
    //...gettter methods
    public int getSerial()
    {return serial.getValue();}
    public double getBalance()
    {return balance.getValue();}
    public Transaction getTransaction()
    {return transaction;}
    public RegisterEntryType getEntryType()
    {return entryType;}
    public Account getTransferAccount()
    {return transferAccount;    }
    
    //... properties
    public IntegerProperty serialProperty()
    {return serial;}
    public DoubleProperty balanceProperty()
    {return balance;}
    
    
    
    
    
}
