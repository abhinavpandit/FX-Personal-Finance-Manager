
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author john
 */
public class Payee 
{
    private final long payeeID;
    private StringProperty name = new SimpleStringProperty();
    
    public Payee(String name)
    {
        this.payeeID = utilities.UniqueID.get();
        this.name.setValue(name);
        
    }
    public Payee(long id)
    {
        this.payeeID = id;
    }
    public Payee(long id,String name)
    {
        this.payeeID = id;
        this.name.setValue(name);
    }
    
    public void setName(String name)
    {	if(name.length()>=3)
    		this.name.setValue(name);
    }
    public String getName() {return name.getValue();}
    public StringProperty nameProperty() {return name;}
    public long getPayeeID() {return payeeID;}
    
    @Override
    public String toString()
    {
        return name.getValue();
    }
}
