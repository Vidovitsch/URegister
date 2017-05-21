package Service;

import Model.Registration;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class FilterHandler {
    
    private List<Registration> allRegistrations;
    
    public FilterHandler(List<Registration> allRegistrations) {
        this.allRegistrations = allRegistrations;
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
        //ToDo
    }
    
    private void fillList(ListView lv, List<Registration> registrations) {
        lv.getItems().clear();
        ObservableList<Registration> doList = FXCollections.observableArrayList(registrations);
        lv.getItems().addAll(doList);
    }
}
