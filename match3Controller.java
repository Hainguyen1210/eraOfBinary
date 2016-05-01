/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author haing
 */
public class match3Controller implements Initializable {
  public static TranslateTransition moving; //for animation
  private timerTask counter;
  private Thread currentThread;
  public static boolean isSendingPhase, isThreadRunning, isGameStarted;
  
  @FXML private AnchorPane mainPane, instructionPane;
  @FXML private Label 
          player1NameLabel, player2NameLabel, player3NameLabel,
          player1KeyLabel, player2KeyLabel, player3KeyLabel,
          player1ReceivedLabel, player2ReceivedLabel, player3ReceivedLabel,
          player1PointLabel, player2PointLabel, player3PointLabel,
          winningPointLabel;
  @FXML private TextField player1NameField, player2NameField, player3NameField;
  @FXML private TextField winningPointField;
  @FXML public ProgressIndicator progressIndicator;
  public static Label a, b, c, sent1, sent2, sent3, point1, point2, point3;
  public static int winningPoint;
  public static String[] colors = {"-fx-progress-color: red;", "-fx-progress-color: blue;" };
  public static int colorIndex;
  public static StringProperty indicatorColor;
  
  @Override  public void initialize(URL url, ResourceBundle rb) {
    isSendingPhase = true;
    indicatorColor = new SimpleStringProperty("-fx-progress-color: red;");
    colorIndex = 0;
    isThreadRunning = false;  isGameStarted = false;
    Sound.playBackgoundSound();
    a = player1KeyLabel; sent1 = player1ReceivedLabel; point1 = player1PointLabel;
    b = player2KeyLabel; sent2 = player2ReceivedLabel; point2 = player2PointLabel;
    c = player3KeyLabel; sent3 = player3ReceivedLabel; point3 = player3PointLabel;
  }
  
  @FXML private void backToMainMenu() throws IOException{
    Sound.background.stop();
    Stage currentStage = (Stage) mainPane.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("chooseMode.fxml"));
    Scene scene = new Scene(root);
    currentStage.setScene(scene);
    currentStage.show();
  }
  
  public static void swapIndicatorColor(){
    if(colorIndex == 0 ) {  colorIndex = 1;    }
    else {  colorIndex = 0; }
    
    Platform.runLater(new Runnable() {
      @Override
      public void run(){
        try {
          indicatorColor.set(colors[colorIndex]);
        }catch(Exception e){
          System.out.println("can't change color");
        }
      }
    });
  }
  private void resetGame() throws IOException{
    if(isThreadRunning) {
    counter.cancel(false);
    currentThread.stop();
    counter = null;
    currentThread = null;
    Player.players.clear();
    }
    
    Parent root = FXMLLoader.load(getClass().getResource("match3.fxml"));
    Stage currentStage = (Stage) mainPane.getScene().getWindow();
    Scene scene = new Scene(root);
    currentStage.setScene(scene);
    currentStage.show();
  }

  @FXML private void onStartButton(){
    //get winning points
    getWinningPoint();
    try {
      System.out.println("---enabling");
      enableKeyStroke();
      System.out.println("---enabled");
    } catch (Exception e) {
      System.err.println("---failed");
    }
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
    Player.players.add(0, new Player(playerNameStrings[0], a, sent1, point1, KeyCode.Z, KeyCode.X));
    System.out.println(" checking------------------1");
    Player.players.add(1, new Player(playerNameStrings[1], b, sent2, point2, KeyCode.N, KeyCode.M));
    System.out.println(" checking------------------2");
    Player.players.add(2, new Player(playerNameStrings[2], c, sent3, point3, KeyCode.NUMPAD1, KeyCode.NUMPAD2));
    System.out.println(" checking------------------3");

    try {
      
    //Display Player's Names 
    for(int i = 0; i < 3; i++) {
      playerNameLabels[i].setText(Player.players.get(i).name);
    }
    //Hide the Instruction Panne
    instructionPane.setVisible(false);
  //    backButton.setVisible(true);
    progressIndicator.setVisible(true);
    
      System.out.println(" checking-----------------FINISHED");
    } catch (Exception e) {
      System.out.println(" checking-----------------FAILED");
    }
  }
  @FXML private void getWinningPoint(){
    try {
      int checkingPoint;
      checkingPoint = Integer.parseInt(winningPointField.getText());
      if(checkingPoint <= 10) {
        winningPoint = checkingPoint;
      }
    } catch (NumberFormatException e) {
      winningPoint = 5;
    }
    winningPointLabel.setText("Winning points: " + Integer.toString(winningPoint));
    winningPointLabel.setVisible(true);
    winningPointField.setVisible(false);
  }

  @FXML private void updateProgress() throws InterruptedException{
    //Create timer, bind it with the Progress Indiicator
    //  Timer can be created with a pair of specific periods of time, 
    //    then it will be swapped turn by turn
    
    
    //Don't run if there is already a thread running
    if(!isThreadRunning){
      try {//try to create timerTask ------------EDIT TIMER HERE
        counter = new timerTask(1, 3);
        try {//try to bind UI with counter task  
          progressIndicator.progressProperty().bind(counter.progressProperty());
          progressIndicator.styleProperty().bind(indicatorColor);
          System.out.println("binded with Counter");
        } catch (Exception e) {
          System.err.println("cannot bind");
        }
        //create background thread
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        currentThread = counterThread;
        isThreadRunning = true;
        counterThread.start();
        System.out.println("  counterThread started");
      } catch (Exception e) {
        System.err.println("  can not create timer task");
      }
      
    }
  }
  @FXML private void enableKeyStroke(){
    //allow Players to enter key inputs
    progressIndicator.getScene().setOnKeyPressed((KeyEvent keyInput) -> {
      //GAME START ON SPACE
      if ( keyInput.getCode() == KeyCode.SPACE) {
        if(!isThreadRunning){Sound.start.play();}
        try {
          isGameStarted = true;
          updateProgress();
        } catch (InterruptedException ex) {
          Logger.getLogger(match3Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      // on ESCAPE 
      if ( keyInput.getCode() == KeyCode.ESCAPE) {
        Sound.error.play();
        try {//------------RESET THE GAME
          resetGame();
        } catch (IOException ex) {
          Logger.getLogger(match2Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      //Player's inputs as 0 and 1
      if(isGameStarted ) {
        for(Player checkingPlayer : Player.players) {
          if (      keyInput.getCode() == checkingPlayer.key1) {
            Sound.typeWriter.play();
            checkingPlayer.key += "0"; checkingPlayer.updateBitsUsed();
            if(isSendingPhase){ sentPackageAnimation(checkingPlayer, Color.DARKORANGE); }
          }
          else if ( keyInput.getCode() == checkingPlayer.key2) {  
            Sound.typeWriter.play();
            checkingPlayer.key += "1"; checkingPlayer.updateBitsUsed();
            if(isSendingPhase){ sentPackageAnimation(checkingPlayer, Color.PURPLE);}
          }
          //create dots animation
          //update player's key label
          checkingPlayer.updateValue();
        }
      }
    });
  }
  public static void printPlayersKeys(){
    //print out current Player's keys
    int index = 0;
    for(Player checkingPlayer : Player.players) {
      try {        System.out.println(Player.players.indexOf(checkingPlayer) + ": " + checkingPlayer.key);      } 
      catch (Exception e) {        System.err.println("cannot print PlayerKeys" + index);      }
      index++;
    }
  }
  
  private void sentPackageAnimation(Player player, Color color) {
    switch (Player.players.indexOf(player)) {
      case 0: createDots(point1, point2, color); break;
      case 1: createDots(point2, point3, color); break;
      case 2: createDots(point3, point1, color); break;
    }
  }
  private void createDots(Node currentNode, Node targetNode, Color color){
    //create a little dot then move it from current Node to target Node
    ObservableList<Node> childrenPane = mainPane.getChildren();
    Circle packageCircle = new Circle(0, 0, 2, color);
    childrenPane.add(packageCircle);
    
    match3Controller.moving = new TranslateTransition(Duration.millis(500), packageCircle);
    moving.setAutoReverse(true);moving.setCycleCount(1);
    moving.setFromX(currentNode.getLayoutX()  +  20);
    moving.setFromY(currentNode.getLayoutY()  +  20);
    moving.setToX(  targetNode.getLayoutX()   +  20);
    moving.setToY(  targetNode.getLayoutY()   +  20);    
    moving.play(); 
    
    moving.setOnFinished(ActionEvent -> {
        childrenPane.remove(packageCircle); 
      });
  }
}
