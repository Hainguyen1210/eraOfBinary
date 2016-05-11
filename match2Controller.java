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
 * Controller of 2P mode
 * Set the game:
 *  - Winning point
 *  - Player names
 * start Counter task
 * get Player's inputs
 * Animation
 */
public class match2Controller implements Initializable {
  public static TranslateTransition moving; //for animation
  private timerTask counter;
  private Thread currentThread;
  public static boolean isSendingPhase, isThreadRunning, isGameStarted;
  
  @FXML private AnchorPane mainPane, instructionPane;
  @FXML private Label 
          player1NameLabel, player2NameLabel,
          player1KeyLabel, player2KeyLabel,
          player1ReceivedLabel, player2ReceivedLabel, 
          player1PointLabel, player2PointLabel, 
          winningPointLabel;
  @FXML private TextField player1NameField, player2NameField ;
  @FXML private TextField winningPointField;
  @FXML public ProgressIndicator progressIndicator;
  public static Label a, b, sent1, sent2, point1, point2;
  public static int winningPoint;
  public static String[] colors = {"-fx-progress-color: red;", "-fx-progress-color: blue;" };
  public static int colorIndex;
  public static StringProperty indicatorColor;
  
  @Override  public void initialize(URL url, ResourceBundle rb) {
    isSendingPhase = true;
    isThreadRunning = false;  
    isGameStarted = false;
    indicatorColor = new SimpleStringProperty("-fx-progress-color: red;");
    colorIndex = 0;
    Sound.playBackgoundSound();
    a = player1KeyLabel; sent1 = player1ReceivedLabel; point1 = player1PointLabel;
    b = player2KeyLabel; sent2 = player2ReceivedLabel; point2 = player2PointLabel;
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
    //swap the colors of progress indicator
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
    //reset the game's tepmporary data and reload the fxml file
    Player.players.clear();
    if(isThreadRunning) {
    counter.cancel(false);
    currentThread.stop();
    counter = null;
    currentThread = null;
    }
    
    Parent root = FXMLLoader.load(getClass().getResource("match2.fxml"));
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
    String player1Name="", player2Name="";
    String[] playerNameStrings = {player1Name, player2Name};
    TextField[] playerNameFields = {player1NameField, player2NameField};
    Label[] playerNameLabels = {player1NameLabel, player2NameLabel};
    
    for(int i = 0; i < 2; i++){
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
    
    //create Players
    Player.players.add(0, new Player(playerNameStrings[0], a, sent1, point1, KeyCode.Z, KeyCode.X));
    System.out.println(" checking------------------1");
    Player.players.add(1, new Player(playerNameStrings[1], b, sent2, point2, KeyCode.N, KeyCode.M));
    System.out.println(" checking------------------2");
    

    try {
      
    //Display Player's Names 
    for(int i = 0; i < 2; i++) {
      playerNameLabels[i].setText(Player.players.get(i).name);
    }
    //Hide the Instruction Panne
    instructionPane.setVisible(false);
    progressIndicator.setVisible(true);
      System.out.println(" checking-----------------FINISHED");
    } catch (Exception e) {
      System.out.println(" checking-----------------FAILED");
    }
  }
  @FXML private void getWinningPoint(){
    //get Player's desired winning points
    try {
      int checkingPoint;
      checkingPoint = Integer.parseInt(winningPointField.getText());
      if(checkingPoint <= 99) {
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
          Logger.getLogger(match2Controller.class.getName()).log(Level.SEVERE, null, ex);
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
      if(isGameStarted && !Player.hasWinner() ) {
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
      case 1: createDots(point2, point1, color); break;
    }
  }
  private void createDots(Node currentNode, Node targetNode, Color color){
    //create a little dot then move it from current Node to target Node
    ObservableList<Node> childrenPane = mainPane.getChildren();
    Circle packageCircle = new Circle(0, 0, 4, color);
    childrenPane.add(packageCircle);
    
    int pointPosition = 20;
    if(currentNode == point1) { pointPosition = 0; }
    else { pointPosition = 60; }
    
    
    match2Controller.moving = new TranslateTransition(Duration.millis(500), packageCircle);
    moving.setAutoReverse(true);moving.setCycleCount(1);
    moving.setFromX(currentNode.getLayoutX()  +  43);
    moving.setFromY(currentNode.getLayoutY()  +  pointPosition);
    moving.setToX(  targetNode.getLayoutX()   +  43);
    moving.setToY(  targetNode.getLayoutY()   +  pointPosition);    
    moving.play(); 
    
    moving.setOnFinished(ActionEvent -> {
        childrenPane.remove(packageCircle); 
      });
  }
}
