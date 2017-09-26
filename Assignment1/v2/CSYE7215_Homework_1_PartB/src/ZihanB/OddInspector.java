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
            }
        }


    }

    public void print() {
        if (collection.size() == 0) {
            System.out.println("Odd Inspector Failed");
            System.out.print("Odd Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();
        }
        else {
            System.out.println("Odd Inspector Succeeded");
            System.out.print("Odd Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();
        }

    }
}
