
//This is server portion of code- MyServer.java
// Java implementation of Server side
import java.io.*;
import java.text.*; import java.util.*;
import java.net.*;
// Server class
public class Server{
	public static void main(String[] args) throws IOException{
// server is listening on port 5050
		System.out.println("asssssss" );
		ServerSocket ss = new ServerSocket(5050);
		System.out.println("bbbbbssss" );
// running infinite loop for getting client request
		while (true){
			Socket s = null;
			try{
// socket object to receive incoming client requests
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				System.out.println("Assigning new thread for this client");
// create a new thread object
				Thread t = new ClientHandler(s, dis, dos);
// Invoking the start() method
				t.start();
			}catch (Exception e){
				s.close();
				ss.close();
				e.printStackTrace();
			}
		}
		
	}
}

// ClientHandler class
class ClientHandler extends Thread{
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run(){
		String received; 
		String toreturn;
		try {
// receive the answer from client
			received = dis.readUTF();
			System.out.println("received message = " + received);
			String response = getResponse(received);
			// dos.writeUTF("Message from server to client");
			dos.writeUTF(response);

			System.out.println("Closing this connection.");
			this.s.close();
			System.out.println("Connection closed");

		} catch (IOException e) {
			e.printStackTrace();}

		try {
		// closing resources
			this.dis.close(); this.dos.close();
		}catch(IOException e){
			e.printStackTrace();}

	}

	String getResponse(String query){
		String[] arrOfStr;
		if(query.contains("?")){
			arrOfStr = query.split("\\?", 2); 
		}else{
			return "ERROR:contains no ? in query";
		}
		String function = arrOfStr[0];
		// try{
			query = arrOfStr[1];	
		// } 
		// catch 
		// {
		// 	System.out.println("most likely string format wrong as problem in accesing elements at1 index in arrOfStr in getResponse in Server ");
		// }
		System.out.println("function = " + function);
		System.out.println("query = " + query);
		String[] secondArrOfStr = arrOfStr[1].split("&");
		HashMap<String, String> hashMap = new HashMap<>();
		for(int i=0; i< secondArrOfStr.length; i++){
			// System.out.println("secondArrOfStr[i] = " + secondArrOfStr[i]);
			String[] temp = secondArrOfStr[i].split("=",2);
			hashMap.put(temp[0],temp[1]);
			 
		}
		String response = "response without any change";
		System.out.println("naaaame = " + hashMap.get("u"));
			connection C=new connection("sendox","root","KshitizSharma28*");
			try {
				switch(function){
				//"SignUp?" + "firstName="+ firstName + "&lastName=" + lastName + "&username=" + username + "&emailAddress=" + emailAddress + "&password=" + SHA256(password);
				case "check_user":
					System.out.println("call check_user function");
					//TODO: edit response
					System.out.println(C.check_user(hashMap.get("name"),"login_users"));
					break;
				case "fetch_public_key":
					System.out.println("call fetch_public_key function");
					response = C.fetch_public_key(hashMap.get("username"), "public_keys");
					break;
				case "SignUp":
					System.out.println("call SignUp function");//change adduser to signup
					response = C.adduser(hashMap.get("username"), hashMap.get("password"), "login_users", hashMap.get("firstName"), hashMap.get("lastName"), hashMap.get("emailAddress"));
					break;
				case "edit_status":
					System.out.println("call edit_status function");
					response = C.Edit_contact(hashMap.get("username"), "about_users", hashMap.get("new_status"));
					break;
				case "view_contact":
					System.out.println("call view_contact function");
					response = C.view_contact(hashMap.get("username"), "about_users");
					
					break;
				case "send_message":
					System.out.println("call send_message function");
					response = C.send_message(hashMap.get("reciever"), hashMap.get("username"), "messages",  hashMap.get("content"));
					break;
				case "load_inbox":
					System.out.println("call load_inbox function");
					response = C.load_messages(hashMap.get("username"),Integer.valueOf(hashMap.get("index")) ,"messages");
					break;
				case "delete_account":
//					System.out.println("call delete_account function");
					response = C.Delete_account(hashMap.get("username"), hashMap.get("password"));
					break;
				case "addpublic_key":
					response = C.addpublic_key(hashMap.get("n"),hashMap.get("e") ,"public_keys", hashMap.get("username"));
					break;
				default:
					System.out.println("no matching function found");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		System.out.println("response = " + response);
		if(response == null)response="fail?";
		System.out.println("response = " + response);
		return response;
	
	}
}

