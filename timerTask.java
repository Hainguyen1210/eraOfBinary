package eraofbinary;

import javafx.concurrent.Task;

/**
 * this class is extended from Task,
 * purpose: create an upđatable timer to binded with ProgressIndicator
 *          timer can swap between 2 specific periods of time
 * read function call() for more information
 */
public class timerTask extends Task<Void> {
  private int rounds = 0, currentSeconds, second1, second2, chosenSecond = 1;
  private final boolean is2PhasesMode;
  private boolean isSendingPhase;
  
  //constructors
  public timerTask(int seconds){
    this.chosenSecond = seconds;
    this.is2PhasesMode = false;
    
    if(Player.is3Players) {this.isSendingPhase = match3Controller.isSendingPhase;}
    else { this.isSendingPhase = match2Controller.isSendingPhase;}
  }
  public timerTask(int second1, int second2){
    this.second1 = second1;
    this.second2 = second2;
    this.chosenSecond = second2;
    is2PhasesMode = true;
    
    if(Player.is3Players) {this.isSendingPhase = match3Controller.isSendingPhase;}
    else { this.isSendingPhase = match2Controller.isSendingPhase;}
  }
  
  private void swapSeconds(){
    //toggle between 2 periods of time
    if (chosenSecond == second1){
      chosenSecond = second2;
    } else {
      chosenSecond = second1;
    }
  }
  private void swapMode(){
    //swap between SENDING mode and DECODING mode
    this.isSendingPhase = chosenSecond == second1;
    match2Controller.isSendingPhase = this.isSendingPhase;
    match3Controller.isSendingPhase = this.isSendingPhase;
    
  }
  
  @Override  protected Void call()throws Exception{
    /** create Phase counter 
     * sending   phase counter: second1 (currently set to 1 second)
     * receiving phase counter: second2 (currently set to 3 seconds)
     * Counter will switch between 2 phases by turn
     * at the end of sending phase, data will be routed like ring topology
     * at the end of decoding phase, counter checks if data is decoded and count point
     * the cycle ends when the game has the winner, whose point reaches the winning point
     */
    
    
    if(is2PhasesMode){
    //swap between 2 periods of time so timer counts different each turn
      swapSeconds();
    //swap between 2 modes depending on the current chosen time
      swapMode();
    }
    
    if(this.isSendingPhase) {this.rounds++;}
      System.out.print("Round " + this.rounds + " - ");
    
    currentSeconds = chosenSecond;
    
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis() + (currentSeconds * 1000);
    long runningTime = startTime, totalTime = endTime - startTime;
    long temp = startTime + 1000;
    
    if(this.isSendingPhase) {
      System.out.println("Sending phase");
    } else {
      System.out.println("Decoding phase");
    }
    System.out.print("remaining seconds: ");
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
    
    //rout packages and count points
      if(this.isSendingPhase) {
    System.out.println("\nRouting");
      Player.routeData();
      Player.clearPlayerKeys();
    } else{
    System.out.println("\nCounting point");
      Player.countPoint();
      Player.clearPlayerReceived();
      Player.clearPlayerKeys();
    }
      
    //Change Progress Indicator's color
    if(Player.is3Players) { match3Controller.swapIndicatorColor(); }
    else { match2Controller.swapIndicatorColor(); }
    
    Player.updateUI();
    
    //repeat the counter
    if (runningTime>=endTime) {
      System.out.println("-----run again-----------------");
      
      if(!Player.hasWinner()) {
        call();      
      } else {
        Sound.winning.play();
        System.out.println("loading");
        Statistics.loadUserData();
        System.out.println("finished");
        Statistics.compareStatistic();
        
        System.out.print("saving");
        Statistics.saveUserData();
        System.out.println(" finished");
        while(Sound.winning.isPlaying()){
         Thread.sleep(200);
         Player.hasWinner();
        }
      }
    }
    return null;
  }
  @Override  protected void failed(){
    System.out.println("Task failed");
  }
  @Override  protected void succeeded(){
    System.out.println("Task finished");
  }
}
