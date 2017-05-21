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
        
    public void checkForProperties() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getTotalLoan(String totalTime, String salary) {
        if (totalTime == null || salary == null) {
            return null;
        } else {
            String[] tValues = totalTime.split(":");
            double hours = Integer.valueOf(tValues[0]);
            double minutes = Integer.valueOf(tValues[1]);
            double seconds = Integer.valueOf(tValues[2]);
            double s = Double.valueOf(salary);
            
            double m = (minutes * 60 + seconds) / 3600;
            
            double loan = (hours + m) * s;

            String l = String.valueOf(loan);
            String result = l.substring(0, l.indexOf(".") + 3);
                
            return result;
        }
    }
    
    public String getTotalTime(String totalTime, String added) {
        if (totalTime == null) {
            return added;
        } else {
            System.out.println("Total Time: " + totalTime);
            int hours;
            int minutes;
            int seconds;
            String[] tValues = totalTime.split(":");
            String[] aValues = added.split(":");

            hours = Integer.valueOf(tValues[0]) + Integer.valueOf(aValues[0]);
            minutes = Integer.valueOf(tValues[1]) + Integer.valueOf(aValues[1]);
            seconds = Integer.valueOf(tValues[2]) + Integer.valueOf(aValues[2]);
            
            while (seconds >= 60) {
                minutes++;
                seconds -= 60;
            }
            while (minutes >= 60) {
                hours++;
                minutes -= 60;
            }

            String time = String.valueOf(hours) + ":" + String.valueOf(minutes)
                    + ":" + String.valueOf(seconds);
            return formatTime(time);
        }
    }
        
    private String formatTime(String time) {
        String[] tValues = time.split(":");
        if (tValues[0].length() == 1) {
            tValues[0] = "0" + tValues[0];
        }
        if (tValues[1].length() == 1) {
            tValues[1] = "0" + tValues[1];
        } 
        if (tValues[2].length() == 1) {
            tValues[2] = "0" + tValues[2];
        }

        time = tValues[0] + ":" + tValues[1] + ":" + tValues[2];
        return time;
    }
}
