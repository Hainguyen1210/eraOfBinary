package eraofbinary;

import java.util.Random;
import javafx.scene.media.AudioClip;
import static javafx.scene.media.AudioClip.INDEFINITE;

/**
 * create AudioClips by loading sound effect files 
 * they are able to be played anywhere outside of class
 * Create function to run the background sound repeatedly and consecutively
 * create function to generate random winning sound.
 */
public class Sound {
  
  public final static AudioClip background = new AudioClip( Sound.class.getResource("sound/searching.wav").toExternalForm());
  public final static AudioClip coin = new AudioClip( Sound.class.getResource("sound/Pickup_Coin.wav").toExternalForm());
  public final static AudioClip power = new AudioClip( Sound.class.getResource("sound/Powerup.wav").toExternalForm());
  public final static AudioClip typeWriter = new AudioClip( Sound.class.getResource("sound/typewriter.wav").toExternalForm());
  public final static AudioClip start = new AudioClip( Sound.class.getResource("sound/start.wav").toExternalForm());
  public final static AudioClip windowsStart = new AudioClip( Sound.class.getResource("sound/windowsStart.wav").toExternalForm());
  public final static AudioClip error = new AudioClip( Sound.class.getResource("sound/windowsError.wav").toExternalForm());
  
  public static AudioClip winning = null;
  private final static AudioClip winning0 = new AudioClip( Sound.class.getResource("sound/win0.wav").toExternalForm());
  private final static AudioClip winning1 = new AudioClip( Sound.class.getResource("sound/win1.wav").toExternalForm());
  private final static AudioClip winning2 = new AudioClip( Sound.class.getResource("sound/win2.wav").toExternalForm());
  private final static AudioClip winning3 = new AudioClip( Sound.class.getResource("sound/win3.wav").toExternalForm());
  private final static AudioClip winning4 = new AudioClip( Sound.class.getResource("sound/win4.wav").toExternalForm());
  private final static AudioClip winning5 = new AudioClip( Sound.class.getResource("sound/win5.wav").toExternalForm());
  private final static AudioClip winning6 = new AudioClip( Sound.class.getResource("sound/win6.wav").toExternalForm());
  private final static AudioClip winning7 = new AudioClip( Sound.class.getResource("sound/win7.wav").toExternalForm());
  private final static AudioClip winning8 = new AudioClip( Sound.class.getResource("sound/win8.wav").toExternalForm());
  
  public final static AudioClip [][] soundList = 
  { {winning0, winning1, winning2}, 
    {winning3, winning4, winning5}, 
    {winning6, winning7, winning8}
  };
  
  public final static void playRandomWinning(){
    Random random = new Random();
      winning = soundList[random.nextInt(3)][random.nextInt(3)];
      winning.play();
  }
  
  public final static void playBackgoundSound(){
    int s = INDEFINITE; 
    background.setCycleCount(s);
    background.stop();
    background.play();
  }
}
