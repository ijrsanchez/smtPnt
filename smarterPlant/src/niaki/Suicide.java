package niaki;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author jara
 */
public class Suicide {
    
    
    
    Timer timer;
    SuicideTask mySuicideTask;
    HiloNodo myVictim;

    public Suicide(HiloNodo aVictim) {
        myVictim = aVictim;
        timer = new Timer();
        mySuicideTask = new SuicideTask();
	}
    
    void schedule() {                
        timer.schedule(mySuicideTask, 1000000); 
    }
    
    void cancel() {
        timer.cancel();
    }

    class SuicideTask extends TimerTask {
        public void run() {
            System.out.println("SuicideTimer: Time's up! Morirás "+myVictim.xbeeAddress64String);
            timer.cancel(); //Terminate the timer thread
            myVictim.myMonitor.put(2); //muere!
        }
    }

}
