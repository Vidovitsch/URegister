/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author David
 */
public class Utility {

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diff = date2.getTime() - date1.getTime();
        return timeUnit.convert(diff, timeUnit);
    }
    
    public Date addDayToDate(Date date) {
        Calendar c = Calendar.getInstance(); 
        c.setTime(date); 
        c.add(Calendar.DATE, 1);
        return (Date) c.getTime();
    }
}
