/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eraofbinary;

import javafx.concurrent.Task;

/**
 *
 * @author haing
 */
public class timerTask extends Task<Void> {
  private int currentSeconds, second1, second2, chosenSecond = 1;
  
  public timerTask(int seconds){
    this.currentSeconds = seconds;
  }
  public timerTask(int second1, int second2){
    this.second1 = second1;
    this.second2 = second2;
    this.chosenSecond = second2;
  }
  
  private void swapSeconds(){
    if (chosenSecond == second1){
      chosenSecond = second2;
    } else {
      chosenSecond = second1;
    }
  }
  
  @Override
  protected Void call()throws Exception{
    System.out.print("timerTask ");
    
    swapSeconds();
    
    currentSeconds = chosenSecond;
    
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis() + (currentSeconds * 1000);
    long runningTime = startTime, totalTime = endTime - startTime;
    long temp = startTime + 1000;
    
    System.out.println(currentSeconds);
    
    while (runningTime < endTime ) {
      if ( runningTime == temp ) {
        currentSeconds--;
        temp += 1000;
      System.out.println(currentSeconds);
      }
      updateProgress(runningTime-startTime, totalTime);
      runningTime = System.currentTimeMillis();
    }
    System.out.print("end while loop ");
    if (runningTime>=endTime) {
      System.out.println("run again");
      call();
    }

    return null;
  }
  @Override
  protected void failed(){
    System.out.println("failed");
  }
  @Override
  protected void succeeded(){
    System.out.println("finished");
  }
}
