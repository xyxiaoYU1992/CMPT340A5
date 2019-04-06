public class Main {
    private Thread t;
    public static void main(String[] args) {
    	//create new monitor to handle philosopher problem
        Monitor monitor = new Monitor();
        //create 5 philosophers and then pass 5 monitors into it
        Philosopher[] p = new Philosopher[5];
        int timeMax = 5;
        
        //the philosopher's most eat time less than 5 times.
        //this 5(second argument of philosopher) can be changed
        for (int i = 0; i < 5; i++)
            p[i] = new Philosopher(i, timeMax, monitor);

        //After dinner, use this to close all thread
        for (int i = 0; i < 5; i++) {
            try {
                p[i].t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}