import java.util.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
	
	public static int port = 8080;
	public static String path = "src/";
	public static Boolean v = false;
	public static Boolean d = false;
	
    public static void main(String[] args) throws Exception {
    	Scanner command = new Scanner(System.in);
        System.out.print(">");
        String input = command.nextLine();
        String[] data = input.split(" ");
        if(data[0].equals("httpfs")) {
        	if(data.length >= 2) {
        		for(int i=1;i<data.length;i++) {
	        		if(data[i].equals("-p")) {
	            		port = Integer.parseInt(data[i+1]);
	            		i+=1;
	            	}else if(data[i].equals("-d")) {
	            		d = true;
	            		path = data[i+1];
	            		i+=1;
	            	}else if(data[i].equals("-v")) {
	            		v = true;
	            	}
        		}
        	}
        	System.out.print("Server is up and running");
        	if(v) {
        		System.out.print(" with port "+port);
        	}
        	if(d) {
        		System.out.print(" and path "+path);
        	}
        	System.out.println();
        	UDPrunning();
        }
    }

    public static void UDPrunning() throws IOException {
    	DatagramSocket dss = new DatagramSocket(port); 
        byte[] receive = new byte[65535];
        String reply = null;
        DatagramPacket DpReceive = null;
        ServerLib slib = new ServerLib();
        while (true) { 
            DpReceive = new DatagramPacket(receive, receive.length); 
            dss.receive(DpReceive); 
            String str = new String(DpReceive.getData());
            // Connections to the ServerLib
            // .....
            if(str.toLowerCase().contains("Get".toLowerCase())) {
            	reply = slib.get(path, str);
            }else if(str.toLowerCase().contains("Post".toLowerCase())) {
            	reply = slib.post(path, str);
            }
            //To Client Starts
            DatagramSocket dsc = new DatagramSocket();  
            InetAddress ip = InetAddress.getLocalHost(); 
            byte buf[] = new byte[65535];

            buf = reply.getBytes(); //reply = msg from server to client
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, DpReceive.getAddress(), DpReceive.getPort());
            dss.send(DpSend);
            //To Client Ends
            receive = new byte[65535]; 
        }
    }
}

