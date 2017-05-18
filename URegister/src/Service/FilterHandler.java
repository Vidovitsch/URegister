/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import Model.Registration;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import uregister.FXMLDocumentController;


/**
 *
 * @author David
 */
public class FilterHandler {

    private RegistrationMgr regMgr = new RegistrationMgr();
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
            System.out.println("YESS!");
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
        
    }
    
    public void filterOnDateSpan(ListView lv, String startDate, String endDate) {
        
    }
    
    private void fillList(ListView lv, List<Registration> registrations) {
        lv.getItems().clear();
        ObservableList<Registration> doList = FXCollections.observableArrayList(registrations);
        lv.getItems().addAll(doList);
    }
}
