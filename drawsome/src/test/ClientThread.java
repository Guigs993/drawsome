package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread implements Runnable
{
	private Thread t;
	private Socket s;
	private DataOutputStream outToClient; // inutilisée pour le moment
	private BufferedReader in;
	
	private DSServeur serveur;

	ClientThread(Socket s, DSServeur serveur)
	{
		this.serveur = serveur; 
		this.s = s; 

		try
		{
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			t = new Thread(this); 
			t.start();
		}
		catch (IOException e)
		{
			System.out.println("Erreur creation flux client");
		}

	}

	public void run()
	{
		String message = "";
		System.out.println("Connexion etablie");
	    try {
	    	// attente des messages
			while((message=in.readLine()) !=null)
			{
				// Boucle infinie vide, sinon la connexion se coupe
			}
	    }
		catch (SocketException e)
		{ 
			System.out.println("Connexion perdue");
		}
	    catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
