package core;

import java.io.Serializable;

// Classe message
public class Message implements Serializable
{	
	// type de message
	private int type;
	
	// contenu du message
	private Serializable content;

	//////////////////
	// Constructeur //
	//////////////////
	public Message (int type, Serializable content)
	{
		this.type = type;
		this.content = content;
	}
	
	/////////////
	// Getters //
	/////////////
	public int getType ()
	{
		return type;
	}
	
	public Serializable getContent ()
	{
		return content;
	}
}
