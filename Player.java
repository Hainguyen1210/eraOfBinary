package eraofbinary;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.KeyCode;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 * Functions of the class:
  * create players
  * handle with both 2P and 3P modes 
  * hold the players in an array
  * bind player's info with TextProperties
  * update Player's properties: dataSent, dataReceived, Points, routeData
 */
public class Player {
  public static ArrayList<Player> players = new ArrayList<>();
  private StringProperty keyProperty = new SimpleStringProperty("Key Field");
  private StringProperty receivedProperty = new SimpleStringProperty("Received Field");
  private StringProperty pointProperty = new SimpleStringProperty("#");
  public static boolean is3Players = false;
  public String 
          key="",
          received="",
          name;
  private int
          bitsUsed = 0,
          bitsSent = 0,
          currentPoint = 0;
  public KeyCode key1, key2;
  
  public Player(String playerName){
    this.name = playerName;
  }
  public Player(String playerName, KeyCode key1, KeyCode key2){
    this.name = playerName;
    this.key1 = key1;
    this.key2 = key2;
  }
  public Player(String playerName, 
          Label keyLabel, Label receivedLabel, Label points,
          KeyCode key1, KeyCode key2){
    this.name = playerName;
    this.key1 = key1;
    this.key2 = key2;
//    if(receivedLabel == null) {
//      System.out.println("eraofbinary.Player.<init>()");
//    }
    try {
      keyLabel.textProperty().bind(this.keyProperty);
      receivedLabel.textProperty().bind(this.receivedProperty);
      points.textProperty().bind(this.pointProperty);
    } catch (NullPointerException e) {
      System.err.println("can not bind key label and received Label");
    }
  }
 
  public final void updateBitsUsed(){
    this.bitsUsed++;
  }
  public final void updateBitsSent(int bitsSent){
    this.bitsSent += bitsSent;
  }
  public final String getBitsUsedString(){
    return Integer.toString(this.bitsUsed);
  }
  public final String getBitsSentString(){
    return Integer.toString(this.bitsSent);
  }
  public final int getBitsUsed(){
    return this.bitsUsed;
  }
  public final int getBitsSent(){
    return this.bitsSent;
  }
  
  public final void updateValue(){
    this.keyProperty.set(this.key);
    this.receivedProperty.set(this.received);
    this.pointProperty.set(Integer.toString(this.currentPoint));
  }
  public static void clearPlayerKeys(){
    //clear all player's keys 
    try {
      for(Player checkingPlayer : players) {
        //clear Player's keys
        checkingPlayer.key = "";        
      }
    } catch (Exception e) {
      System.err.println("failed to clear Key");
    }
  }
  public static void clearPlayerReceived(){
    //clear all player's Received bits
    try {
      for(Player checkingPlayer : players) {
        //clear Player's Received bits
        checkingPlayer.received = "";        
      }
    } catch (Exception e) {
      System.err.println("failed to clear Received Keys");
    }
  }
  public static void routeData(){
    //route bits between players in a circle
    int bitsAvailable = 0; //check if there is bits to route
    int numberOfplayers = 0;
    if(is3Players) { numberOfplayers = 3; }
    else { numberOfplayers = 2; }
    
    //collect sent bits
    for(int i = 0; i < numberOfplayers; i++) {
      int bitsSent = players.get(i).key.length();
      players.get(i).updateBitsSent(bitsSent);
    }
    //swap Player's keys
    try {
      String [] sentData;
      if(is3Players) {
        sentData = new String[] {players.get(0).key, players.get(1).key, players.get(2).key};
      } else {
        sentData = new String[] {players.get(0).key, players.get(1).key};
      }
      
      //print out array
      System.out.println("  Player's keys: ");
      int count = 1;
      for(String key: sentData) {
        System.out.println("  " + count + " \\ " + key);
        count++;
      }
      
      for(Player checkingPlayer : players) {
        int index = players.indexOf(checkingPlayer);
        try {
          checkingPlayer.received = sentData[index-1];
        } catch (ArrayIndexOutOfBoundsException e) {
          checkingPlayer.received = sentData[players.size() - 1];
        }
        bitsAvailable += checkingPlayer.received.length();
      }
      if(bitsAvailable > 0) { Sound.power.play(); }
      System.out.println("Routing finished");
    } catch (Exception e) {
      System.err.println("Routing failed");
    }
  }
  public static void countPoint(){
    //Count point
    Player previousPlayer;
    int index = 0;
    for(Player checkingPlayer : players) {
      //get previous Player
      try{        previousPlayer = players.get(index-1);      }
      catch (ArrayIndexOutOfBoundsException e) {
        previousPlayer = players.get(players.size()-1);
      }
      index++;
      
      // count point only when players input some bits
      if( !checkingPlayer.received.equals("") ) {
        Sound.coin.play();
        if(checkingPlayer.key.equals(checkingPlayer.received)){
          checkingPlayer.currentPoint++;
        }else{
          previousPlayer.currentPoint++;
        }
      }
      
    }
    //Print out Player's points
    System.out.println("  Player's points:");
    int count = 1;
    for(Player checkingPlayer : players) {
      System.out.println("  " + count + "  // " + checkingPlayer.currentPoint);
      count++;
    }
    System.out.println("Count finished");
  }
  public static void updateUI(){
    for(Player checkingPlayer : players) {
      System.out.println(
        Player.players.indexOf(checkingPlayer) + " ||" 
        + " bitsUsed: " + checkingPlayer.bitsUsed + " bits Sent: " + checkingPlayer.bitsSent
      );
      //update UI
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          try {
            checkingPlayer.updateValue();
          } catch (Exception e) {
            System.err.println("cant set Value");
          }
        }
      });
    }
  }
  public static boolean hasWinner(){
    int winningPoint = 5;
    ArrayList<Player> highestPointPlayers = new ArrayList<>();
    if (is3Players) { winningPoint = match3Controller.winningPoint;  }
    else{ winningPoint = match2Controller.winningPoint;  }
    
    //filter Players who have reach the winning point
    for (Player checkingPlayer : players) {
      if(checkingPlayer.currentPoint >= winningPoint) {
        highestPointPlayers.add(checkingPlayer);
      }
    }
    int point1, point2, point3;
    switch(highestPointPlayers.size()){
      case 0: //There is no one reaches the winningPoint
        return false;
      case 1: //There is one Player reaches the winningPoint -> Winner
        //add to winners list
        WonPlayer.addToList(new WonPlayer(highestPointPlayers.get(0).name));
        updateWinnerUI("Winner", highestPointPlayers.get(0));
        return true;
      case 2: //There is Two Players reach the winningPoint
              // Compare Who has higher point
        point1 = highestPointPlayers.get(0).currentPoint;
        point2 = highestPointPlayers.get(1).currentPoint;
        if(point1 == point2) {
          updateWinnerUI("Draw", highestPointPlayers.get(0));
          updateWinnerUI("Draw", highestPointPlayers.get(1));
          return true;
        } else if (point1 > point2){
        //add to winners list
        WonPlayer.addToList(new WonPlayer(highestPointPlayers.get(0).name));
          updateWinnerUI("Winner", highestPointPlayers.get(0));
          return true;
        } else {
        //add to winners list
        WonPlayer.addToList(new WonPlayer(highestPointPlayers.get(1).name));
          updateWinnerUI("Winner", highestPointPlayers.get(1));
          return true;
        }
      case 3: //when there is 3 player reach the winningPoint
              //they will have the same point -> game is draw
        for(Player checkingPlayer : highestPointPlayers) {
          updateWinnerUI("Draw", checkingPlayer);
        }
        return true;
    }
    for(Player checkingPlayer : players) {
      updateWinnerUI("Bug", checkingPlayer);
    }
    return true;
  }
  private static void updateWinnerUI(String winOrDrawString, Player winner){
  //update winner name to th UI
    Platform.runLater(new Runnable() {
    @Override public void run() {
      winner.receivedProperty.set(winOrDrawString);
      winner.keyProperty.set(winner.name);
    }
    });
  }
}
