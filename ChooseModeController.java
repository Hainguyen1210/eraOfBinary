/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * choose between 2 modes: 2P and 3P
 */
public class ChooseModeController implements Initializable {
  @FXML Label eraOfBinary;
  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }  
  @FXML public void toMatch2() throws Exception {
    Player.is3Players = false;
    Stage window = (Stage) eraOfBinary.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("match2.fxml"));
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.show();
  }
  @FXML public void toMatch3() throws Exception {
    Player.is3Players = true;
    Stage window = (Stage) eraOfBinary.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("match3.fxml"));
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.show();
  }
}
