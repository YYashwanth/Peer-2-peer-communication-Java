import java.io.BufferedReader;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

/**
*This class is the starter class that starts the program and is responsible for starting the threads.
*@author Yashwanth Yellapragada
*@version 1.1
*/
public class chatProgram {

	//An arraylist is declared and is used to check repeated requests from the IPs. This is made static because all other classes can access it.
	static ArrayList<InetAddress> initialIPs;
	//The main function of the program
	public static void main(String args[]) throws Exception
	{
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));	
		String subnet = "";
		String last="";
		int port = 4000;
		DatagramSocket serverSocket = new DatagramSocket();
		byte[] sendData = new byte[40];
		InetAddress currentIp = InetAddress.getLocalHost();
		String[] tempIp = currentIp.toString().split("/");		
		StringTokenizer ipSplit = new StringTokenizer(tempIp[1],".");
		int c = 0;
		int tokens = ipSplit.countTokens();		
		while(ipSplit.hasMoreTokens())
		{
			c++;
			if(c==4)
			{
				last = last+ipSplit.nextToken();
				break;
			}
			String temp = ipSplit.nextToken();
			subnet = subnet+temp+".";

		}		
		initialIPs = new ArrayList<InetAddress>();
		int timeout=100;		 
		int lastIP = Integer.parseInt(last);
		int toprange = lastIP + 150;
		int belowRange = lastIP - 10;
		if(belowRange<1)
			belowRange = 1;	
		if(toprange>255)
			toprange = 255;
		PingReceiver ping = new PingReceiver(port);
		ping.start();	
		for(int i = belowRange; i < toprange; i++)
		{
			String host=subnet + i;			
			InetAddress tempIP = InetAddress.getByName(host);
			sendData = "Are you online?".getBytes();
			DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length, tempIP, port);
            serverSocket.send(sendPacket);			
		}			
		while(true)
		{
			byte[] receiveData = new byte[40];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);		
			serverSocket.receive(receivePacket);
			String sentence = new String( receivePacket.getData());		
			InetAddress replyIPAddress = receivePacket.getAddress();
			String name = replyIPAddress.getHostName();		
			int replyPort = receivePacket.getPort();    
			if(sentence.contains("ABCD"))
			{
				byte[] sendData1 = new byte[40];				
				if(!replyIPAddress.equals(InetAddress.getLocalHost()))
				{
					System.out.println("System : " + replyIPAddress + " is connected");
					String sendMessage1 = "225.4.5.6";
					sendData1 = sendMessage1.getBytes();
					DatagramSocket serverSocket1 = new DatagramSocket();
					DatagramPacket sendPacket1 = new DatagramPacket(sendData1,sendData1.length,replyIPAddress,replyPort);
					serverSocket1.send(sendPacket1);
					if(!initialIPs.contains(replyIPAddress))
					{
						initialIPs.add(replyIPAddress);
					}
					Rec rec = new Rec(sendMessage1,4001);	
					tcpc filerec = new tcpc();							
					if(initialIPs.size()==1)
					{
						if(!rec.isAlive() && !filerec.isAlive())
						{
							rec.start();									
							filerec.start();	
						}								
					}						
				}					
			}	
		}	
	}	
}
