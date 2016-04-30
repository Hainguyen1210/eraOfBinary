/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import javafx.scene.media.AudioClip;
import static javafx.scene.media.AudioClip.INDEFINITE;

/**
 *
 * @author haing
 */
public class Sound {
  private final static AudioClip background = new AudioClip( Sound.class.getResource("sound/searching.wav").toExternalForm());
  public final static AudioClip coin = new AudioClip( Sound.class.getResource("sound/Pickup_Coin.wav").toExternalForm());
  public final static AudioClip power = new AudioClip( Sound.class.getResource("sound/Powerup.wav").toExternalForm());
  public final static AudioClip typeWriter = new AudioClip( Sound.class.getResource("sound/typewriter.wav").toExternalForm());
  public final static AudioClip start = new AudioClip( Sound.class.getResource("sound/start.wav").toExternalForm());
  public final static AudioClip thuglife = new AudioClip( Sound.class.getResource("sound/thugLife.wav").toExternalForm());
  public final static AudioClip windowsStart = new AudioClip( Sound.class.getResource("sound/windowsStart.wav").toExternalForm());
  public final static AudioClip error = new AudioClip( Sound.class.getResource("sound/windowsError.wav").toExternalForm());
  public final static void playBackgoundSound(){
    int s = INDEFINITE; 
    background.setCycleCount(s);
    background.stop();
    background.play();
  }
}
