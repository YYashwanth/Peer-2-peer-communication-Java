import java.io.*;
import java.net.*;
import java.util.*;


/**
*This class receives the request that has been broadcasted and sends a reply.
*@author Yashwanth Yellapragada
*@version 1.1
*/
class PingReceiver extends Thread
{
	//<li> port : takes in an int value to store the port number
	public int port;
	
	//this parameterised constructor initialises the port number
	//@param port takes in an int to initialise the port number
	public PingReceiver(int port)
	{
		this.port = port;
	}
	//This contains the main code for the thread to execute when started
	public void run()
	{
		try
		{
		
			DatagramSocket serverSocket = new DatagramSocket(port);		
			while(true)
			{
				byte[] receiveData = new byte[40];
				byte[] sendData = new byte[40];		
					
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				InetAddress IPAddress = receivePacket.getAddress();				
				StringTokenizer ipSplit = new StringTokenizer(sentence,".");
				if(ipSplit.countTokens()==4)
				{
					if(!chatProgram.initialIPs.contains(IPAddress))
					{
						System.out.println("System : " + IPAddress + " is connected ");
						tcpc filerec = new tcpc();
						if(!filerec.isAlive())
						filerec.start();
						Rec rec = new Rec(sentence, 4001);
						rec.start();
					}
					
				}				
				if(sentence.contains("Are you online"))
				{				
					
					int port = receivePacket.getPort(); 
					String sendsentence = "ABCD";
					sendData = sendsentence.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);				
					
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Class Ping Receiver : " + e);
		}
	}
}