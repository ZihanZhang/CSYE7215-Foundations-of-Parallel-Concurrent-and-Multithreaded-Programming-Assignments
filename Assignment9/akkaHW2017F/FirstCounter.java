package akkaHW2017F;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import scala.collection.mutable.HashSet;


public class FirstCounter extends UntypedActor {

//	ActorRef estimatorRef;

	HashSet<Character> vowels = new HashSet<>();


	public FirstCounter() {
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

//		this.estimatorRef = estimatorRef;
	}

	int num = 0;

	@Override
	public void onReceive(Object msg) throws Throwable {
		ActorRef sender = getSender();
		Messages.partMessage payload = (Messages.partMessage)msg;

		for (int i = 0; i < payload.getMessage().length(); i++) {
			if (vowels.contains(payload.getMessage().charAt(i))) {
				num++;
			}
		}

		Messages.feedBack feedBack = new Messages.feedBack(num);

		sender.tell(feedBack, sender);
	}

}
