import java.util.*;
import sun.net.*;
import java.net.*;

/**
*This class is responsible for taking in input from the user and sending it to the network.
*@author Yashwanth Yellapragada
*@version 1.1
*/
public class Sender extends Thread
{
	/**
	*<li> groupId : takes in a string value to join a multicast group.
	*<li> port : takes in an int value to store the port number.
	*/
	public String groupId="";
	public int port;
	
	/**
	*This parameterised constructor initialises the multicast group id and the port number
	*@param group takes in a string to initialise the groupId.
	*@param port takes in an int to initialise the port number
	*/
	public Sender(String group, int port)
	{
		this.groupId= group;
		this.port = port;
		
	}
	
	//This contains the main code that the thread executes when started
	public void run()
	{
		try
		{			
			String group = groupId;
			Scanner scan = new Scanner(System.in);
			MulticastSocket s = new MulticastSocket();
			while(true)
			{				
				byte buf[] = new byte[10];
				String message = scan.nextLine();
				String[] split = message.split(",");
				if(split.length==3 && split[0].equalsIgnoreCase("file"))
				{
					try
					{		
						try
							{
								InetAddress tempip = InetAddress.getByName(split[1]);
								String fname = split[2];		
								TCPS filesend = new TCPS(tempip,fname);
								filesend.start();								
							}
							catch(Exception e)
							{
								System.out.println("Invalid details : " + e.getMessage());
							}								
					
					}
					catch(Exception e)
					{
						System.out.println(e);
					}				
				}
				else if(message.equals("bye"))
				{					
					buf = "I am leaving the chat network.".getBytes();
					DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(group), port);
					s.send(pack);				
					//s.leaveGroup(InetAddress.getByName(group));
					System.exit(0);
				}				
				else
				{
					buf = message.getBytes();
					DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(group), port);
					s.send(pack);				
				}
				
			}
		}
		catch(Exception e)
		{
			System.out.println("In Sender class :" + e);
		}

			
	}
}