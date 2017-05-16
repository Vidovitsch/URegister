/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import domain.Registratie;
import domain.TimeDiff;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author David
 */
public class TimeManager {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("URegisterPU");
    
    private Registratie registratie;
    private String start;
    private Timer timer = null;
    private int seconds = 0;
    
    
    public TimeManager(Registratie registratie) {
        this.registratie = registratie;
    }
    
    public String getCurrentCount() {
        return formatSeconds(seconds);
    }
    
    public boolean startTimer() {
        if (timer != null) {
            start = getCurrentTime();
            return false;
        } else {
            start();
            return true;
        }
    }
    
    public String endTimer() {
        timer.cancel();
        timer = null;
        String elapsed = getCurrentCount();
                
        createNewTimeStamp(start, getCurrentTime());
        
        seconds = 0;
        return elapsed;
    }
    
    private void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                System.out.println(formatSeconds(seconds));
            }
        }, 0, 1000);
    }
    
    private String formatSeconds(int seconds) {
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        
        return timeOfDay.toString();
    }
    
    private void createNewTimeStamp(String begin, String end) {
        TimeDiff td = new TimeDiff();
        td.setStart(begin);
        td.setEnd(end);
        registratie.getTimes().add(td);
    }
    
    private String getCurrentTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        
        return strDate;
    }
}
