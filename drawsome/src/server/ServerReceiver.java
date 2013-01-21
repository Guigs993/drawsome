package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import client.Client;

import core.Message;

// "Thread" qui s'occupera de la connexion avec chaque client
public class ServerReceiver implements Runnable
{
	private ServerMain server;
	
	private Thread thread;
	private Socket socket;
	
	// l'index du client dans le tableau de clients du serveur
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
		// on creer un client qu'on a joute a la liste de client
		Client client = new Client(socket);
		server.addClient(client);
		
		// on recupere son index
		refreshIndex();
		
		try
		{
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			// on recupere les message
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
		
		// on sort de la boucle while true au moment ou on perd la connexion du socket, on supprime
		// donc le client en question
		server.removePlayer(server.getClient(indexClient).getNickname());
		server.removeClient(indexClient);
	}
	
	//////////////
	// Methodes //
	//////////////
	public void refreshIndex ()
	{
		indexClient = server.searchClient(socket);
	}
	
	////////////
	// Getter //
	////////////
	public int getIndexClient ()
	{
		return indexClient;
	}
}
