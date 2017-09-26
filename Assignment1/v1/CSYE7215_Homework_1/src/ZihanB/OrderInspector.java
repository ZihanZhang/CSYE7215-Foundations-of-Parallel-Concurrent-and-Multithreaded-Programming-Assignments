package ZihanB;

import java.util.LinkedList;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class OrderInspector extends Thread {
    LinkedList<Integer> list;
    LinkedList<Integer> collection = new LinkedList<>();

    public OrderInspector(LinkedList list) {
        this.list = list;
    }

    public void run() {
        int number;

        while (true) {
            synchronized(list) {
                if (list.isEmpty())
                    return; // list is empty
                number = list.remove();
                if (collection.size() == 0) {
                    collection.add(number);
                }
                else if (number > collection.getLast()) {
                    collection.add(number);
                }
                else {
                    System.out.print("Order Inspector Failed On: " + number + "  ");
                }
            }
        }


    }

    public void print() {
        System.out.print("Order Inspector Results: ");
        for (int i : collection) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
