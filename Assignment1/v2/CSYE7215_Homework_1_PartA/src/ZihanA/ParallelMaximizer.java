package ZihanA;

//import java.util.LinkedList;
import java.util.*;

/**
 * This class runs <code>numThreads</code> instances of
 * <code>ParallelMaximizerWorker</code> in parallel to find the maximum
 * <code>Integer</code> in a <code>LinkedList</code>.
 */
public class ParallelMaximizer {
	
	int numThreads;
	ArrayList<ParallelMaximizerWorker> workers; // = new ArrayList<ParallelMaximizerWorker>(numThreads);

	public ParallelMaximizer(int numThreads) {
		workers = new ArrayList<ParallelMaximizerWorker>(numThreads);
		this.numThreads = numThreads;
	}


	
	public static void main(String[] args) {
		int numThreads = 10; // number of threads for the maximizer
		int numElements = 10000; // number of integers in the list
		
		ParallelMaximizer maximizer = new ParallelMaximizer(numThreads);
		LinkedList<Integer> list = new LinkedList<Integer>();

		// populate the list
		// TODO: change this implementation to test accordingly
		Random rand = new Random();
		for (int i = 0; i < numElements; i++) {
			int next = rand.nextInt();
			list.add(next);
		}

		LinkedList<Integer> clist = (LinkedList<Integer>) list.clone();

		// run the maximizer
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("Maximum Integer: " + maximizer.max(clist));
				clist = (LinkedList<Integer>) list.clone();
				System.out.println("********************************************************************R");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Finds the maximum by using <code>numThreads</code> instances of
	 * <code>ParallelMaximizerWorker</code> to find partial maximums and then
	 * combining the results.
	 * @param list <code>LinkedList</code> containing <code>Integers</code>
	 * @return Maximum element in the <code>LinkedList</code>
	 * @throws InterruptedException
	 */
	public int max(LinkedList<Integer> list) throws InterruptedException {
		int max = Integer.MIN_VALUE; // initialize max as lowest value


		System.out.println("Numbers in List: " + list.size());
//		for (int s : list) {
//			System.out.println(s);
//		}
		
//		System.out.println(workers.size());
		// run numThreads instances of ParallelMaximizerWorker
//		for (int i=0; i < workers.size(); i++) {
//			workers.set(i, new ParallelMaximizerWorker(list));
//			workers.get(i).run();
//		}
		for (int i=0; i < numThreads; i++) {
			workers.add(new ParallelMaximizerWorker(list));
			// why run() can only use 1 thread?
//			workers.get(i).run();
			workers.get(i).start();
		}
		// wait for threads to finish
		for (int i=0; i<numThreads; i++)
			workers.get(i).join();
		
		// take the highest of the partial maximums
		// TODO: IMPLEMENT CODE HERE
		int i = 0;
		for (ParallelMaximizerWorker pworker : workers) {
			System.out.println("Worker " + i +  ": " + pworker.getPartialMax());
			i++;
			if (pworker.getPartialMax() > max) {
				max = pworker.getPartialMax();
			}
		}

		System.out.println("Numbers in List: " + list.size());

		workers.clear();
		
		return max;
	}
	
}
