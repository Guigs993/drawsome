package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import client.Client;

import core.Message;

public class ServerReceiver implements Runnable
{
	private ServerMain server;
	
	private Thread thread;
	private Socket socket;
	
	private int indexClient;
	
	//////////////////
	// Constructeur //
	//////////////////
	public ServerReceiver (Socket socket, ServerMain server)
	{
		this.server = server;
		this.socket = socket;
		
		thread = new Thread(this);
		thread.start();
	}
	
	//////////////////
	// Start Thread //
	//////////////////
	public void run ()
	{
		Client client = new Client(socket);
		server.addClient(client);

		refreshIndex();
		
		try
		{
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

			while (true)
			{
				Message message = (Message) input.readObject();
				
				server.receiveMessage(message, indexClient);
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
		
		server.removePlayer(server.getClient(indexClient).getNickname());
		server.removeClient(indexClient);
		
		System.out.println("NB joueurs : " + server.getNbPlayers());
	}
	
	//////////////
	// Methodes //
	//////////////
	public void refreshIndex ()
	{
		indexClient = server.searchClient(socket);
	}
	
	public int getIndexClient ()
	{
		return indexClient;
	}
}
