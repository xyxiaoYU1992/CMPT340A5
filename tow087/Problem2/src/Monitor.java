import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum State {HUNGRY, THINKING, EATING};

public class Monitor {

	//define and initialize the monitor's lock, state and condition
    final Lock lock;
    private State [] state;
    final Condition[] condition;
    

    public Monitor(){
        lock = new ReentrantLock();
        state = new State[5];
        condition = new Condition[5];
        for(int i = 0; i < 5; i++){
            state[i] = State.THINKING;
            condition[i] = lock.newCondition();
        }
    }
    
    
    // build getForks method
    public void takeForks(int i){
        lock.lock();
        int resultGet = 0;
        try{
            // Indicate that I want to take forks.
        	// State is hungry
            state[i] = State.HUNGRY;
            // Pick up forks if both neighbors are not eating.
            // check neighbour people state
            if( ( state[(i-1+5)%5] != State.EATING ) &&
                    (state[(i+1)%5] != State.EATING) ){
            	resultGet = i + 1;
                System.out.println("The philosopher " + resultGet +" pick up forks\n");
                state[i] = State.EATING;
            }
            else {	//if neighbour people eating now, should wait
                try {
                    condition[i].await();
                    // Eat after waiting.
                    resultGet = i + 1;
                    System.out.println("The philosopher " + resultGet +" pick up forks\n");
                    state[i] = State.EATING;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finally{
            lock.unlock();
        }
    }
    // build relForks method
    public void putDownForks(int i){
        lock.lock();
        int resultRel = 0;
        try{
        	resultRel = i + 1;
            System.out.println("The philosopher " + resultRel +" put down forks\n");
            state[i] = State.THINKING;
            int left = (i - 1 + 5)%5;
            int left2 = (i - 2 +5)%5;
            if( (state[left] == State.HUNGRY) &&
                    (state[left2] != State.EATING) ){
                condition[left].signal();
            }
            if( (state[(i+1)%5] == State.HUNGRY) &&
                    (state[(i+2)%5] != State.EATING) ){
                condition[(i+1)%5].signal();
            }
        }
        finally {
            lock.unlock();
        }
    }
}
