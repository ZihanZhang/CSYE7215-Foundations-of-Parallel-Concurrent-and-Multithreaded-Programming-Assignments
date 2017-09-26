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
            }
        }


    }

    public void print() {
        if (collection.size() == 0) {
            System.out.println("Even Inspector Failed");
            System.out.print("Even Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();

        }
        else {
            System.out.println("Even Inspector Succeeded");
            System.out.print("Even Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();
        }
    }
}
