package core;

import java.io.Serializable;

public class Message implements Serializable
{	
	private int type;
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
