import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

class FirstJ extends UntypedActor {

    public void onReceive(Object msg) throws Exception{
	if ((msg instanceof String) && msg.equals("hello")) {
	    System.out.println("Hello world!");
	} else if (msg instanceof String) {
	    System.out.println("Got " + (String) msg);
	} else { 
	    System.out.println("Unknown message ");
	}
    }
}

public class FirstAppJ {
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("FirstExample");
    final ActorRef first = system.actorOf(Props.create(FirstJ.class), "first");
    System.out.println("The path associated with first is " + first.path());
    first.tell("hello", null);
    first.tell("Goodbye", null);
    first.tell(4, null);
    system.shutdown();
  }
}
