package test;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.util.Vector;

public class DSServeur
{
	// TODO variables obseletes pour le moment
	private Vector tabClients = new Vector();
	private int numNextClient = -1;
	
	synchronized public int addClient(DataOutputStream out)
	{
		numNextClient++;
		
		tabClients.addElement(out);
		
		return numNextClient;
	}

	synchronized public void removeClient(int i)
	{
		if (tabClients.elementAt(i) != null) 
		{
			tabClients.removeElementAt(i);
		}
	}
	
	public int getLastIndex()
	{
		return tabClients.size()-1;
	}
	
	public static void main (String[] args)
	{
		DSServeur serveur = new DSServeur();
		
		try
		{
			int port = 6112;
			ServerSocket ss = new ServerSocket(port);
			
			// Fausse boucle infinie, ss.accept() attend une connexion sur le socket
			while (true)
			{
		        new ClientThread(ss.accept(), serveur);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
