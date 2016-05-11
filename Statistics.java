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
 * Record and update game statistic by reading and writing text file
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
class WonPlayer extends Player{
  private static ArrayList<WonPlayer> wonPlayers = new ArrayList<>();

  public WonPlayer(String playerName) {
    super(playerName);
  }
  public static ArrayList<WonPlayer> getList(){
    return wonPlayers;
  }
  public static void addToList(WonPlayer player){
    wonPlayers.add(player);
  }
  public static void clear(){
    wonPlayers.clear();
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
  static final File README_FILE = new File("Readme.txt");
  static final File LOG_FILE = new File("log.txt");
  static final File statistic = new File("statistic.txt");
  public static void loadUserData() throws FileNotFoundException{
    //load info from statistic file to compare and update in the future
    try {
      Scanner input = new Scanner(statistic);
      while(input.hasNextLine()) {
        String ignore = input.nextLine();
        String highestWinningPointString = input.nextLine();
        String higestBitsUsedString = input.nextLine();
        String higestBitsSentString = input.nextLine();
        ignore = input.nextLine();// the line "won players"
        
        String wonPlayer = " ";
        while (input.hasNextLine()) {
          try { wonPlayer = input.nextLine(); } catch (NullPointerException e) {break;          }
          if (wonPlayer.length() > 1) {
          WonPlayer.addToList( new WonPlayer(wonPlayer));
          }
        }
        System.out.println("Won Player list read from file successfully");

        highestWinningPointString = highestWinningPointString.substring("Highest winning point: ".length());
        highestWinningPoint = Integer.parseInt(highestWinningPointString);
        System.out.println("  Highest winning points: " +  highestWinningPoint);

        higestBitsUsedString = higestBitsUsedString.substring("Highest bits used:     ".length());
        highestBitsUsed = Integer.parseInt(higestBitsUsedString);
        System.out.println("  Highest bits used: " +  highestBitsUsed);

        higestBitsSentString = higestBitsSentString.substring("Highest bits sent:     ".length());
        highestBitsSent = Integer.parseInt(higestBitsSentString);
        System.out.println("  Highest bits sent: " +  highestBitsSent);
        
        System.out.print("Current Players: " );
        Player.players.stream().forEach((checkingPlayer) -> {
          System.out.print(checkingPlayer.name + ", ");
        });
        System.out.println();
      } 
    } catch (FileNotFoundException | NumberFormatException e) {
      System.err.println("File statistic.txt is not available or contains error or first time running");
    }
  }
  public static void compareStatistic(){
    //compare with the old data then update statistic
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
      statisticUpdater.write("Highest bits used:     " + highestBitsUsed + System.lineSeparator());
      statisticUpdater.write("Highest bits sent:     " + highestBitsSent + System.lineSeparator());
      statisticUpdater.write("Won Players:     " + System.lineSeparator());
      
      for(WonPlayer checkingPlayer : WonPlayer.getList()) {
        statisticUpdater.write(checkingPlayer.name + System.lineSeparator());
      }
      statisticUpdater.close();
    } catch (IOException ex) {
      System.err.println("Can't write to file statistic.txt");
      Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  public static void information() throws IOException{
    try ( //create information text file of the application
            FileWriter informationWriter = new FileWriter(README_FILE, false)) {
      informationWriter.write(
      " * Title: Era of Binary \n" + System.lineSeparator() +
      " * Author: Hai Nguyen \n" + System.lineSeparator() +
      " * Version: v2\n" + System.lineSeparator() +
      " * Last updated: May 2, 2016\n" + System.lineSeparator() +
      " * Description:\n" + System.lineSeparator() +
      " *  This is a simple game which was inspired by Binary and Internet\n" + System.lineSeparator() +
      " *  The game currently is designed to be played by 2 or 3 people in the same keyboard\n" + System.lineSeparator() +
      " * \n" + System.lineSeparator() +
      " * How to Play: \n" + System.lineSeparator() +
      " *  each Player has 2 keys to represent two bits ( 0 and 1 )\n" + System.lineSeparator() +
      " *  the game has two phases: Sending and Decoding\n" + System.lineSeparator() +
      " *  - in Sending phase, each player try to tab his 2 keys to create \n" + System.lineSeparator() + 
      " *    \"a package of random bits\" which will be sent to other in the end of phase\n" + System.lineSeparator() +
      " *  - in Sending phase, when players receive their packages, they need to use \n" + System.lineSeparator() +
      " *    those keys to type exactly the same bits as they received\n" + System.lineSeparator() +
      " *    + if player types the correct package, he would receive one point\n" + System.lineSeparator() +
      " *    + if the package was typed incorrectly, one point will be sent to that \n" + System.lineSeparator() +
      " *      package owner.\n" + System.lineSeparator() +
      " *  Game will repeat these two phases consecutively until \n" + System.lineSeparator() +
      " *  someone's point reaches the Winning point, which was chosen at the game starts\n" + System.lineSeparator() +
      " * \n" + System.lineSeparator() +
      " * Shortcut keys:\n" + System.lineSeparator() +
      " *  - SPACE to start the game\n" + System.lineSeparator() +
      " *  - ESC   to reset the game after finished");
    }
  }
}
