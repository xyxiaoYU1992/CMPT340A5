public class Message {
	// we have two kinds of messages
	// ServerMsg is message communicate between server and client
	// CommandMsg is message communicate between client and command
	
	static public class CommandMsg{
		//msg from the command/user
		public String file;
		public String cmd;
		public String whattowrite;
		
		CommandMsg(String fn, String cm, String con){
			this.file = fn;
			this.cmd = cm;
			this.whattowrite = con;
		}
	}
	
	static public class ServerMsg{
		//msg from the server
		public String reply; 
		
		ServerMsg(String st){
			this.reply = st;
		}
	}
}
