public class Philosopher implements Runnable {
	// define and initialize myId, eat times, monitor and threads
    private int myId;
    private int timesMax;
    private Monitor monitor;
    public Thread t;
    Philosopher(int myId, int timesMax, Monitor m){
        this.myId = myId;
        this.timesMax = timesMax;
        this.monitor = m;
        t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        
        for(int i = 1; i < timesMax; i++) {
        	// get forks first
        	// eat food second
        	// put down forks third
            monitor.takeForks(myId);
            eat(i);
            monitor.putDownForks(myId);
        }
    }

    public void eat(int count){
        System.out.format("The philosopher %d eat (%d times)\n\r", myId+1, count);
        try {
            Thread.sleep(10);
        }
        catch (InterruptedException e) {}
    }


}