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

// Classe main du server
public class ServerMain
{
	private ServerSocket serverSocket;
	
	// un tableau contenant tous les clients qui se connecte au socket
	private Vector<Client> tabClients;

	// la liste de joueur
	private ArrayList<Client> tabPlayers;
	
	// un tableau avec tout les threads contenant chaque connexion au serveur
	private Vector<ServerReceiver> tabReceiver;
	
	// l'output qui permet d'envoyer des informations aux clients
	private ObjectOutputStream output;
	
	/////////////////
	// Construteur //
	/////////////////
	public ServerMain (int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		tabClients = new Vector<Client>();
		tabPlayers = new ArrayList<Client>();
		tabReceiver = new Vector<ServerReceiver>();
	}
	
	////////////////////////
	// Interaction Client //
	////////////////////////
	synchronized public void addClient (Client client)
	{
		tabClients.add(client);
	}

	// Recherche l'index d'un client avec son pseudo
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

	// Recherche l'index d'un client a partir de son socket
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
	
	// Supprime un client et le thread qui lui est associé
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
	
	// Recupere un client
	synchronized public Client getClient (int index)
	{
		return tabClients.get(index);
	}
	
	
	// Methode qui creer un thread a chaque connexion sur le server socket
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
	// Envoie de message
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
	
	// Methode qui trie les messages recu
	public void receiveMessage (Message message, int indexSender)
	{
		int type = message.getType();
		
		/*
		 * type : détermine le message recu coté serveur
		 * 		0  : le serveur reçoit un nickname
		 * 		1  : le serveur reçoit une proposition de mot
		 * 		21 : le serveur recoit un point qu'il renvoit à tout les clients
		 * 		22 : le serveur recoit le signal d'une fin de trace a la souris 
		 */
		switch (type)
		{
			// le serveur recoit un nickname
			case 0 :
				// on recupere le nickname
				String nickname = (String) message.getContent();
				
				// on cherche si le nickname est disponible dans la liste des joueurs
				int nickname_dispo = searchClient(nickname);
				// si le nickname est disponible
				if (nickname_dispo == -1)
				{
					// on lui attribut le nickname en question
					Client client = tabClients.get(indexSender);
					client.setNickname(nickname);
					
					// on l'ajoute dans la liste des joueurs
					addPlayer(client);
				}

				// on renvoie au joueur le signal d'autorisation d'authentification 
				Message log_permission = new Message(0, nickname_dispo);
				sendMessage(log_permission, indexSender);
				break;
			case 1 :
				// on recupere le nickname du joueur qui a envoyé une proposition
				String nickname2 = getClient(indexSender).getNickname();
				// on recupere la proposition de mot
				String contenu = (String) message.getContent();
				
				// on test si la proposition correspond au mot a trouver (TODO)
				boolean bonMot = false;
				
				// si le mot est faux, on envoie a tout le monde la proposition du joueur
				if (!bonMot)
				{
					Message wrong_proposition = new Message(1, nickname2 + " : " + contenu);
					for (int i=0; i<tabClients.size(); i++)
						sendMessage(wrong_proposition, i);
				}
				break;
				// si on reçoit un message contenant des informations sur un dessin (tracé, ou fin de tracé) on le revoit a tout le monde sauf au dessinateur
				// le fait de mettre case 21 et 22 a la suite sans mettre de "break;" implique qu'on fera les meme actions pour les 2 cas
			case 21 :
			case 22 :
				for (Client player : tabPlayers)
				{
					// si le joueur est diffent du dessinateur on envoie les donnees
					if (getClient(indexSender) != player)
					{
						try
						{
							output = player.getOutput();
							output.writeObject(message);
							output.flush();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
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
		int index = -1;
		
		for (Client player : tabPlayers)
		{
			index++;
			
			if (player.getNickname().equals(nickname)) 
				break;
		}
		
		if (index != -1)
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
