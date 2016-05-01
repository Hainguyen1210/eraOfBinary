/**
 * Title: Era of Binary
 * Description:
 *  This is a simple game which was inspired by Binary and Internet
 *  The game currently is designed to be played by 2 or 3 people in the same keyboard
 * 
 * How to Play: 
 *  each Player has 2 keys to represent two bits ( 0 and 1 )
 *  the game has two phases: Sending and Decoding
 *  - in Sending phase, each player try to tab his 2 keys to create 
 *    "a package of random bits" which will be sent to other in the end of phase
 *  - in Sending phase, when players receive their packages, they need to use 
 *    those keys to type exactly the same bits as they received
 *    + if player types the correct package, he would receive one point
 *    + if the package was typed incorrectly, one point will be sent to that 
 *      package owner.
 *  Game will repeat these two phases consecutively until 
 *  someone's point reaches the Winning point, which is chosen at the game starts
 * 
 * Author: Hai Nguyen
 * Version: v2
 * Current functions:
 *  # 2 phases of Timer: Sending phase and Decoding phase
 *  # timer-binded progress Indicator (change speed and color)
 *  # 2 play modes: 2P or 3P
 *  # Sound effect
 *  # animation while sending bits
 *  # Record Statistic
 * Sound effect: YouTube.
 * Images: self designed
 */
package eraofbinary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author haing
 */
public class EraOfBinary extends Application {
  
  @Override
  public void start(Stage stage) throws Exception {
    Sound.windowsStart.play();
    Parent root = FXMLLoader.load(getClass().getResource("chooseMode.fxml"));
    Scene scene = new Scene(root);
    stage.setTitle("Era Of Binary");
    stage.setScene(scene);
    stage.show();
  }
  @Override
  public void stop(){
    System.err.println("window is closing");
  }
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}
