/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.models;

import java.util.Comparator;
import models.Transaction;

/**
 *
 * @author john
 */
public class TransactionComparatorByDate implements Comparator<Transaction>
{

    @Override
    public int compare(Transaction o1, Transaction o2) 
    {
        return o1.getTransactionDate().compareTo(o2.getTransactionDate());
    }
    
}
