package akkaHW2017F;

import akka.actor.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

public class User extends UntypedActor{

//	static int count = 0;

	public static void main(String[] args) throws Exception {
		Props userProps = Props.create(User.class);

		ActorSystem system = ActorSystem.create("EstimationSystem");

		ActorRef userRef = system.actorOf(userProps);

		/*
		 * Create the Estimator Actor and send it the StartProcessingFolder
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
		 */

		Props estimatorProps = Props.create(Estimator.class);
		ActorRef estimatorRef = system.actorOf(estimatorProps);

//		Props firstCounterProps = Props.create(FirstCounter.class, estimatorRef);
//		Props secondCounterProps = Props.create(SecondCounter.class, estimatorRef);
//		ActorRef firstCounterRef = system.actorOf(firstCounterProps);
//		ActorRef secondCounterRef = system.actorOf(secondCounterProps);

		for (int i = 1; i <= 10; i++) {
			String fileName = "./akkaHW2017F/Akka" + i + ".txt";
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			StringBuffer firstHalf = new StringBuffer();
			StringBuffer secondHalf = new StringBuffer();

			int lineNum = 1;
			String line;
			while((line = bufferedReader.readLine()) != null) {
				if (lineNum % 2 != 0) {
					firstHalf.append(line);
				}
				else {
					secondHalf.append(line);
				}
				lineNum++;
			}
//
//			Messages.fileMessage curFileMessage1 = new Messages.fileMessage(firstHalf.toString());
//			Messages.fileMessage curFileMessage2 = new Messages.fileMessage(firstHalf.toString());
			Messages.fileMessage fileMessage = new Messages.fileMessage(firstHalf.toString(), secondHalf.toString());
			estimatorRef.tell(fileMessage,userRef);
//			firstCounterRef.tell(curFileMessage1, null);
//			secondCounterRef.tell(curFileMessage2, null);

			// Always close files.
			bufferedReader.close();

//			system.terminate();
		}

		TimeUnit.SECONDS.sleep(2);
		system.terminate();
//		while (true) {
////			System.out.println("here" + count);
//			if (count >= 10) {
//				system.terminate();
//				break;
//			}
//		}
	}

//	public void showResult(double estimate, int real) {
//		System.out.println("Estimate: " + estimate + " Real: " + real);
//	}

	@Override
	public void onReceive(Object o) throws Throwable {
		if (o instanceof Messages.resultMessage) {
			Messages.resultMessage payload = (Messages.resultMessage)o;
			System.out.println("Estimate: " + (int)payload.getEstimate() + " Real: " + payload.getReal());
		}

//		count++;
//		System.out.println(count);
	}
}
