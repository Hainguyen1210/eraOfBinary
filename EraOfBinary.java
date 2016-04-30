/**
 * Title: Era of Binary
 * Description:
 *  This is a simple game which was inspired by Binary and Internet
 *  The game currently is designed to be played by 3 people in the same keyboard
 * Author: Hai Nguyen
 * Version: v1
 * Current functions:
 *  # 2 phases of timer with different specific time
 *  # working progress Indicator binded with timer
 * 
 * Sound effect: YouTube.
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
    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
    Scene scene = new Scene(root);
    stage.setTitle("Era Of Binary");
    stage.setScene(scene);
    Sound.windowsStart.play();
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
