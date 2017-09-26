package ZihanB;

import java.util.LinkedList;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class JackInspector extends Thread{
    LinkedList<Integer> list;
    LinkedList<Integer> collection = new LinkedList<>();

    public JackInspector(LinkedList list) {
        this.list = list;
    }

    public void run() {
        int number;
        int sum = 0;

        while (true) {
            synchronized(list) {
                if (list.isEmpty() || sum == 21)
                    return; // list is empty
                number = list.remove();
                collection.add(number);
                sum = sum + number;
            }
        }


    }

    public void print() {
        System.out.print("Jack Inspector Results: ");
        for (int i : collection) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}
