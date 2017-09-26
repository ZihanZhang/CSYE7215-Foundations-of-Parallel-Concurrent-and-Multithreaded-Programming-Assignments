package ZihanB;

import java.util.LinkedList;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class OddInspector extends Thread {
    LinkedList<Integer> list;
    LinkedList<Integer> collection = new LinkedList<>();

    public OddInspector(LinkedList list) {
        this.list = list;
    }

    public void run() {
        int number;

        while (true) {
            synchronized(list) {
                if (list.isEmpty())
                    return; // list is empty
                number = list.remove();
                if (number % 2 == 1) {
                    collection.add(number);
                }
                else {
                    System.out.print("Odd Inspector Failed On: " + number + "  ");
                }
            }
        }


    }

    public void print() {
        System.out.print("Odd Inspector Results: ");
        for (int i : collection) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
