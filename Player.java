/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import javafx.scene.input.KeyCode;

/**
 *
 * @author haing
 */
public class Player {
  public static Player[] players;
  public String 
          key="",
          name;
  public int
          currentPoint = 0;
  public KeyCode key1, key2;
  
  public Player(String playerName, KeyCode key1, KeyCode key2){
    this.name = playerName;
    this.key1 = key1;
    this.key2 = key2;
  }
}
