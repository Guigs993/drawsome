package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import client.Client;

import core.FinalStaticTools;
import core.Message;

public class ServerMain
{	
	private ServerSocket serverSocket;
	private Vector<Client> tabClients;
	private Vector<ServerReceiver> tabReceiver;
	
	private ArrayList<Client> tabPlayers;
	
	private ObjectOutputStream output;
	
	/////////////////
	// Construteur //
	/////////////////
	public ServerMain (int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		tabClients = new Vector<Client>();
		tabReceiver = new Vector<ServerReceiver>();
		
		tabPlayers = new ArrayList<Client>();
	}
	
	////////////////////////
	// Interaction Client //
	////////////////////////
	synchronized public void addClient (Client client)
	{
		tabClients.add(client);
	}

	synchronized public int searchClient (String nickname)
	{
		int index = -1;
		
		for (int i=0; i<tabClients.size(); i++)
		{
			if (tabClients.get(i).getNickname().equals(nickname))
				index = i;
		}
		
		return index;
	}
	
	synchronized public int searchClient (Socket socket)
	{
		int index = -1;
		
		for (int i=0; i<tabClients.size(); i++)
		{
			if (tabClients.get(i).getSocket() == socket)
				index = i;
		}
		
		return index;
	}
	
	synchronized public void removeClient (int index)
	{
		for (int i=0; i<tabReceiver.size(); i++)
		{
			if (tabReceiver.get(i).getIndexClient() == index)
				tabReceiver.remove(index);
		}
		
		tabClients.remove(index);
		
		for (ServerReceiver receiver : tabReceiver)
			receiver.refreshIndex();
	}
	
	synchronized public Client getClient (int index)
	{
		return tabClients.get(index);
	}
	
	synchronized public int getNbClients ()
	{
		return tabClients.size();
	}
	
	public void acceptClient () throws IOException
	{
		while (true)
		{
			tabReceiver.add(new ServerReceiver(serverSocket.accept(), this));
		}
	}
	
	//////////////
	// Methodes //
	//////////////
	public void sendMessage (Message message, int indexRecipient)
	{
		try
		{
			output = tabClients.get(indexRecipient).getOutput();
			output.writeObject(message);
			output.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void receiveMessage (Message message, int indexSender)
	{
		int type = message.getType();
		
		switch (type)
		{
			case 0 :
				String nickname = (String) message.getContent();

				int nickname_dispo = searchClient(nickname);
				if (nickname_dispo == -1)
				{
					Client client = tabClients.get(indexSender);
					client.setNickname(nickname);
					
					addPlayer(client);
					System.out.println("NB joueurs : " + getNbPlayers());
				}

				Message log_permission = new Message(0, nickname_dispo);

				sendMessage(log_permission, indexSender);
				break;
			case 1 :
				String nickname2 = getClient(indexSender).getNickname();
				String contenu = (String) message.getContent();
				
				boolean bonMot = false;
				if (!bonMot)
				{
					Message wrong_proposition = new Message(1, nickname2 + " : " + contenu);
					for (int i=0; i<tabClients.size(); i++)
						sendMessage(wrong_proposition, i);
				}
				break;
			default :
				break;
		}
	}
	
	///////////////////////
	// Mecaniques de jeu //
	///////////////////////
	public void addPlayer (Client client)
	{
		tabPlayers.add(client);
	}
	
	public int getNbPlayers ()
	{
		return tabPlayers.size();
	}
	
	public void removePlayer (String nickname)
	{
		int index = 0;
		
		for (Client player : tabPlayers)
		{
			if (!player.getNickname().equals(nickname))
				index++;
			else 
				break;
		}
		
		tabPlayers.remove(index);
	}
	
	//////////
	// Main //
	//////////
	public static void main (String[] args)
	{
		try
		{
			ServerMain prgmServer = new ServerMain(FinalStaticTools.PORT);
			prgmServer.acceptClient();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
