/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haing
 */
class AllPlayers extends Player{
  private int winningPoint=0;
  public AllPlayers(String playerName) {
    super(playerName);
  }
  public int getWinningPoint(){
    return this.winningPoint;
  }
  public void setWinningPoint(int winningPoint) {
    this.winningPoint = winningPoint;
  }
}
class dateProvider{
  public static final String get() {
    DateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //get current date time with Date()
    Date date = new Date();
    return ft.format(date);
  }
  
}
public class Statistics {
  static int highestWinningPoint = 0, highestBitsUsed = 0 , highestBitsSent = 0;
  static final File LOG_FILE = new File("log.txt");
  static final File statistic = new File("statistic.txt");
  public static void loadUserData() throws FileNotFoundException{
    try {
      Scanner input = new Scanner(statistic);
      while(input.hasNextLine()) {
        String time = input.nextLine();
        String highestWinningPointString = input.nextLine();
        String higestBitsUsedString = input.nextLine();
        String higestBitsSentString = input.nextLine();

        highestWinningPointString = highestWinningPointString.substring("Highest winning point: ".length());
        highestWinningPoint = Integer.parseInt(highestWinningPointString);
        System.out.println("  Highest winning points: " +  highestWinningPoint);

        higestBitsUsedString = higestBitsUsedString.substring("Highest bits used:     ".length());
        highestBitsUsed = Integer.parseInt(higestBitsUsedString);
        System.out.println("  Highest bits used: " +  highestBitsUsed);

        higestBitsSentString = higestBitsSentString.substring("Highest bits sent:     ".length());
        highestBitsSent = Integer.parseInt(higestBitsSentString);
        System.out.println("  Highest bits sent: " +  highestBitsSent);
      } 
    } catch (FileNotFoundException | NumberFormatException e) {
      System.err.println("File statistic.txt is not available or contains error or first time running");
    }
  }
  public static void compareStatistic(){
    int winningPoint = 5;
    if(Player.is3Players) { winningPoint = match3Controller.winningPoint;  } 
    else { winningPoint = match2Controller.winningPoint ; }
    
    if( winningPoint > highestWinningPoint) {
      highestWinningPoint = winningPoint;
    }
    for(Player checkingPlayer : Player.players) {
      if( checkingPlayer.getBitsUsed() > highestBitsUsed) {
        highestBitsUsed = checkingPlayer.getBitsUsed();
      }
      if( checkingPlayer.getBitsSent()> highestBitsSent) {
        highestBitsSent = checkingPlayer.getBitsSent();
      }
    }
  }
  public static void saveUserData(){
    /**
     * this method will get save the statistics of all Players into text files
    */
    //save log APPEND
    try {
      FileWriter logger = new FileWriter(LOG_FILE, true);
      logger.write("Time: " + dateProvider.get() + System.lineSeparator());
      logger.write("Winnning point: " + match3Controller.winningPoint + System.lineSeparator());
      logger.write("Player's name: " + System.lineSeparator());
      for (Player exportingPlayers: Player.players) {
        logger.write("      " + exportingPlayers.name + " " );
        logger.write(
          "Bits Used: " + exportingPlayers.getBitsUsedString() +
          " " + 
          "Bits Sent: " + exportingPlayers.getBitsSentString() + 
          System.lineSeparator() );
      }
      logger.close();
    } catch (IOException ex) {
      System.err.println("Can't write to file log.txt");
      Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
    }
    //collect statistic OVERWRITING
    try {
      FileWriter statisticUpdater = new FileWriter(statistic, false);
      statisticUpdater.write("Last updated: " + dateProvider.get() + System.lineSeparator());
      statisticUpdater.write("Highest winning point: " + highestWinningPoint + System.lineSeparator());
      statisticUpdater.write("Highest bist used:     " + highestBitsUsed + System.lineSeparator());
      statisticUpdater.write("Highest bist sent:     " + highestBitsSent + System.lineSeparator());
      statisticUpdater.close();
    } catch (IOException ex) {
      System.err.println("Can't write to file statistic.txt");
      Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
