package Zihan;

import java.util.List;

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

	Store store;

	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order, Store store) {
//		this.simulation = simulation;
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		this.store = store;
	}

	public List<Food> getOrder() {
		return order;
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
        System.out.println("Customer start");
        System.out.println(store.numTables);
		while (store.tableFull()) {
		    System.out.println("Table's full");
			try {
			    synchronized (Store.tablelock) {
                    Store.tablelock.wait();
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		synchronized (Store.tablelock) {
            store.takeSeat();
            store.customers.add(this);
        }
		System.out.println("Take seat");
        System.out.println("ready to submit");
		store.submitOrders(this);

		while(!over) {

		}

		synchronized (Store.tablelock) {
            store.leaveSeat();
            store.customers.remove(this);
            Store.tablelock.notifyAll();
        }
		System.out.println(store.numTables);
    }

	public void exit() {
	    this.over = true;
    }
}