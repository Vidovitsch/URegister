package com.ksdgroep.uregister;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class FXMLController implements Initializable {
    
    
    @FXML
    private Pane pane;
    
    @FXML
    private Label label;
    
    @FXML
    private Button buttonFinishWork;
            
    @FXML
    private Button buttonStartWork;
    
    @FXML
    private ListView listViewRegistrations;
            @FXML
    private ComboBox comboBoxYearFilter;
            @FXML
    private ComboBox comboBoxMonthFilter;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
