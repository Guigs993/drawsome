package test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.util.Vector;

public class DSServeur
{
	// TODO variables obseletes pour le moment
	private Vector tabClients = new Vector();
	private int numNextClient = -1;
	
	synchronized public int addClient(OutputStreamWriter out)
	{
		numNextClient++;
		
		tabClients.addElement(out);
		
		return numNextClient;
	}
	
	public int getLastIndex()
	{
		return tabClients.size()-1;
	}

	synchronized public void removeClient(OutputStreamWriter fluxClient)
	{
		tabClients.removeElement(fluxClient);
	}
	
	synchronized public void sendAll(String message)
	{
		OutputStreamWriter out;
	
		for (int i=0; i < tabClients.size(); i++)
		{
			out = (OutputStreamWriter) tabClients.elementAt(i);
			
			if (out != null)
			{
				try {
					out.write(message + "\n");
					out.flush();
				} catch (IOException e) {
					System.out.println("Erreur Ecriture");
				}
			}
		}
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
