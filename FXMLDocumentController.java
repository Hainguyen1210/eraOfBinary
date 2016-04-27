/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressIndicator;

/**
 *
 * @author haing
 */
public class FXMLDocumentController implements Initializable {
  private timerTask counter;
  public static boolean isSendingPhase;
  
  @FXML private AnchorPane mainPane;
  @FXML private Label player1KeyLabel, player2KeyLabel, player3KeyLabel;
  @FXML private Button startButton;
  @FXML private ProgressIndicator progressIndicator;
  
  
  @Override  public void initialize(URL url, ResourceBundle rb) {
  }
  
  @FXML private void updateProgress() throws InterruptedException{
    //Create timer, bind it with the Progress Indiicator
    //  Timer can be created with a pair of specific periods of time, 
    //    then it will be swapped turn by turn
    
      try {//try to create timerTask
        counter = new timerTask(2, 4);
        try {//try to bind UI with counter task
          progressIndicator.progressProperty().bind(counter.progressProperty());
          System.out.println("binded with Counter");
          } catch (Exception e) {
            System.err.println("cannot bind");
          }
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        counterThread.start();
        System.out.println("counterThread started");
      } catch (Exception e) {
        System.err.println("can not create timer task");
      }
  }
  
}
