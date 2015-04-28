package models;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.GregorianCalendar;
import models.Transaction;

public class ScheduledTransaction extends Transaction
{
     private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;
    private LocalDate lastCommitDate;
    private DayOfWeek onWeekDay;
    private int onDate;
    private TRANSACTION_FREQUENCY transactionFrequency;
    
    // ......................... Getter & Setters ()..................
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public LocalDate getLastCommitDate() {
        return lastCommitDate;
    }

    public void setLastCommitDate(LocalDate lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public DayOfWeek getOnWeekDay() {
        return onWeekDay;
    }

    public void setOnWeekDay(DayOfWeek onWeekDay) {
        this.onWeekDay = onWeekDay;
    }

    public int getOnDate() {
        return onDate;
    }

    public void setOnDate(int onDate) {
        this.onDate = onDate;
    }

    public TRANSACTION_FREQUENCY getTransactionFrequency() {
        return transactionFrequency;
    }

    public void setTransactionFrequency(TRANSACTION_FREQUENCY transactionFrequency) {
        this.transactionFrequency = transactionFrequency;
    }
   //.......................................................................
      
    public Transaction commit()
    {
        LocalDate today = LocalDate.now();
        if(nextDueDate == null)
            return null;
        if(today.compareTo(nextDueDate) >=0)
        {
            System.out.println("ScheduledTransaction : Commiting sch transaction : "+getTransactionID() +"Frequency : "+getTransactionFrequency());
            Transaction t = Transaction.createTransaction(getFromAC(), getToAC());
            t.setAmount(getAmount());
            t.setMemo(getMemo());
            t.setTransactionNum(getTransactionNum());
            t.setPayee(getPayee());
            t.setTransactionDate(getNextDueDate());
            
            if(getTransactionFrequency() == TRANSACTION_FREQUENCY.ONCE)
            {
                setLastCommitDate(getNextDueDate());
                setNextDueDate(null);
            }
            else if(getTransactionFrequency() == TRANSACTION_FREQUENCY.DAILY)
            {
                setLastCommitDate(getNextDueDate());
                setNextDueDate(getNextDueDate().plusDays(1));
            }
            else if(getTransactionFrequency() == TRANSACTION_FREQUENCY.WEEKLY)
            {
                setLastCommitDate(getNextDueDate());
                DayOfWeek onWeekDay = getOnWeekDay();
                setNextDueDate(getNextDueDate().plusDays(1));
                while(getNextDueDate().getDayOfWeek() != onWeekDay)
                {
                    setNextDueDate(getNextDueDate().plusDays(1));
                }
                
            }
            else if(getTransactionFrequency() == TRANSACTION_FREQUENCY.MONTHLY)
            {
                setLastCommitDate(getNextDueDate());
                int day = getOnDate();
                Month month = getNextDueDate().getMonth().plus(1);
                int year = getNextDueDate().getYear();
                if(month ==Month.JANUARY)
                    year++;
                if(day>month.maxLength())
                    day = month.maxLength();
                if(month ==Month.FEBRUARY)
                    day = 28;
                setNextDueDate(LocalDate.of(year, month, day));
            }
            
            if(getNextDueDate().compareTo(endDate) >0)
                setNextDueDate(null);
            
            return t;
        }
        return null;
    }
    public void printToConsole()
    {
        super.printTransactionToConsole();
        System.out.println("Frequency        : "+transactionFrequency);
        System.out.println("Start Date       : "+startDate);
        System.out.println("End Date         : "+endDate);
        System.out.println("Next Due Date    : "+nextDueDate);
        System.out.println("Last Commit Date : "+lastCommitDate);
        System.out.println("On Week Day      : "+onWeekDay);
        System.out.println("On Date (Monthly): "+onDate);
    }
    

    public ScheduledTransaction(long transactionID) 
    {
        super(transactionID);
    }
    
}