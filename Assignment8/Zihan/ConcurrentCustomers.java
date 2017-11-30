package Zihan;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentCustomers {
    Customer dummy = new Customer(null,null,null);
    AtomicReference<Customer> first = new AtomicReference<>(dummy);
    AtomicReference<Customer> last = new AtomicReference<>(dummy);

    AtomicInteger num = new AtomicInteger();

    public ConcurrentCustomers() {
        num.set(0);
    }

    public int getNum() {
        int curNum;
        do {
            curNum = num.get();
        } while (!num.compareAndSet(curNum, curNum));
        return curNum;
    }

    public Customer getCustomer() {
        Customer curCustomer;
        Customer newFirst;
        int curNum;
        int newNum;
        do {
            curCustomer = first.get();
            if (curCustomer == null) {
                return null;
            }
            newFirst = curCustomer.next.get();
            curNum = num.get();
            newNum = curNum - 1;
        } while (!first.compareAndSet(curCustomer, newFirst) && !num.compareAndSet(curNum, newNum));
        return curCustomer;
    }

    public void submitCustomer(Customer customer) {
        if (first.get() == dummy) {
            first.set(customer);
            last.set(customer);
            return;
        }

        while (true) {
            Customer curLast = last.get();
            Customer lastNext = curLast.next.get();
            int curNum = num.get();
            int newNum;
            if (curLast == last.get()) {
                if (lastNext != null) {
                    last.compareAndSet(curLast, lastNext);
                } else {
                    if (curLast.next.compareAndSet(null, customer)) {
                        newNum = curNum + 1;
                        num.compareAndSet(curNum, newNum);
                        last.compareAndSet(curLast, customer);
                        return;
                    }
                }
            }
        }
    }
}
