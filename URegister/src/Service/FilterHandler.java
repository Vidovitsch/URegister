package Service;

import Model.Registration;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class FilterHandler {
    
    private List<Registration> allRegistrations;
    private String[] totalValues;
    
    public FilterHandler(List<Registration> allRegistrations) {
        this.allRegistrations = allRegistrations;
        this.totalValues = new String[2];
    }
    
    public String[] getTotalValues() {
        return this.totalValues;
    }
    
    public void filterOnMonthYear(ListView lv, String year, int monthIndex) {
        List<Registration> filtered = new ArrayList();
        if (year == null) {
            for (Registration reg : allRegistrations) {
                int mIndex = reg.getDate().getMonth();
                if (mIndex == monthIndex) {
                    filtered.add(reg);
                }
            }
        } else if (monthIndex == -1) {
            for (Registration reg : allRegistrations) {
                String y = String.valueOf(reg.getDate().getYear() + 1900);
                if (y.equals(year)) {
                    filtered.add(reg);
                }
            }
        } else {
            List<Registration> extraFiltered = new ArrayList();
            for (Registration reg : allRegistrations) {
                String y = String.valueOf(reg.getDate().getYear() + 1900);
                if (y.equals(year)) {
                    extraFiltered.add(reg);
                }
            }
            for (Registration reg : extraFiltered) {
                int mIndex = reg.getDate().getMonth();
                if (mIndex == monthIndex) {
                    filtered.add(reg);
                }
            }
        }
        fillList(lv, filtered);
    }
    
    public void filterOnDate(ListView lv, String date) {
        List<Registration> filtered = new ArrayList();
        String[] values = date.split("-");
        for (Registration reg : allRegistrations) {
            String[] v = reg.getDate().toString().split("-");
            if (v[0].equals(values[0]) && v[1].equals(values[1]) && v[2].equals(values[2])) {
                filtered.add(reg);
            }
        }
        fillList(lv, filtered);
    }
    
    public void filterOnDateSpan(ListView lv, String startDate, String endDate) {
        Date start = new Utility().stringToDate(startDate);
        Date end = new Utility().stringToDate(endDate);
        
        fillList(lv, new RegistrationMgr().findByDateSpan(start, end));
    }
    
    private void calcTotalValues(List<Registration> filtered) {
        String totalTime = null;
        String salary = new RegistrationMgr().loadSalary();
        for (Registration reg : filtered) {
            totalTime = new Utility().getTotalTime(totalTime, reg.getWorkedTime().toString());
        }
        String totalLoan = new Utility().getTotalLoan(totalTime, salary);
        
        totalValues[0] = totalTime;
        totalValues[1] = totalLoan;
    }
   
    
    public void fillList(ListView lv, List<Registration> registrations) {
        calcTotalValues(registrations);
        lv.getItems().clear();
        ObservableList<Registration> doList = FXCollections.observableArrayList(registrations);
        lv.getItems().addAll(doList);
    }
}
