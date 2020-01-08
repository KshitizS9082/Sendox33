package com.example.sendox33;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

 public class Client {
    public  String server_url ; // FORMAT : https://0.0.0.0/Sendox2, no slash at the last position !!!
    public int server_port ;
    public String query;

    public Client(String server_url, int server_port) {
        this.server_port = server_port;
        this.server_url = server_url;
        // this.query = queryInput;
    }

    public void printer(){
        System.out.println("inside printer");
    }

    public String sendQuery(String queryInput){
        this.query = queryInput;
        System.out.println("entered SendQuery");
        String received = "";
        try{
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("10.145.126.103");
            System.out.println("ip = " + ip);
            // establish the connection with server port 5050
            Log.d("my Tag", "sendQuery: afterip");
            Socket s = new Socket(ip, 5050);
            Log.d("my Tag" , "bafter ne wsocket");
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // the following loop performs the exchange of
            // information between client and client handler
            String tosend = query;
            dos.writeUTF(tosend);
            // printing date or time as requested by client
            received = dis.readUTF();
            System.out.println(received);
            s.close();
            // closing resources
            dis.close(); dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return received;

    }
}