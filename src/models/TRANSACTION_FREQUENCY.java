/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author HP
 */
public enum TRANSACTION_FREQUENCY 
{
    ONCE,DAILY,WEEKLY,MONTHLY;
    public static int getIntValue(TRANSACTION_FREQUENCY tf)
    {
        if(tf == ONCE)
            return 1;
        else if(tf == DAILY)
            return 2;
        else if(tf == WEEKLY)
            return 3;
        else if(tf == MONTHLY)
            return 4;
        return -1;
    }
    public static TRANSACTION_FREQUENCY getFromIntValue(int x)
    {
        if(x == 1)
            return ONCE;
        else if(x == 2)
            return DAILY;
        else if(x == 3)
            return WEEKLY;
        else if(x == 4)
            return MONTHLY;
        else
            return null;
    }
    
}
