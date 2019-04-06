import java.util.ArrayList;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Client extends AbstractActor{
	private ArrayList<ActorRef> sysserver;
	Client(ArrayList<ActorRef> s){
		this.sysserver = s;
	}
	
	
	static public Props props(ArrayList<ActorRef> s) {
        return Props.create(Client.class, () -> new Client(s));
        
    }

	
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return this.receiveBuilder()
				.match(Message.CommandMsg.class, msg->{this.urmsgReceive(msg);})
				.match(Message.ServerMsg.class, msg->{this.semsgReceive(msg);})
				.build();
	}
	
	
	private void semsgReceive(Message.ServerMsg msg) {
		//get msg from server
		System.out.println(this.getSender().path().name() + ": " + msg.reply);
	}


	private void urmsgReceive(Message.CommandMsg msg) {
		//get msg from user
		for (ActorRef s : this.sysserver){
			Message.CommandMsg message = msg;
			s.tell(message, self());
		}
	}


	
	
	
	
	
	
	
}
