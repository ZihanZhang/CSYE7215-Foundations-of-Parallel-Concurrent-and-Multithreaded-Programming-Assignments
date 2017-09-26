package ZihanB;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by ZihanZhang on 17/9/12.
 */
public class InspectorsManager {
    EvenInspector ei;
    OddInspector oi;
    OrderInspector ori;
    JackInspector ji;

    public static void main(String[] args) {
        int size = 10000;
        InspectorsManager im = new InspectorsManager();
        LinkedList<Integer> list = new LinkedList<Integer>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int next = rand.nextInt();
            list.add(next);
        }
        LinkedList<Integer> clist = (LinkedList)list.clone();
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Test " + i + ":");
                im.inspect(clist);
                clist = (LinkedList)list.clone();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void inspect(LinkedList list) throws InterruptedException{
        ei = new EvenInspector(list);
        oi = new OddInspector(list);
        ori = new OrderInspector(list);
        ji = new JackInspector(list);

        ei.start();
        oi.start();
        ori.start();
        ji.start();

        ei.join();
        oi.join();
        ori.join();
        ji.join();

        System.out.println();
        System.out.println();

        ei.print();
        oi.print();
        ori.print();
        ji.print();

        System.out.println("*****************************************************************");
    }

}
