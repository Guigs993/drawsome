package client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import core.FinalStaticTools;
import core.Message;

public class ClientMain extends JFrame implements WindowListener
{
	private Socket clientSocket;
	
	private ObjectOutputStream output;
	
	private ClientPanelConnexion panel_connexion;
	private ClientPanelJeu panel_jeu;
	
	//////////////////
	// Constructeur //
	//////////////////
	public ClientMain () throws UnknownHostException, IOException
	{
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		addWindowListener(this);
		
		panel_connexion = new ClientPanelConnexion(this);
		setContentPane(panel_connexion);
		
		pack();
		setVisible(true);
		
		connectSocket();
	}
	
	////////////////////////
	// Interaction Server //
	////////////////////////
	public void connectSocket () throws UnknownHostException, IOException
	{
		clientSocket = new Socket("localhost", FinalStaticTools.PORT);
		
		output = new ObjectOutputStream(clientSocket.getOutputStream());
		
		new ClientReceiver(clientSocket, this);
	}
	
	public void tryLogin (String nickname)
	{
		try
		{
			Message message = new Message(0, nickname);
			output.writeObject(message);
			output.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//////////////
	// Methodes //
	//////////////
	public void sendMessage (Message message)
	{
		try
		{
			output.writeObject(message);
			output.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public void receiveMessage (Message message)
	{
		int type = message.getType();
		
		switch (type)
		{
			case 0 :
				int log_permission = ((Integer) message.getContent()).intValue();
				
				if (log_permission == -1)
				{
					panel_jeu = new ClientPanelJeu(this);
					setContentPane(panel_jeu);
					validate();
				}
				else
					panel_connexion.showError(1);
				break;
			case 1 :
				panel_jeu.addProposition((String) message.getContent());
				break;
			case 2 :
				break;
		}
	}

	//////////////////////
	// Listener Methods //
	//////////////////////
	public void windowClosed (WindowEvent e)
	{
		try
		{
			clientSocket.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public void windowActivated (WindowEvent e) {}

	public void windowClosing (WindowEvent e) {}

	public void windowDeactivated (WindowEvent e) {}

	public void windowDeiconified (WindowEvent e) {}

	public void windowIconified (WindowEvent e) {}

	public void windowOpened (WindowEvent e) {}

	//////////
	// Main //
	//////////
	public static void main (String[] args)
	{
		try
		{
			new ClientMain();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
