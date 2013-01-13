package test;

import java.net.ServerSocket;
import java.util.Vector;

public class DSServeur
{
	// TODO variables obseletes pour le moment
	Vector tab_Clients = new Vector();
	int nb_Clients = 0;
	
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
