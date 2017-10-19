package Zihan;

import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Simulation is the main class used to run the simulation.  You may
 * add any fields (static or instance) or any methods you wish.
 */
public class Simulation {
//    Object tablelock = new Object();
//    Object orderlock = new Object();


	// List to track simulation events during simulation
	public static List<SimulationEvent> events;  
//	static private LinkedList<Food> orders;
//	private ArrayList<Machine> machines;
//	static private Thread[] cooks;
//	static private LinkedList<Customer> customers = new LinkedList<>();

//    static Machine Grill;
//    static Machine Fryer;
//    static Machine CoffeeMaker2000;

//	private int numTables;

//	public int getAvailableTablesNumTables() {
//		return numTables;
//	}
//
//	public void setAvailableTablesNumTables(int num) {
//		numTables = num;
//	}

//	public boolean tableFull() {
//		if ( numTables <= 0) {
//			return true;
//		}
//		return false;
//	}

//	static public LinkedList<Food> getOrders() {
//		return orders;
//	}

	//could append or copy
//	public void setOrders(LinkedList<Food> orders) {
//		this.orders = orders;
//	}

//	static public void submitOrders(Customer customer) {
//		customers.add(customer);
//	}

//	static public Customer getOrder() {
//		return customers.poll();
//	}


	/**
	 * Used by other classes in the simulation to log events
	 * @param event
	 */
	public static void logEvent(SimulationEvent event) {
		events.add(event);
		System.out.println(event);
	}

	/**
	 * 	Function responsible for performing the simulation. Returns a List of 
	 *  SimulationEvent objects, constructed any way you see fit. This List will
	 *  be validated by a call to Validate.validateSimulation. This method is
	 *  called from Simulation.main(). We should be able to test your code by 
	 *  only calling runSimulation.
	 *  
	 *  Parameters:
	 *	@param numCustomers the number of customers wanting to enter the coffee shop
	 *	@param numCooks the number of cooks in the simulation
	 *	@param numTables the number of tables in the coffe shop (i.e. coffee shop capacity)
	 *	@param machineCapacity the capacity of all machines in the coffee shop
	 *  @param randomOrders a flag say whether or not to give each customer a random order
	 *
	 */
	public static List<SimulationEvent> runSimulation(
			int numCustomers, int numCooks,
			int numTables, 
			int machineCapacity,
			boolean randomOrders
			) {

		//This method's signature MUST NOT CHANGE.

//        Simulation.numTables = numTables;
        Store store = new Store(numCustomers, numCooks,
        numTables,
        machineCapacity,
        randomOrders);

		//We are providing this events list object for you.  
		//  It is the ONLY PLACE where a concurrent collection object is 
		//  allowed to be used.
		events = Collections.synchronizedList(new ArrayList<SimulationEvent>());




		// Start the simulation
		logEvent(SimulationEvent.startSimulation(numCustomers,
				numCooks,
				numTables,
				machineCapacity));



		// Set things up you might need


		// Start up machines
        store.Grill = new Machine("Grill", FoodType.burger, machineCapacity);
        store.Fryer = new Machine("Fryer", FoodType.fries, machineCapacity);
        store.CoffeeMaker2000 = new Machine("CoffeeMaker2000", FoodType.coffee, machineCapacity);


		// Let cooks in
        for (int i = 0; i < numCooks; i++) {
            store.cooks = new Thread[numCooks];
            Cook cook = new Cook("Cook:" + i, store);
            Thread cookThread = new Thread(cook);
            cookThread.start();
            store.cooks[i] = cookThread;
        }

		// Build the customers.
		Thread[] customers = new Thread[numCustomers];
		LinkedList<Food> order;
		if (!randomOrders) {
			order = new LinkedList<Food>();
			order.add(FoodType.burger);
			order.add(FoodType.fries);
			order.add(FoodType.fries);
			order.add(FoodType.coffee);
			for(int i = 0; i < customers.length; i++) {
				customers[i] = new Thread(
						new Customer("Customer " + (i+1), order, store)
						);
			}
		}
		else {
			for(int i = 0; i < customers.length; i++) {
				Random rnd = new Random(27);
				int burgerCount = rnd.nextInt(3);
				int friesCount = rnd.nextInt(3);
				int coffeeCount = rnd.nextInt(3);
				order = new LinkedList<Food>();
				for (int b = 0; b < burgerCount; b++) {
					order.add(FoodType.burger);
				}
				for (int f = 0; f < friesCount; f++) {
					order.add(FoodType.fries);
				}
				for (int c = 0; c < coffeeCount; c++) {
					order.add(FoodType.coffee);
				}
				customers[i] = new Thread(
						new Customer("Customer " + (i+1), order, store)
						);
			}
		}


		// Now "let the customers know the shop is open" by
		//    starting them running in their own thread.
		for(int i = 0; i < customers.length; i++) {
			customers[i].start();
			//NOTE: Starting the customer does NOT mean they get to go
			//      right into the shop.  There has to be a table for
			//      them.  The Customer class' run method has many jobs
			//      to do - one of these is waiting for an available
			//      table...
		}


		try {
			// Wait for customers to finish
			//   -- you need to add some code here...
            for (int i = 0; i < customers.length; i++) {
                customers[i].join();
            }
			
			
			
			
			

			// Then send cooks home...
			// The easiest way to do this might be the following, where
			// we interrupt their threads.  There are other approaches
			// though, so you can change this if you want to.
			for(int i = 0; i < store.cooks.length; i++)
				store.cooks[i].interrupt();
			for(int i = 0; i < store.cooks.length; i++)
				store.cooks[i].join();

		}
		catch(InterruptedException e) {
			System.out.println("Simulation thread interrupted.");
		}

		// Shut down machines
        for (Thread t: Store.machineThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logEvent(SimulationEvent.machineEnding(store.Grill));
        logEvent(SimulationEvent.machineEnding(store.Fryer));
        logEvent(SimulationEvent.machineEnding(store.CoffeeMaker2000));



        // Done with simulation
		logEvent(SimulationEvent.endSimulation());

		return events;
	}

	/**
	 * Entry point for the simulation.
	 *
	 * @param args the command-line arguments for the simulation.  There
	 * should be exactly four arguments: the first is the number of customers,
	 * the second is the number of cooks, the third is the number of tables
	 * in the coffee shop, and the fourth is the number of items each cooking
	 * machine can make at the same time.  
	 */
	public static void main(String args[]) throws InterruptedException {
		// Parameters to the simulation
		/*
		if (args.length != 4) {
			System.err.println("usage: java Simulation <#customers> <#cooks> <#tables> <capacity> <randomorders");
			System.exit(1);
		}
		int numCustomers = new Integer(args[0]).intValue();
		int numCooks = new Integer(args[1]).intValue();
		int numTables = new Integer(args[2]).intValue();
		int machineCapacity = new Integer(args[3]).intValue();
		boolean randomOrders = new Boolean(args[4]);
		 */
		int numCustomers = 10;
		int numCooks =1;
		int numTables = 5;
		int machineCapacity = 4;
		boolean randomOrders = false;


		// Run the simulation and then 
		//   feed the result into the method to validate simulation.
		System.out.println("Did it work? " + 
				Validate.validateSimulation(
						runSimulation(
								numCustomers, numCooks, 
								numTables, machineCapacity,
								randomOrders
								)
						)
				);
	}

}



