/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.KeyCode;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 *
 * @author haing
 */
public class Player {
  public static ArrayList<Player> players = new ArrayList<>();
  private StringProperty keyProperty = new SimpleStringProperty("Key Field");
  private StringProperty receivedProperty = new SimpleStringProperty("Received Field");
  private StringProperty pointProperty = new SimpleStringProperty("#");
  
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
    if(receivedLabel == null) {
      System.out.println("eraofbinary.Player.<init>()");
    }
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
      System.out.println("Keys Cleared");
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
      System.out.println("Received Keys Cleared");
    } catch (Exception e) {
      System.err.println("failed to clear Received Keys");
    }
  }
  public static void routeData(){
    //collect sent bits
    for(int i = 0; i < 3; i++) {
      int bitsSent = players.get(i).key.length();
      players.get(i).updateBitsSent(bitsSent);
    }
    //swap Player's keys
    try {
      String [] sentData = {players.get(0).key, players.get(1).key, players.get(2).key};
      System.out.println(Arrays.toString(sentData));
      for(Player checkingPlayer : players) {
        int index = players.indexOf(checkingPlayer);
        try {
          checkingPlayer.received = sentData[index-1];
        } catch (ArrayIndexOutOfBoundsException e) {
          checkingPlayer.received = sentData[players.size() - 1];
        }
      }
    } catch (Exception e) {
      System.err.println("route failed");
    }
  }
  public static void countPoint(){
    for(Player checkingPlayer : players) {
      if(
        !checkingPlayer.key.equals("") && 
        !checkingPlayer.received.equals("") && 
        checkingPlayer.key.equals(checkingPlayer.received)
        ) 
      {
        checkingPlayer.currentPoint++;
      }
    }
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
    for (Player checkingPlayer : players) {
      if(checkingPlayer.currentPoint == FXMLDocumentController.winningPoint) {
        return true;
      }
    }
    return false;
  }
}
