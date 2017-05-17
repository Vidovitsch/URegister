/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author David
 */
public class Utility {

    private final String fileName = "config.properties";
    
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
    
    public void saveSalary(String salary) {
        checkForProperties();
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            props.setProperty("Salary", salary);
            props.store(out, null);
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String loadSalary() {
        checkForProperties();
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        props.getProperty("Salary");
        return props.getProperty("Salary");
    }
        
    private void checkForProperties() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
