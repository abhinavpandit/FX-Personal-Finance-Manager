/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author johnl
 */
public class DateConverter 
{
    public static GregorianCalendar LocalDateToGregorian(LocalDate date)
    {
        return new GregorianCalendar(date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());
    }
    
    public static LocalDate GregorianToLocalDate(GregorianCalendar date)
    {
       return LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));
      
    }
    
}
