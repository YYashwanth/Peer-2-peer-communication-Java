import java.io.*;
import java.net.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


/**
*This class is the server class that has the files stored and transfers the files to the clients that request them.
*@author Yashwanth Yellapragada
*@version 1.1
*@see http://www.codejava.net/coding/file-encryption-and-decryption-simple-example
*/

public class TCPS extends Thread implements Serializable
{
	
	/**
	*<li> keygenerator : is of type keygenerator and that responsible to generate the key that encrypt the data
	*<li> myDesKey : is of type SecretKey and stores the key generated from keygenerator
	*<li> desCipher : is a cipher instance that takes in a secret key and text to encrypt.
	*<li> ip : is of type InetAddress that stores an IP.
	*<li> port : is of type int and stores a port number
	*<li> fname : is of type String and stores the file name that is to be transmitted.
	*/
	public static KeyGenerator keygenerator;
	public static SecretKey myDesKey;
	public static Cipher desCipher;
	private InetAddress ip;
	private int port;
	private String fname;
	
	/**
	* this parameterised constructor is used to initialise the variables required to transfer a file.
	* @param ip takes in an IP address
	* @param port takes in an int value to initialise the port number
	* @param fname takes in a string to initialise the file name
	*/
	public TCPS(InetAddress ip, String fname) throws Exception
	{
		
		try
		{
		this.ip = ip;
		this.port = 4010;
		this.fname = fname;
		keygenerator = KeyGenerator.getInstance("AES");
		myDesKey = keygenerator.generateKey();
		
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	//This contains the main code for the thread to execute when started
    public void run()
	{
		
		ObjectOutputStream objout= null;		
		System.out.println("File Transfer Initiated.");
		
		try
		{
		
			Socket clientSocket= new Socket(ip,port);
			objout = new ObjectOutputStream(clientSocket.getOutputStream());
			OutputStream out=clientSocket.getOutputStream();			
			String fileName = fname;
			System.out.println("File name requested is : " + fileName);
			File f= new File(fileName);
			DataOutputStream dos = new DataOutputStream(out);
			
			if(!f.exists()) //checks if the file is available in the server
			{
				System.out.println("File not found in the server directory");							
			}
			
			FileInputStream fis=new FileInputStream(f);
			System.out.println(myDesKey);
			dos.writeUTF(f.getName());
			dos.writeLong(f.length());		
			objout.writeObject((Object)myDesKey); 		 
			byte[] buffer = new byte[(int)f.length()];
			
			
			while (fis.read(buffer) > 0) 
			{				
				out.write(Encrypt(buffer));
				out.flush();
			}
			System.out.println("File has been sent");
			fis.close();
			out.close();
		
		}
		catch(Exception e)
		{
			System.out.println("There was some error in sending the file. Please try again or contact admin!");
		}    
	}
	
	/*This function is responsible for encrypting the data.
	* @param buffer takes in an array of bytes and encrypts them
	* @return byte[] returns the encrypted array of bytes
	*/
	public static byte[] Encrypt(byte [] buffer)
	{
		try{			
			desCipher = Cipher.getInstance("AES");
			desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
			byte[] textEncrypted = desCipher.doFinal(buffer);
			
			String s = new String(textEncrypted);
			return textEncrypted;

		}catch(Exception e)
		{
			System.out.println("Exception");
		}
		return null;
		
	}
}