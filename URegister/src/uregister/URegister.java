/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uregister;

import Service.RegistrationMgr;
import java.sql.Date;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class URegister extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLDocumentController c = new FXMLDocumentController();
        FXMLLoader l = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Pane p = (Pane) l.load();
        c = (FXMLDocumentController) l.getController();
        Scene scene = new Scene(p);
        c.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
