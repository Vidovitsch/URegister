package uregister;

import Model.Registration;
import Service.ExportList;
import Service.FilterHandler;
import Service.RegistrationMgr;
import java.io.File;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    private FilterHandler fHandler;
    private RegistrationMgr regMgr = new RegistrationMgr();
    private int elapsedhours = 0;
    private int elapsedminutes = 0;
    private int elapsedseconds = 0;
    private Date endtime = null;
    private Timeline timeline = null;
    private String startString = "";
    private String endString = "";
    private String ElapsedText = "";
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    boolean ispaused = false;
    boolean isworking = false;
    private Registration selectedRegistration = null;
    private List<Registration> regView;
    private List<Registration> allRegistrations;
    private String startDate = null;
    private String endDate = null;

    @FXML
    private Pane PaneAddDescription, panePopUp;

    @FXML
    private ListView ListViewRegistrations;

    @FXML
    private Label LabelElapsedTime, lblTotalLoan, lblTotalHours;

    @FXML
    private ComboBox ComboBoxMonthFilter, ComboBoxYearFilter;

    @FXML
    private DatePicker DatePickerSelectedRegistrationDate, DatePickerTillDayFilter,
            DatePickerFromDayFilter, DatePickerDayFilter;

    @FXML
    private TextField TextFieldSelectedRegistrationWorkedTime, TextFieldSelectedRegistrationEndTime, 
        TextFieldSelectedRegistrationStartTime, TextFieldSalary;

    @FXML
    private TextArea TextAreaSelectedRegistrationDescription, textAreaNewRegistrationDescription, 
            TextAreaSelectedRegistrationDescription2;

    @FXML
    private Button ButtonUpdateSelectedRegistration,
        ButtonDeleteSelectedRegistration, ButtonPauseResume,
        ButtonExportCurrentList, ButtonUpdateSalary, buttonSubmitNewRegistration,
        ButtonFinishWork, ButtonStartWork, ButtonCreateNewRegistration;

    @FXML
    private void handleExportCurrentListButton(ActionEvent event) {
        String filename = locateFile(event).getAbsolutePath();
        if (filename != null) {
            setCurrentListData();
            ExportList e = new ExportList();
            e.exportToExcel(regView, filename);
            System.out.println(filename);
            initSuccessMessage("Current list exported to excel");
        } else {
            initAlertMessage("List is not exported");
        }
    }
    
    @FXML
    private void handleSubmitRegistrationButton(ActionEvent event) {
        String registrationDescription = textAreaNewRegistrationDescription.getText();
        Registration r = new Registration();
        if (registrationDescription.equals("")) {
            initErrorMessage("You should add a description of the work you've done");
        } else {
            r.setContent(registrationDescription);
            r.setDate(new java.sql.Date(endtime.getTime()));
            r.setStart(java.sql.Time.valueOf(startString));
            r.setEnd(java.sql.Time.valueOf(endString));
            r.setWorkedTime(java.sql.Time.valueOf(ElapsedText));
            RegistrationMgr rmgr = new RegistrationMgr();
            rmgr.createRegistration(r);
            resetTime();
            setPause(false);
            timeline.stop();
            ButtonPauseResume.setDisable(true);
            textAreaNewRegistrationDescription.clear();
            isworking = false;
            PaneAddDescription.setVisible(false);
            resetRegistrationsList();
        }
    }
    
    @FXML
    private void handleUpdateSalaryButton(ActionEvent event) {
        String salary = TextFieldSalary.getText();
        regMgr.saveSalary(salary);
    }

    @FXML
    private void handleStartWorkButton(ActionEvent event) {
        if (isworking) {
            initAlertMessage("you have already started working, click pause to take a break or finish you worksession");
        } else {
            createNewTimeline();
            isworking = true;
            ButtonPauseResume.setDisable(!isworking);
        }
    }
    
    @FXML
    protected File locateFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        return chooser.showSaveDialog(new Stage());
    }
    
    @FXML
    private void handleFinishWorkButton(ActionEvent event) {
        if (isworking) {
            timeline.stop();
            Calendar cal = Calendar.getInstance();
            endtime = cal.getTime();
            endString = (sdf.format(cal.getTime()));
            showPopup();
        } else {
            initAlertMessage("Start a worksession before you can finish one");
        }
    }

    @FXML
    private void handlePauseOrResumeWorkButton(ActionEvent event) {
        if (ispaused) {
            setPause(false);
            ispaused = false;
        } else {
            setPause(true);
            ispaused = true;
        }
    }
    
    @FXML
    private void handleSelectedRegistrationDateChanged(ActionEvent event) {
        prepareNewRegistration();
    }

    @FXML
    private void updateRegistration(ActionEvent event) {
        selectedRegistration.setContent(TextAreaSelectedRegistrationDescription.getText());
        selectedRegistration.setStart(java.sql.Time.valueOf(TextFieldSelectedRegistrationStartTime.getText()));
        selectedRegistration.setEnd(java.sql.Time.valueOf(TextFieldSelectedRegistrationEndTime.getText()));
        selectedRegistration.setWorkedTime(java.sql.Time.valueOf(TextFieldSelectedRegistrationWorkedTime.getText()));
        regMgr.updateRegistration(selectedRegistration);
        initSuccessMessage("Registration updated");
        resetRegistrationsList();
        clearSelectedRegistration();
        
        panePopUp.setVisible(false);
    }

    @FXML
    private void CreateNewRegistration(ActionEvent event) {
        Registration newRegistration = new Registration();
        newRegistration.setDate(java.sql.Date.valueOf(DatePickerSelectedRegistrationDate.getValue()));
        newRegistration.setContent(TextAreaSelectedRegistrationDescription.getText());
        newRegistration.setStart(java.sql.Time.valueOf(TextFieldSelectedRegistrationStartTime.getText()));
        newRegistration.setEnd(java.sql.Time.valueOf(TextFieldSelectedRegistrationEndTime.getText()));
        newRegistration.setWorkedTime(java.sql.Time.valueOf(TextFieldSelectedRegistrationWorkedTime.getText()));
        regMgr.createRegistration(newRegistration);
        initSuccessMessage("Registration created");
        resetRegistrationsList();
        clearSelectedRegistration();
        
        panePopUp.setVisible(false);
    }
    
    @FXML
    private void deleteRegistration(ActionEvent event) {
        if (initAlertMessage("Are you sure you want to remove the selected registration?")) {
            System.out.println(String.valueOf(selectedRegistration.getId()));
            regMgr.remove(selectedRegistration);
            clearSelectedRegistration();
            resetRegistrationsList();
            
            panePopUp.setVisible(false);
        }
    }
    
    @FXML
    private void closeInfoPopUp(ActionEvent event) {
        panePopUp.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panePopUp.setVisible(false);
        setSalaryField();
        setEventHandlers();
    }
    
    public void updateElapsedTime(String timerText) {
        LabelElapsedTime.setText("Elapsed time: " + timerText);
    }

    public void removeItemFromList(ListView listview, Object item) {
        listview.getItems().remove(item);
    }
    
    public void addItemToList(ListView listview, Object item) {
        listview.getItems().add(item);
    }

    public void initErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean initAlertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public void initSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    private void setEventHandlers() {
        ComboBoxYearFilter.getItems().addAll("All", "2018", "2017", "2016", "2015", "2014", "2013");
        ComboBoxYearFilter.getSelectionModel().select(0);
        ComboBoxYearFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String month = (String) ComboBoxMonthFilter.getSelectionModel().getSelectedItem();
                int index = ComboBoxMonthFilter.getItems().indexOf(month);
                if (newValue.equals("All") && index == 0) {
                    fHandler.fillList(ListViewRegistrations, allRegistrations);
                } else {
                    if (month.equals("All")) {
                        fHandler.filterOnMonthYear(ListViewRegistrations, newValue, -1);
                    } else if (newValue.equals("All")) {
                        fHandler.filterOnMonthYear(ListViewRegistrations, null, index);
                    } else {
                        fHandler.filterOnMonthYear(ListViewRegistrations, newValue, index);
                        setTotalFields(fHandler.getTotalValues());
                    }
                }
            }
        });

        String[] months = new DateFormatSymbols().getMonths();
        ComboBoxMonthFilter.getItems().add("All");
        for (int i = 0; i < months.length; i++) {
            ComboBoxMonthFilter.getItems().add(months[i]);
        }
        ComboBoxMonthFilter.getSelectionModel().select(0);
        ComboBoxMonthFilter.setEditable(false);
        ComboBoxMonthFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int index = ComboBoxMonthFilter.getItems().indexOf(newValue);
                String year = (String) ComboBoxYearFilter.getSelectionModel().getSelectedItem();
                if (year.equals("All") && index == 0) {
                    fHandler.fillList(ListViewRegistrations, allRegistrations);
                } else {
                    if (year.equals("All")) {
                        fHandler.filterOnMonthYear(ListViewRegistrations, null, index);
                    } else if (newValue.equals("All")) {
                        fHandler.filterOnMonthYear(ListViewRegistrations, year, -1);
                    } else {
                        fHandler.filterOnMonthYear(ListViewRegistrations, year, index);
                        setTotalFields(fHandler.getTotalValues());
                    }
                }
            }
        });

        DatePickerDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> {
            fHandler.filterOnDate(ListViewRegistrations, newValue.toString());
            setTotalFields(fHandler.getTotalValues());
        });

        DatePickerFromDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> {
            setFromDay(newValue);
        });

        DatePickerTillDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> {
            setTillDay(newValue);
        });
        resetRegistrationsList();
        ListViewRegistrations.setOnMouseClicked((MouseEvent event)
                -> {
            Registration r = (Registration) ListViewRegistrations.getSelectionModel().getSelectedItem();
            setSelectedRegistration(r);
            if (event.getClickCount() == 2) {
                panePopUp.setVisible(true);
            }
        });
        ListViewRegistrations.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Registration>() {
            @Override
            public void changed(ObservableValue<? extends Registration> observable, Registration oldValue, Registration newValue) {
                panePopUp.setVisible(false);
            }
        });
    }

    private void setCurrentListData() {
        regView = new ArrayList<>();
        for (int i = 0; i < ListViewRegistrations.getItems().size(); i++) {
            Registration r = (Registration) ListViewRegistrations.getItems().get(i);
            regView.add(r);
        }
    }

    private void createNewTimeline() {
        Calendar cal = Calendar.getInstance();
        startString = (sdf.format(cal.getTime()));
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> updateTime()
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setPause(boolean pause) {
        if (!pause) {
            timeline.play();
            ButtonPauseResume.setText("Pause");
        } else {
            timeline.stop();
            ButtonPauseResume.setText("resume");
        }
    }

    private void resetRegistrationsList() {
        RegistrationMgr r = new RegistrationMgr();
        allRegistrations = r.findAll();
        fHandler = new FilterHandler(allRegistrations);
        fHandler.fillList(ListViewRegistrations, allRegistrations);
        setTotalFields(fHandler.getTotalValues());
    }

    private void showPopup() {
        PaneAddDescription.setVisible(true);
    }

    private void updateTime() {
        elapsedseconds++;
        if (elapsedseconds == 60) {
            elapsedminutes++;
            elapsedseconds = 0;
            if (elapsedminutes == 60) {
                elapsedhours++;
                elapsedminutes = 0;
            }
        }

        String hours = String.valueOf(elapsedhours);
        String minutes = String.valueOf(elapsedminutes);
        String seconds = String.valueOf(elapsedseconds);
        ElapsedText = ("0" + hours).substring(hours.length() - 1) + ":" + ("0" + minutes).substring(minutes.length() - 1) + ":" + ("0" + seconds).substring(seconds.length() - 1);
        updateElapsedTime(ElapsedText);
    }

    private void resetTime() {
        elapsedhours = 0;
        elapsedminutes = 0;
        elapsedseconds = 0;
        String hours = String.valueOf(elapsedhours);
        String minutes = String.valueOf(elapsedminutes);
        String seconds = String.valueOf(elapsedseconds);
        ElapsedText = ("0" + hours).substring(hours.length() - 1) + ":" + ("0" + minutes).substring(minutes.length() - 1) + ":" + ("0" + seconds).substring(seconds.length() - 1);
        updateElapsedTime(ElapsedText);
    }

    private void setFromDay(LocalDate date) {
        startDate = date.toString();
        if (endDate != null) {
            fHandler.filterOnDateSpan(ListViewRegistrations, startDate, endDate);
            setTotalFields(fHandler.getTotalValues());
        }
    }

    private void setTillDay(LocalDate date) {
        endDate = date.toString();
        if (startDate != null) {
            fHandler.filterOnDateSpan(ListViewRegistrations, startDate, endDate);
            setTotalFields(fHandler.getTotalValues());
        }
    }

    private void setSelectedRegistration(Registration r) {
        if (r != null) {
            DatePickerSelectedRegistrationDate.setValue(r.getDate().toLocalDate());
            TextFieldSelectedRegistrationEndTime.setText(r.getEnd().toString());
            TextFieldSelectedRegistrationStartTime.setText(r.getStart().toString());
            TextFieldSelectedRegistrationWorkedTime.setText(r.getWorkedTime().toString());
            TextAreaSelectedRegistrationDescription.setText(r.getContent());
            TextAreaSelectedRegistrationDescription2.setText(r.getContent());
            selectedRegistration = r;
            ButtonUpdateSelectedRegistration.setDisable(false);
            ButtonDeleteSelectedRegistration.setDisable(false);
        } else {

        }
    }

    private void clearSelectedRegistration() {
        selectedRegistration = null;
        DatePickerSelectedRegistrationDate.setValue(null);
        TextFieldSelectedRegistrationEndTime.clear();
        TextFieldSelectedRegistrationStartTime.clear();
        TextFieldSelectedRegistrationWorkedTime.clear();
        TextAreaSelectedRegistrationDescription.clear();
        TextAreaSelectedRegistrationDescription2.clear();
        ButtonUpdateSelectedRegistration.setDisable(true);
        ButtonDeleteSelectedRegistration.setDisable(true);
    }

    private void prepareNewRegistration() {
        selectedRegistration = null;
        TextFieldSelectedRegistrationEndTime.setText("00:00:00");
        TextFieldSelectedRegistrationStartTime.setText("00:00:00");
        TextFieldSelectedRegistrationWorkedTime.setText("00:00:00");
        TextAreaSelectedRegistrationDescription.clear();
        TextAreaSelectedRegistrationDescription2.clear();
        ButtonUpdateSelectedRegistration.setDisable(true);
        ButtonDeleteSelectedRegistration.setDisable(true);
    }

    private void setSalaryField() {
        String salary = regMgr.loadSalary();
        if (salary != null) {
            TextFieldSalary.setText(salary);
        }
    }
    
    private void setTotalFields(String[] totalValues) {
        if (totalValues[0] == null) {
            lblTotalHours.setText("Total hours: -");
        } else {
            lblTotalHours.setText("Total hours: " + totalValues[0]);
        }
        if (totalValues[1] == null) {
            lblTotalLoan.setText("Total loan (bruto): -");
        } else {
            lblTotalLoan.setText("Total loan (bruto): €" + totalValues[1]);
        }
    }
}
