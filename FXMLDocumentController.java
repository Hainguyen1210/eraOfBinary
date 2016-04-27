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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 *
 * @author haing
 */
public class FXMLDocumentController implements Initializable {
  private timerTask counter;
  public static boolean isSendingPhase;
  
  @FXML private AnchorPane mainPane, instructionPane;
  @FXML private Label player1KeyLabel, player2KeyLabel, player3KeyLabel;
  @FXML private Label player1NameLabel, player2NameLabel, player3NameLabel;
  @FXML private TextField player1NameField, player2NameField, player3NameField;
  @FXML private Button startButton, backButton;
  @FXML public ProgressIndicator progressIndicator;
  
  
  @Override  public void initialize(URL url, ResourceBundle rb) {
  }
  @FXML private void onBackButton(){
    instructionPane.setVisible(true);
    backButton.setVisible(false);
  }
  @FXML private void onStartButton(){
    //get Player's Name
    String player1Name="", player2Name="", player3Name="";
    String[] playerNameStrings = {player1Name, player2Name, player3Name};
    TextField[] playerNameFields = {player1NameField, player2NameField, player3NameField};
    Label[] playerNameLabels = {player1NameLabel, player2NameLabel, player3NameLabel};
    
    for(int i = 0; i < 3; i++){
      try {
        playerNameStrings[i] = playerNameFields[i].getText();
        if (playerNameStrings[i].length()<2){
          playerNameStrings[i] = "anonymous";
        }
      } catch (Exception e) {
        System.err.println("cannot get text");
      }
    } 
    System.out.println("player1Name " + playerNameStrings[0]);
    System.out.println("player2Name " + playerNameStrings[1]);
    System.out.println("player3Name " + playerNameStrings[2]);
    //create Players
    Player one = new Player(playerNameStrings[0], KeyCode.Z, KeyCode.X);
    Player.players.add(0, new Player(playerNameStrings[0], KeyCode.Z, KeyCode.X));
    Player.players.add(1, new Player(playerNameStrings[1], KeyCode.N, KeyCode.M));
    Player.players.add(2, new Player(playerNameStrings[2], KeyCode.NUMPAD1, KeyCode.NUMPAD2));
    //Display Player's Names 
    for(int i = 0; i < 3; i++) {
      playerNameLabels[i].setText(Player.players.get(i).name);
    }
    //Hide the Instruction Panne
    instructionPane.setVisible(false);
    backButton.setVisible(true);
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
