/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import Model.Registration;
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
public class TimeMgr {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("URegisterPU");
    
    private String start;
    private Timer timer = null;
    private int seconds = 0;
    
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

    
    private String getCurrentTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        
        return strDate;
    }
}
