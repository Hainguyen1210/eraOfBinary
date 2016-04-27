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
  
  @FXML private AnchorPane mainPane;
  @FXML private Label player1KeyLabel, player2KeyLabel, player3KeyLabel;
  @FXML private Button startButton;
  @FXML private ProgressIndicator progressIndicator;
//  private static ProgressIndicator progressIndicator1;
  
  @Override  public void initialize(URL url, ResourceBundle rb) {
//    progressIndicator1 = progressIndicator;
  }
  
  @FXML private void updateProgress() throws InterruptedException{
    
      try {//try to create timerTask
        timerTask sendingCounter = new timerTask(1, 3);
        try {//try to bind UI with counter task
          progressIndicator.progressProperty().bind(sendingCounter.progressProperty());
          System.out.println("binded with sendingCounter");
          } catch (Exception e) {
            System.err.println("cannot bind");
          }
        Thread sendingThread = new Thread(sendingCounter);
        sendingThread.setDaemon(true);
        sendingThread.start();
        System.out.println("thread2 started");
      } catch (Exception e) {
        System.err.println("can not create timer task");
      }
  }
}
