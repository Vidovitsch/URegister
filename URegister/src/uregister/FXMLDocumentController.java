/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uregister;

import Model.TestModel;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author David
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button ButtonStartWork;

    @FXML
    private Button ButtonFinishWork;

    @FXML
    private ListView ListViewRegistrations;

    @FXML
    private Label LabelElapsedTime;

    @FXML
    private ComboBox ComboBoxYearFilter;

    @FXML
    private ComboBox ComboBoxMonthFilter;

    @FXML
    private DatePicker DatePickerDayFilter;

    @FXML
    private DatePicker DatePickerFromDayFilter;

    @FXML
    private DatePicker DatePickerTillDayFilter;

    @FXML
    private TextField TextFieldSalary;

    @FXML
    private Button ButtonUpdateSalary;

    @FXML
    private DatePicker DatePickerSelectedRegistrationDate;

    @FXML
    private TextField TextFieldSelectedRegistrationStartTime;

    @FXML
    private TextField TextFieldSelectedRegistrationEndTime;

    @FXML
    private TextArea TextAreaSelectedRegistrationDescription;

    @FXML
    private Button ButtonUpdateSelectedRegistration;

    @FXML
    private Button ButtonDeleteSelectedRegistration;

    @FXML
    private Button ButtonExportCurrentList;

    @FXML
    private void handleButtonAction(ActionEvent event) {

    }

    @FXML
    private void handleUpdateSalaryButton(ActionEvent event) {

    }

    @FXML
    private void handleStartWorkButton(ActionEvent event) {
        TestModel m = new TestModel();
        m.setName("Testname");
        addItemToList(ListViewRegistrations, m);
    }

    @FXML
    private void handleFinishWorkButton(ActionEvent event) {
        TestModel m = new TestModel();
        m.setName("Testname");
        removeItemFromList(ListViewRegistrations, m);
    }

    public void updateElapsedTime(String timerText) {
        LabelElapsedTime.setText("Elapsed time: " + timerText);
    }

    public void fillList(ListView listview, ArrayList<String> stringlist) {
        ObservableList<String> doList = FXCollections.observableArrayList(stringlist);
        listview.getItems().addAll(doList);
    }

    public void removeItemFromList(ListView listview, Object item) {
        listview.getItems().remove(item);
    }

    public void useYearFilter(String year) {
        int yearInt = Integer.parseInt(year);
        //TODO filter registrationobject based on yearfilter
    }

    public void useMonthFilter(String month) throws ParseException {
        Date date;
        date = new SimpleDateFormat("MMMM").parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        //TODO filter registrationobject based on yearfilter
    }

    public void addItemToList(ListView listview, Object item) {
        listview.getItems().add(item);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ComboBoxYearFilter.getItems().addAll("2017", "2016", "2015", "2014", "2013");
        ComboBoxYearFilter.setPromptText("year");
        ComboBoxYearFilter.setEditable(false);
        ComboBoxYearFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                useYearFilter(newValue);
            }
        });

        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            ComboBoxMonthFilter.getItems().add(months[i]);
        }
        ComboBoxMonthFilter.setPromptText("month");
        ComboBoxMonthFilter.setEditable(false);
        ComboBoxMonthFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    useMonthFilter(newValue);
                } catch (ParseException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
