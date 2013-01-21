package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import core.Message;

//"Thread" qui s'occupera de la connexion le serveur et donc l'écoute des messages recus
public class ClientReceiver implements Runnable
{
	private ClientMain prgmClient;

	private Thread thread;
	private Socket socket;

	//////////////////
	// Constructeur //
	//////////////////
	public ClientReceiver (Socket socket, ClientMain prgmClient)
	{
		this.socket = socket;
		this.prgmClient = prgmClient;

		thread = new Thread(this);
		thread.start();
	}
	
	//////////////////
	// Start Thread //
	//////////////////
	public void run ()
	{	
		try
		{
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			while (true)
			{
				Message message = (Message) input.readObject();
				
				prgmClient.receiveMessage(message);
			}
		}
		catch (SocketException se)
		{
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}
}
