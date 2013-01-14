package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable
{
	private Thread t;
	private Socket s;
	
	private DataOutputStream out; // inutilisée pour le moment
	private BufferedReader in;
	
	private int numClient;
	private int indexClient;
	
	private DSServeur serveur;

	ClientThread(Socket s, DSServeur serveur)
	{
		this.serveur = serveur; 
		this.s = s; 

		try
		{
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			// out = new DataOutputStream(s.getOutputStream()); 
			
			numClient = serveur.addClient(out);
			indexClient = serveur.getLastIndex();
			
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
		
		System.out.println("client n°" + numClient + " : Connexion etablie");
		
	    try {
			while((message=in.readLine()) != null)
			{
				System.out.println("client n°" + numClient  + " : " + message);
			}
	    }
	    catch (Exception e)
		{
			e.printStackTrace();
		}
	    
		System.out.println("client n°" + numClient + " : Connexion interrompue");
		serveur.removeClient(indexClient);
	}
}
