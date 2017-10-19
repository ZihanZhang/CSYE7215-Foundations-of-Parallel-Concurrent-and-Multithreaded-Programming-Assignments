package Zihan;


import java.util.List;

/**
 * Cooks are simulation actors that have at least one field, a name.
 * When running, a cook attempts to retrieve outstanding orders placed
 * by Eaters and process them.
 */

//*************************************************************************
//Synchronization is done through instance confine approach by Store.java
//*************************************************************************
public class Cook implements Runnable {
    private final String name;
    Store store;
    static int index = 0;
//	Simulation simulation;

    /**
     * You can feel free modify this constructor.  It must
     * take at least the name, but may take other parameters
     * if you would find adding them useful.
     *
     * @param: the name of the cook
     */
    public Cook(String name, Store store) {
        this.store = store;
        this.name = name;
//		this.simulation = simulation;
    }

    public String toString() {
        return name;
    }

    /**
     * This method executes as follows.  The cook tries to retrieve
     * orders placed by Customers.  For each order, a List<Food>, the
     * cook submits each Food item in the List to an appropriate
     * Machine, by calling makeFood().  Once all machines have
     * produced the desired Food, the order is complete, and the Customer
     * is notified.  The cook can then go to process the next order.
     * If during its execution the cook is interrupted (i.e., some
     * other thread calls the interrupt() method on it, which could
     * raise InterruptedException if the cook is blocking), then it
     * terminates.
     */
    public void run() {

        Simulation.logEvent(SimulationEvent.cookStarting(this));
        try {
            while(true) {
                //YOUR CODE GOES HERE...
                if (!store.customerReady()) {
                    continue;
                }
                Customer curCustomer = null;
                if (index < store.customers.size()) {
                    curCustomer = store.customers.get(index);
                    index++;
                    index++;
                }

                if (curCustomer == null) {
                    break;
                }

                List<Food> curFood= curCustomer.getOrder();
                Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, curFood, curCustomer.getOrderNum()));
                for (Food food: curFood) {
                    switch (food.name) {
                        case "burger":
                            Simulation.logEvent(SimulationEvent.cookStartedFood(this, food, curCustomer.getOrderNum()));
                            store.Grill.makeFood().join();
//                            store.Grill.curNum--;
                        case "fries":
                            Simulation.logEvent(SimulationEvent.cookStartedFood(this, food, curCustomer.getOrderNum()));
                            store.Fryer.makeFood().join();
//                            store.Fryer.curNum--;
                        case "coffee":
                            Simulation.logEvent(SimulationEvent.cookStartedFood(this, food, curCustomer.getOrderNum()));
                            store.CoffeeMaker2000.makeFood().join();
//                            store.CoffeeMaker2000.curNum--;
                    }
                    Simulation.logEvent(SimulationEvent.cookFinishedFood(this, food, curCustomer.getOrderNum()));
                }
                Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, curCustomer.getOrderNum()));
//                store.removeCustomer(curCustomer);
                synchronized (curCustomer.waitinglock) {
                    curCustomer.waitinglock.notify();
                }
            }
        }
        catch(InterruptedException e) {
            // This code assumes the provided code in the Simulation class
            // that interrupts each cook thread when all customers are done.
            // You might need to change this if you change how things are
            // done in the Simulation class.
            Simulation.logEvent(SimulationEvent.cookEnding(this));
        }
    }
}