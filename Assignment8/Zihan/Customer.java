package Zihan;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the coffee shop (only successful if the
 * coffee shop has a free table), place its order, and then leave the 
 * coffee shop when the order is complete.
 */
public class Customer implements Runnable {
	//JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;
//	Simulation simulation;
	private boolean orderReady = false;
	
	private static int runningCounter = 0;

	private volatile boolean over = false;

    Object waitinglock = new Object();

    Object foodlock = new Object();
//    static Object foodlock = new Object();

	Store store;

	AtomicReference<Customer> next = new AtomicReference<>();

	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order, Store store) {
//		this.simulation = simulation;
		this.name = name;
		this.order = order;
		this.orderNum = runningCounter++;
		this.store = store;
	}

	public List<Food> getOrder() {
	    synchronized (foodlock) {
            return order;
        }
	}

	public void setOrderReady() {
		this.orderReady = true;
	}

	public String toString() {
		return name;
	}

	/** 
	 * This method defines what an Customer does: The customer attempts to
	 * enter the coffee shop (only successful when the coffee shop has a
	 * free table), place its order, and then leave the coffee shop
	 * when the order is complete.
	 */
	public void run() {
		//YOUR CODE GOES HERE...
        Simulation.logEvent(SimulationEvent.customerStarting(this));
        synchronized (Store.tablelock) {
            while (store.tableFull()) {
                try {
                    Store.tablelock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
		synchronized (Store.tablelock) {
            store.takeSeat();
//            store.customers.add(this);
        }
//		store.submitOrders(this);
		store.customers.submitCustomer(this);
        Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, order, orderNum));

//		while(!over) {
//
//		}
        synchronized (waitinglock) {
            try {
                waitinglock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, order, orderNum));

		synchronized (Store.tablelock) {
            store.leaveSeat();
//            store.removeCustomer(this);
//            System.out.println("ready to notify");
            Store.tablelock.notifyAll();
            Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
        }
    }

    public int getOrderNum() {
	    return orderNum;
    }

	public void exit() {
	    synchronized (this) {
            this.over = true;
        }
    }
}