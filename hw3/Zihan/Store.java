package Zihan;

import java.util.LinkedList;

public class Store {
    public static Object tablelock = new Object();

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
        numTables--;
    }

    public void leaveSeat() {
        numTables++;
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

    public void submitOrders(Customer customer) {
        customers.add(customer);
    }

    public Customer getOrder() {
        return customers.poll();
    }
}
