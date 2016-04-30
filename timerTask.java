/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import javafx.concurrent.Task;

/**
 * this class is extended from Task,
 * purpose: create an upđatable timer to binded with ProgressIndicator
 *          timer can swap between 2 specific periods of time 
 * @author haing
 */
public class timerTask extends Task<Void> {
  private int currentSeconds, second1, second2, chosenSecond = 1;
  private final boolean is2PhasesMode;
  
  public timerTask(int seconds){
    this.chosenSecond = seconds;
    this.is2PhasesMode = false;
  }
  public timerTask(int second1, int second2){
    this.second1 = second1;
    this.second2 = second2;
    this.chosenSecond = second2;
    is2PhasesMode = true;
  }
  
  private void swapSeconds(){
    if (chosenSecond == second1){
      chosenSecond = second2;
    } else {
      chosenSecond = second1;
    }
  }
  private void swapMode(){
    //swap between SENDING mode and DECODING mode
    FXMLDocumentController.isSendingPhase = chosenSecond == second1;
  }
  
  @Override
  protected Void call()throws Exception{
    System.out.println("Task Started");
    
    if(is2PhasesMode){
    //swap between 2 periods of time so timer counts different each turn
      swapSeconds();
    //swap between 2 modes depending on the current chosen time
      swapMode();
    }
    
    currentSeconds = chosenSecond;
    
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis() + (currentSeconds * 1000);
    long runningTime = startTime, totalTime = endTime - startTime;
    long temp = startTime + 1000;
    
    System.out.println("isSendingPhase? " + FXMLDocumentController.isSendingPhase);
    System.out.print(currentSeconds + " ");
    
    //count each second
    while (runningTime < endTime ) { 
      if ( runningTime == temp ) {
        currentSeconds--;
        temp += 1000;
      System.out.print(currentSeconds + " ");
      }
      updateProgress(runningTime-startTime, totalTime);
      runningTime = System.currentTimeMillis();
    }
    System.out.print("end while loop ");
    
    //
      if(FXMLDocumentController.isSendingPhase) {
      Player.routeData();
      Player.clearPlayerKeys();
    } else{
      Player.countPoint();
      Player.clearPlayerReceived();
      Player.clearPlayerKeys();
    }
    Player.updateUI();
    
    //repeat the counter
    if (runningTime>=endTime) {
      System.out.println("run again");
      System.out.println("---------------------------------");
      
      if(!Player.hasWinner()) {
        call();      
      } else {
        System.out.println("loading");
        Statistics.loadUserData();
        System.out.println("finished");
        Statistics.compareStatistic();
        
        System.out.print("saving");
        Statistics.saveUserData();
        System.out.println(" finished");
      }
    }
    return null;
  }
  @Override
  protected void failed(){
    System.out.println("Task failed");
  }
  @Override
  protected void succeeded(){
    System.out.println("Task finished");
  }
}
