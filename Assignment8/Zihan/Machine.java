package Zihan;

/**
 * A Machine is used to make a particular Food.  Each Machine makes
 * just one kind of Food.  Each machine has a capacity: it can make
 * that many food items in parallel; if the machine is asked to
 * produce a food item beyond its capacity, the requester blocks.
 * Each food item takes at least item.cookTimeMS milliseconds to
 * produce.
 */
public class Machine {
    Object machinelock = new Object();

	public final String machineName;
	public final Food machineFoodType;
	public final int capacityIn;

	//YOUR CODE GOES HERE...
    private int curNum;



	/**
	 * The constructor takes at least the name of the machine,
	 * the Food item it makes, and its capacity.  You may extend
	 * it with other arguments, if you wish.  Notice that the
	 * constructor currently does nothing with the capacity; you
	 * must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
		this.capacityIn = capacityIn;
		//YOUR CODE GOES HERE...
        Simulation.logEvent(SimulationEvent.machineStarting(this, foodIn, capacityIn));
    }
	

	

	/**
	 * This method is called by a Cook in order to make the Machine's
	 * food item.  You can extend this method however you like, e.g.,
	 * you can have it take extra parameters or return something other
	 * than Object.  It should block if the machine is currently at full
	 * capacity.  If not, the method should return, so the Cook making
	 * the call can proceed.  You will need to implement some means to
	 * notify the calling Cook when the food item is finished.
	 */
	public Thread makeFood() throws InterruptedException {
		//YOUR CODE GOES HERE...
        while (curNum >= capacityIn) {
            synchronized (machinelock) {
                machinelock.wait();
            }
        }
        Thread makeFoodThread = new Thread(new CookAnItem());
        Store.machineThreads.add(makeFoodThread);
        makeFoodThread.start();
        Simulation.logEvent(SimulationEvent.machineCookingFood(this, machineFoodType));
//        makeFoodThread.join();
        return makeFoodThread;
	}

	//THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		public void run() {
			try {
				//YOUR CODE GOES HERE...
//                switch (machineFoodType.name) {
//                    case "burger": Thread.sleep(600);
//                    case "fries": Thread.sleep(450);
//                    case "coffee": Thread.sleep(150);
//                }
                synchronized (machinelock) {
                    takeMachine();
                    Thread.sleep(machineFoodType.cookTimeMS);
                    Simulation.logEvent(SimulationEvent.machineDoneFood(Machine.this, machineFoodType));
                    machinelock.notifyAll();
                    leaveMachine();
                }
			} catch(InterruptedException e) { }
		}
	}

	public void takeMachine() {
	    synchronized (machinelock) {
            curNum--;
        }
    }

    public void leaveMachine() {
	    synchronized (machinelock) {
            curNum++;
        }
    }
 

	public String toString() {
		return machineName;
	}
}