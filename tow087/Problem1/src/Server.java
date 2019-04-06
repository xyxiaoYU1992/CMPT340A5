import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Server extends AbstractActor{
	private String filename;
	private Boolean opened;
	
	Server(String fn){
		this.filename = fn;
		this.opened = false;
	}
	
	static public Props props(String fn) {
        return Props.create(Server.class, () -> new Server(fn));
    }
	
	private void writeFileCmd(String content) throws IOException {
		// TODO Auto-generated method stub
		//write content to the file
		String existContent = readFileCmd();
		String inputContent = content;
		//check the file have contents or not
		if(existContent != null) {
			inputContent = existContent + content;
		}
		BufferedWriter out = new BufferedWriter(new FileWriter("src/" + filename + ".txt"));
		out.write(inputContent);
		out.close();

	}

	private String readFileCmd() throws IOException {
		//read the file
		File file = new File("src/" + filename + ".txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st;
		st = br.readLine();
		return st;
	}

	@Override
	public Receive createReceive() {
		//receive msg
		return this.receiveBuilder()
				.match(Message.CommandMsg.class, msg->{this.urmsgReceive(msg);})
				.build();
	}

	private void urmsgReceive(Message.CommandMsg msg) throws IOException {
		//check the file matched
		if(msg.file.equals(this.filename)) {
			switch(msg.cmd) {
			case "open":
				if(opened) {
					this.getSender().tell(new Message.ServerMsg("the file already opened"), self());
				}else {
					this.opened = true;
					this.getSender().tell(new Message.ServerMsg(this.filename + " successfully opened"), self());
				}
				break;
			case "read":
				if(opened) {
					String content = readFileCmd();
					this.getSender().tell(new Message.ServerMsg(content), self());
				}else {
					this.getSender().tell(new Message.ServerMsg(this.filename + " needs to be opened before read"), self());
				}
				break;
			case "write":
				if(opened) {
					String content = msg.whattowrite;
					writeFileCmd(content);
					this.getSender().tell(new Message.ServerMsg(" successful wroten."), self());
				}else {
					this.getSender().tell(new Message.ServerMsg(this.filename + " needs to be opened before write"), self());
				}
				break;
			case "close":
				if(opened) {
					this.opened = false;
					this.getSender().tell(new Message.ServerMsg("file closed"), self());
				}else {
					this.getSender().tell(new Message.ServerMsg(this.filename + " needs to be opened before close"), self());
				}
				break;
			default:
				//else command is illegal, tell the client
				this.getSender().tell(new Message.ServerMsg("command needs to be legal"), self());
			}
		}else {
			//else, not has the file
			this.getSender().tell(new Message.ServerMsg("This file is NOT in this server."), self());
		}
	}


	
	

}
