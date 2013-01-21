package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Classe client
public class Client implements Comparable<Client>
{
	private String nickname;
	private Socket socket;
	private ObjectOutputStream output;
	
	private int nbPoints;
	private int nbDessins;
	
	//////////////////
	// Constructeur //
	//////////////////
	public Client (Socket socket)
	{
		try
		{
			this.socket = socket;
			output = new ObjectOutputStream(this.socket.getOutputStream());
			
			nickname = "";
			nbPoints = 0;
			nbDessins = 0;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/////////////
	// Getters //
	/////////////
	public String getNickname ()
	{
		return nickname;
	}
	
	public Socket getSocket ()
	{
		return socket;
	}
	
	public ObjectOutputStream getOutput ()
	{
		return output;
	}

	public int getPoints ()
	{
		return nbPoints;
	}
	
	public int getDessins ()
	{
		return nbDessins;
	}
	
	/////////////
	// Setters //
	/////////////
	public void setNickname (String nickname)
	{
		this.nickname = nickname;
	}

	// Methode inutilisée mais qui sert a trier les clients par ordre decroissant en fonction des points
	public int compareTo(Client client)
	{
		if (this.getPoints() > client.getPoints())
			return -1;
		else if (this.getPoints() == client.getPoints())
		{
			return this.getNickname().compareTo(client.getNickname());
		}
		else
			return 1;
	}
	
	//////////////
	// Methodes //
	//////////////
}
