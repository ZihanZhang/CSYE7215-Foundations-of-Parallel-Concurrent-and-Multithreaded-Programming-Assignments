package ZihanB;

import java.util.LinkedList;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class JackInspector extends Thread{
    LinkedList<Integer> list;
    LinkedList<Integer> collection = new LinkedList<>();
    boolean success = false;

    public JackInspector(LinkedList list) {
        this.list = list;
    }

    public void run() {
        int number;
        int sum = 0;

        while (true) {
            synchronized(list) {
                if (sum == 21) {
                    success = true;
                    return; // list is empty
                }
                if (list.isEmpty()) {
                    return;
                }
                number = list.remove();
                collection.add(number);
                sum = sum + number;
            }
        }


    }

    public void print() {
        if (success) {
            System.out.println("Jack Inspector Succeeded");
            System.out.print("Jack Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();
        }
        else {
            System.out.println("Jack Inspector Failed");
            System.out.print("Jack Inspector Results: ");
            for (int i : collection) {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println();
        }
    }

}
