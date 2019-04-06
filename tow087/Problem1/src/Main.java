import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Start.");
		final ActorSystem system = ActorSystem.create("Simulate_File_System");
		//servers
		final ActorRef s1 = system.actorOf(Server.props("f1"), "server1");
		final ActorRef s2 = system.actorOf(Server.props("f2"), "server2");
		final ActorRef s3 = system.actorOf(Server.props("f3"), "server3");
		ArrayList<ActorRef> sysserver = new ArrayList<ActorRef>();
		sysserver.add(s1);
		sysserver.add(s2);
		sysserver.add(s3);
		//client map
		Map<String, ActorRef> clients = new HashMap<String, ActorRef>(); 
		//get input from user
		while(true) {
	        String[] ncommands = null;
	        Scanner input= new Scanner(System.in);
	        do {
	        	System.out.print("user_name + cmd + file(use space to seprate): ");
		        String name_command = input.nextLine();
		        ncommands = name_command.split(" ");
	        }while(ncommands == null || ncommands.length != 3);
	        
	        String content = "";
	        //if command is write, ask what to write
	        if(ncommands[1].equals("write")) {
	        	System.out.println("what to write: ");
	        	content = input.nextLine();
	        	System.out.println(content);
	        }
	        //create a message use user info
	        Message.CommandMsg msg = new Message.CommandMsg(ncommands[2],ncommands[1],content);
	        
	        //if the client not in the map
	        if(clients.get(ncommands[0]) == null) {
	        	//create a new client, and put it into the map
	        	ActorRef client = system.actorOf(Client.props(sysserver), ncommands[0]);
	        	clients.put(ncommands[0], client);
	        	//sent msg
		        client.tell(msg, ActorRef.noSender());
	        }else {
	        	//the client is in the map, send msg to it
	        	clients.get(ncommands[0]).tell(msg, ActorRef.noSender());
	        }
	        
	        
	        
	        
		}
	}

}
