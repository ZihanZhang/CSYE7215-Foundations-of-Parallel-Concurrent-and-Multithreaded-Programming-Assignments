package Zihan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;


//********************************************************
//Take care of all resources and do the synchronization
//********************************************************
public class Store {
    public static Object tablelock = new Object();
    private Object customerlock = new Object();
//    public static Object foodlock = new Object();

    public static ArrayList<Thread> machineThreads = new ArrayList<>();

    Thread[] cooks;
    ConcurrentCustomers customers;
    Machine Grill;
    Machine Fryer;
    Machine CoffeeMaker2000;
    int numTables;

    public Store(int numCustomers, int numCooks,
                 int numTables,
                 int machineCapacity,
                 boolean randomOrders) {
        this.numTables = numTables;
        this.customers = new ConcurrentCustomers();
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
            if (customers.getNum() <= 0) {
                return false;
            }
            return true;
        }
    }

//    public void submitOrders(Customer customer) {
////        synchronized (customerlock) {
////            customers.add(customer);
////        }
//
//        Customer newCustomer = customer;
//        Customer oldCutomer;
//
//        do {
//            oldCutomer = customers.get();
//        } while (customers.compareAndSet(oldCutomer, newCustomer));
//
//    }

//    public Customer getOrder() {
////        synchronized (customerlock) {
////            return customers.poll();
////        }
//        Customer curCustomer;
//        do {
//            curCustomer = customers.get();
//            return curCustomer;
//        } while (customers.compareAndSet(curCustomer, curCustomer));
//    }

//    public void removeCustomer(Customer customer) {
//        synchronized (customerlock) {
//            customers.remove(customer);
//        }
//    }
}
