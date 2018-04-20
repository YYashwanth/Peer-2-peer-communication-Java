import java.io.*;
import java.net.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;




/**
*This class is the client class that connects to the server and requests a file
*@author Yashwanth Yellapragada
*@version 1.1
*@see http://www.programcreek.com/java-api-examples/index.php?api=javax.crypto.CipherOutputStream
*/
public class tcpc extends Thread
{
	
	//This contains the main code that the thread executes when started
    public void run()
	{        	
		try
		{		
			ServerSocket ss=new ServerSocket(4010);
			while(true)
			{
				
				Socket clientSocket=ss.accept();	
				System.out.println("File transmission iniated. Receiving a file from : " + clientSocket.getInetAddress());
				ObjectInputStream objin = new ObjectInputStream(clientSocket.getInputStream());		       	
				InputStream in=clientSocket.getInputStream();
				OutputStream out=clientSocket.getOutputStream(); 		
				DataInputStream din = new DataInputStream(in);		
				DataOutputStream dos = new DataOutputStream(out);		
				String fileName2 = din.readUTF();
				if(fileName2.trim().isEmpty())
				{
					System.out.println("File could not found in the server. Please check your file name again!!");
					System.exit(0);
				}		
				long size = din.readLong();		
				SecretKey secretKey = (SecretKey)objin.readObject();	
				System.out.println(secretKey);		
				byte[] buffer = new byte[64512];
				File folder = new File("FilesReceived");
				if(!folder.exists())
				{
					folder.mkdir();
				}
				FileOutputStream fos=new FileOutputStream(new File(folder+"/"+fileName2));
				long filesize = size; 
				int read = 0;
				int totalRead = 0;						
				Cipher desCipher = Cipher.getInstance("AES");
				desCipher.init(Cipher.DECRYPT_MODE, secretKey);
				CipherOutputStream outSt = new CipherOutputStream(fos,desCipher);	//using cipher output stream to decrypt the incoming stream		
				while((read = in.read(buffer)) > 0)
				{
					totalRead += read;			
					if(read == 64512)
						outSt.write(buffer, 0, read);
					else
					{
					byte[] tempBuffer = new byte[read];
					for(int i = 0;i<read;i++)
					{
						tempBuffer[i] = buffer[i];
					}
					outSt.write(tempBuffer, 0, read);
					}			
				}	   
				System.out.println("read " + totalRead + " bytes.");
				fos.close();
			}	
		}
		catch(BindException e)
		{
			System.out.println("In class tcpc : " + e);
			
		}
		catch(Exception e)
		{
			System.out.println("In class tcpc : " + e);
			
		}
	}
}