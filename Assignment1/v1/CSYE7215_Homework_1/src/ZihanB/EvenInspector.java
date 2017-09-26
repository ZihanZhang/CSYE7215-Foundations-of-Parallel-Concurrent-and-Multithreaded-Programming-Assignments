package ZihanB;

import java.util.LinkedList;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class EvenInspector extends Thread {
    LinkedList<Integer> list;
    LinkedList<Integer> collection = new LinkedList<>();

    public EvenInspector(LinkedList list) {
        this.list = list;
    }

    public void run() {
        int number;

        while (true) {
            synchronized(list) {
                if (list.isEmpty())
                    return; // list is empty
                number = list.remove();
                if (number % 2 == 0) {
                    collection.add(number);
                }
                else {
                    System.out.print("Even Inspector Failed On: " + number + "  ");
                }
            }
        }


    }

    public void print() {
        System.out.print("Even Inspector Results: ");
        for (int i : collection) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
