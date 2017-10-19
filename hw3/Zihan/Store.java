package Zihan;

import java.util.ArrayList;
import java.util.LinkedList;

public class Store {
    public static Object tablelock = new Object();
    private Object customerlock = new Object();
//    public static Object foodlock = new Object();

    public static ArrayList<Thread> machineThreads = new ArrayList<>();

    Thread[] cooks;
    LinkedList<Customer> customers;
    Machine Grill;
    Machine Fryer;
    Machine CoffeeMaker2000;
    int numTables;

    public Store(int numCustomers, int numCooks,
                 int numTables,
                 int machineCapacity,
                 boolean randomOrders) {
        this.numTables = numTables;
        this.customers = new LinkedList<>();
    }

    public void takeSeat() {
        synchronized (Store.tablelock) {
            numTables--;
        }
    }

    public void leaveSeat() {
        synchronized (Store.tablelock) {
            numTables++;
        }
    }


//	public int getAvailableTablesNumTables() {
//		return numTables;
//	}
//
//	public void setAvailableTablesNumTables(int num) {
//		numTables = num;
//	}

	public boolean tableFull() {
		if ( numTables <= 0) {
			return true;
		}
		return false;
	}

	public boolean customerReady() {
        synchronized (customerlock) {
            if (customers.size() <= 0) {
                return false;
            }
            return true;
        }
    }

    public void submitOrders(Customer customer) {
        synchronized (customerlock) {
            customers.add(customer);
        }
    }

    public Customer getOrder() {
        synchronized (customerlock) {
            return customers.poll();
        }
    }

    public void removeCustomer(Customer customer) {
        synchronized (customerlock) {
            customers.remove(customer);
        }
    }
}
