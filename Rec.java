
import sun.net.*;
import java.net.*;
import java.util.*;

/**
*This class manages all the messages being received.
*@author Yashwanth Yellapragada
*@version 1.1
*/
class Rec extends Thread
{
	/**
	*<li> groupId : takes in a string value to join a multicast group.
	*<li> port : takes in an int value to store the port number.
	*/
	public String groupId;
	public int port;
	
	/**
	*This parameterised constructor initialises the multicast group id and the port number
	*@param group takes in a string to initialise the groupId.
	*@param port takes in an int to initialise the port number
	*/
	public Rec(String group, int port)
	{
		this.groupId = group;
		this.port = port;
		
	}
	
	//This contains the main code that the thread executes when started
	public void run()
	{
		try
		{					
			String group = groupId;
			Scanner scan = new Scanner(System.in);
			
			MulticastSocket s = new MulticastSocket(port);
			
			Sender send = new Sender(group,port);
			
			send.start();					
			s.joinGroup(InetAddress.getByName(group));
			while(true)
			{
				byte receive[] = new byte[40];
				DatagramPacket pack = new DatagramPacket(receive, receive.length);
				s.receive(pack);
				if(pack.getAddress().equals(InetAddress.getLocalHost()))
				{
						
				}
					
				else
				{
					String[] tempIp = pack.getAddress().toString().split("/");
					System.out.println("Received data from : "+ pack.getAddress().getHostName() + " at " + tempIp[1].toString());
					System.out.write(pack.getData(),0,pack.getLength());
					System.out.println();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("In class Rec : " + e.getMessage());
		}

	}
}