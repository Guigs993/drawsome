package client;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import core.FinalStaticTools;
import core.Message;

// Classe main de l'utilisateur
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
		
		// on se connecte au serveur et on ouvre le flux
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
	
	//////////////
	// Methodes //
	//////////////
	// Methode d'envoi de message au serveur
	public void sendMessage (Message message)
	{
		try
		{
			output.reset();
			output.writeObject(message);
			output.flush();
		}
		catch (IOException ioe)
		{
			//ioe.printStackTrace();
		}
	}
	
	// Methode qui trie les messages recu
	public void receiveMessage (Message message)
	{
		int type = message.getType();

		/*
		 * type : détermine le message recu coté client
		 * 		0  : le client reçoit la réponse de la disponibilité du pseudo
		 * 		1  : le client reçoit un mot a afficher parce qu'il est faux
		 * 		21 : le client recoit un point qu'il doit afficher sur son panel
		 * 		22 : le client recoit le signal d'une fin de trace a la souris 
		 */
		switch (type)
		{
			// le client recoit une confirmation d'authentification
			case 0 :
				int log_permission = ((Integer) message.getContent()).intValue();
				
				// si le nickname est disponible on passe a l'écran de jeu
				if (log_permission == -1)
				{
					panel_jeu = new ClientPanelJeu(this);
					setContentPane(panel_jeu);
					validate();
				}
				// si log_perssion est different de -1 le nickname est déjà utilisé
				else
					panel_connexion.showError(1);
				break;
			// le client recoit une proposition qui a ete refuse par le serveur
			// on l'ajoute donc au JTextArea
			case 1 :
				panel_jeu.addProposition((String) message.getContent());
				break;
			// On recoit un point a ajouter au JPanel
			case 21 :
				Point point = (Point) message.getContent();
				panel_jeu.getClientPanelDessin().setDraw(point);
				break;
			// On recoit un signal de fin de trace a la souris
			case 22 : 
				panel_jeu.getClientPanelDessin().setDraw();
				break;
		}
	}

	//////////////////////
	// Listener Methods //
	//////////////////////
	// Quand on ferme le programme, on coupe le socket
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
