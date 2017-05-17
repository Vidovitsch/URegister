/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uregister;

import Model.Registration;
import Model.TestModel;
import Service.RegistrationMgr;
import Service.TimeMgr;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author David
 */
public class FXMLDocumentController implements Initializable
{

    private boolean fromDateSet = false;
    private boolean tillDateSet = false;
    private Date starttime = null;
    int elapsedhours = 0;
    int elapsedminutes = 0;
    int elapsedseconds = 0;
    private String workedTime = "";
    private Date endtime = null;
    private Timeline timeline = null;
    private String startString = "";
    private String endString = "";
    private Time sTime = null;
    private Time eTime = null;
    private String ElapsedText = "";
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    boolean ispaused = false;
    boolean isworking = false;

    @FXML
    private Button ButtonStartWork;

    @FXML
    private Button ButtonFinishWork;

    @FXML
    private Pane PaneAddDescription;

    @FXML
    private TextArea textAreaNewRegistrationDescription;

    @FXML
    private Button buttonSubmitNewRegistration;

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
    private Button ButtonPauseResume;

    @FXML
    private void handleButtonAction(ActionEvent event)
    {

    }

    @FXML
    private void handleUpdateSalaryButton(ActionEvent event)
    {

    }

    @FXML
    private void handleStartWorkButton(ActionEvent event)
    {
        if (isworking)
        {
            initAlertMessage("you have already started working, click pause to take a break or finish you worksession");
        } else
        {
            createNewTimeline();
            isworking = true;
            ButtonPauseResume.setDisable(!isworking);
        }

    }

    private void createNewTimeline()
    {
        Calendar cal = Calendar.getInstance();
        starttime = cal.getTime();
        startString = (sdf.format(cal.getTime()));
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> updateTime()
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleFinishWorkButton(ActionEvent event)
    {
        if (isworking)
        {
            timeline.stop();
            Calendar cal = Calendar.getInstance();
            endtime = cal.getTime();
            endString = (sdf.format(cal.getTime()));
            workedTime = LabelElapsedTime.getText();
            showPopup();
        } else
        {
            initAlertMessage("Start a worksession before you can finish one");
        }

    }

    @FXML
    private void handlePauseOrResumeWorkButton(ActionEvent event)
    {
        if (ispaused)
        {
            setPause(false);
            ispaused = false;
        } else
        {
            setPause(true);
            ispaused = true;
        }
    }

    private void setPause(boolean pause)
    {
        if (!pause)
        {
            timeline.play();
            ButtonPauseResume.setText("Pause");
        } else
        {
            timeline.stop();
            ButtonPauseResume.setText("resume");
        }
    }

    @FXML
    private void handleSubmitRegistrationButton(ActionEvent event)
    {
        String registrationDescription = textAreaNewRegistrationDescription.getText();
        Registration r = new Registration();
        if (registrationDescription.equals(""))
        {
            initErrorMessage("You should add a description of the work you've done");
        } else
        {
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

    private void resetRegistrationsList(){
        RegistrationMgr r = new RegistrationMgr();
        fillList(ListViewRegistrations, r.findAll());
    }
    
    private void showPopup()
    {
        PaneAddDescription.setVisible(true);
    }

    private void updateTime()
    {
        elapsedseconds++;
        if (elapsedseconds == 60)
        {
            elapsedminutes++;
            elapsedseconds = 0;
            if (elapsedminutes == 60)
            {
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

    private void resetTime()
    {
        elapsedhours = 0;
        elapsedminutes = 0;
        elapsedseconds = 0;
        String hours = String.valueOf(elapsedhours);
        String minutes = String.valueOf(elapsedminutes);
        String seconds = String.valueOf(elapsedseconds);
        ElapsedText = ("0" + hours).substring(hours.length() - 1) + ":" + ("0" + minutes).substring(minutes.length() - 1) + ":" + ("0" + seconds).substring(seconds.length() - 1);
        updateElapsedTime(ElapsedText);
    }

    public void updateElapsedTime(String timerText)
    {
        LabelElapsedTime.setText("Elapsed time: " + timerText);
    }

    public void fillList(ListView listview, List<Registration> stringlist)
    {
        listview.getItems().clear();
        ObservableList<Registration> doList = FXCollections.observableArrayList(stringlist);
        listview.getItems().addAll(doList);
    }

    public void removeItemFromList(ListView listview, Object item)
    {
        listview.getItems().remove(item);
    }

    public void useYearFilter(String year)
    {
        int yearInt = Integer.parseInt(year);
        //TODO filter registrationobject based on yearfilter
    }

    public void useMonthFilter(String month) throws ParseException
    {
        Date date;
        date = new SimpleDateFormat("MMMM").parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        //TODO filter registrationobject based on monthfilter
    }

    public void useDateFilter(LocalDate date)
    {
        //TODO filter registrationobject based on datefilter
        System.out.println(date);
    }

    private void setFromDay(LocalDate date)
    {
        System.out.println(date);
    }

    private void setTillDay(LocalDate date)
    {
        System.out.println(date);
    }

    public void addItemToList(ListView listview, Object item)
    {
        listview.getItems().add(item);
    }

    public void initErrorMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean initAlertMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ComboBoxYearFilter.getItems().addAll("2017", "2016", "2015", "2014", "2013");
        ComboBoxYearFilter.setPromptText("year");
        ComboBoxYearFilter.setEditable(false);
        ComboBoxYearFilter.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                useYearFilter(newValue);
            }
        });

        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++)
        {
            ComboBoxMonthFilter.getItems().add(months[i]);
        }
        ComboBoxMonthFilter.setPromptText("month");
        ComboBoxMonthFilter.setEditable(false);
        ComboBoxMonthFilter.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    useMonthFilter(newValue);
                } catch (ParseException ex)
                {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        DatePickerDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> 
                {
                    useDateFilter(newValue);
        });

        DatePickerFromDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> 
                {
                    setFromDay(newValue);
        });

        DatePickerTillDayFilter.valueProperty().addListener((ov, oldValue, newValue)
                -> 
                {
                    setTillDay(newValue);
        });
        resetRegistrationsList();
    }

}
