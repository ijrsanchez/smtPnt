package niaki;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



public class Monitor {
    private int wassanow;		// shared data

/* Method used by the consumer to access the shared data */
    public synchronized int get() {	
      try {
        wait();		// Consumer enters a wait state until notified by the Producer
      } catch (InterruptedException e) { 
    	  e.printStackTrace();
      }
      return wassanow;	
    }

/* Method used by the consumer to access (store) the shared data */
    public synchronized void put(int value) {
        wassanow = value;
        notifyAll();		// Producer notifies Consumer to come out 
				// of the wait state and consume the contents
    }
}
