package akkaHW2017F;

import java.io.File;
import java.util.concurrent.Future;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.collection.mutable.HashMap;
import scala.collection.mutable.HashSet;
import scala.collection.mutable.HashTable;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

public class Estimator extends UntypedActor {

	public Estimator() {
		vowels.add('A');
		vowels.add('a');
		vowels.add('E');
		vowels.add('e');
		vowels.add('I');
		vowels.add('i');
		vowels.add('O');
		vowels.add('o');
		vowels.add('U');
		vowels.add('u');
		vowels.add('Y');
		vowels.add('y');
	}

	int num = 0;
	double estimation;
	double P1 = 1, P2 = 0.9, P3 = 1.1;

	HashSet<Character> vowels = new HashSet<>();

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof Messages.fileMessage) {
			Messages.fileMessage payload = (Messages.fileMessage)msg;
//		System.out.print(payload.getMessage());

			num = 0;

			for (int i = 0; i < payload.getFirstPart().length(); i++) {
				if (vowels.contains(payload.getFirstPart().charAt(i))) {
					num++;
				}
			}

			estimation = num * P1 * 2;

			ActorRef firstCounterRef = getContext().actorOf(Props.create(FirstCounter.class));
			ActorRef secondCounterRef = getContext().actorOf(Props.create(SecondCounter.class));

//			firstCounterRef.tell(payload.getFirstPart(), null);
//			secondCounterRef.tell(payload.getSecondPart(), null);

			Timeout timeout = new Timeout(Duration.create(10, "seconds"));

			scala.concurrent.Future<Object> firstResult = Patterns.ask(firstCounterRef, new Messages.partMessage(payload.getFirstPart()), timeout);
			scala.concurrent.Future<Object> secondResult = Patterns.ask(secondCounterRef, new Messages.partMessage(payload.getSecondPart()), timeout);

			try {

			} catch (Exception e){
				System.out.println("Out of time");
			}
			Messages.feedBack firstFeedBack = (Messages.feedBack)Await.result(firstResult, timeout.duration());
			Messages.feedBack secondFeedBack = (Messages.feedBack)Await.result(secondResult, timeout.duration());

			int firstNum = firstFeedBack.getFeedBack();
//			System.out.println(firstNum);
			int secondNum = secondFeedBack.getFeedBack();
//			System.out.println(secondNum);

			if (estimation < firstNum + secondNum) {
				P1 = P1 * P3;
			}
			else if (estimation > firstNum + secondNum) {
				P1 = P1 * P2;
			}
			else { }

//			System.out.println(P1);

			Messages.resultMessage resultMessage = new Messages.resultMessage(estimation, firstNum + secondNum);
			ActorRef sender = getSender();
			sender.tell(resultMessage, sender);
		}
	}

}
