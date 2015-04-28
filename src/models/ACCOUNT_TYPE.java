package models;

public enum ACCOUNT_TYPE
{
    BANK(100),
    INCOME(200),
    EXPENSE(300),
    ASSET(400),
    LIABILITY(500),
    EQUITY(600),
    CASH(700),
    OTHER(800);
   
    
   private int code;

    ACCOUNT_TYPE(int code) 
    {
        this.code = code;
    }
    
    public int getCode()
    {
        return code;
    }
    public static ACCOUNT_TYPE getType(int code)
    {
        if(code == 100)
            return ACCOUNT_TYPE.BANK;
        else if(code == 200)
            return ACCOUNT_TYPE.INCOME;
        else if(code == 300)
            return ACCOUNT_TYPE.EXPENSE;
        else if(code == 400)
            return ACCOUNT_TYPE.ASSET;
        else if(code == 500)
            return ACCOUNT_TYPE.LIABILITY;
        else if(code == 600)
            return ACCOUNT_TYPE.EQUITY;
        else if(code == 700)
            return ACCOUNT_TYPE.CASH;
        else
            return ACCOUNT_TYPE.OTHER;
    }
   
    public static String creditLabel(ACCOUNT_TYPE at)
    {
        String label;
        switch(at)
        {
        case CASH:
        	label = "RECEIVE";
        	break;
        case BANK:
        	label = "DEPOSIT";
        	break;
        case ASSET:
        	label = "INCREASE";
        	break;
        case EXPENSE:
        	label = "EXPENSE";
        	break;
        case INCOME:
        	label = "CHARGE";
        	break;
        case LIABILITY:
        	label = "PAYMENT";
        	break;
        default:
        	label ="CREDIT";
        }
        return label;
        
    }
    
    public static String debitLabel(ACCOUNT_TYPE at)
    {
        String label;
        switch(at)
        {
        case CASH:
        	label = "SPEND";
        	break;
        case BANK:
        	label = "WITHDRAWAL";
        	break;
        case ASSET:
        	label = "DECREASE";
        	break;
        case EXPENSE:
        	label = "REBATE";
        	break;
        case INCOME:
        	label = "INCOME";
        	break;
        case LIABILITY:
        	label = "CHARGE";
        	break;
        default:
        	label ="DEBIT";
        }
        return label;
        
    }
}
