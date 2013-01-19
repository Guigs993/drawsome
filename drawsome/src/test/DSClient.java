package test;
	
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DSClient extends JFrame implements ActionListener
{
	private Socket clientSocket;
	private JFrame fenetre;
	
	private PanelDessin draw_area;
	private JTextArea players_area;
	private JTextField message_field;
	private JScrollPane chat_pane;
	private JTextArea chat_area;
	private JButton bouton_env;

	public DSClient ()
	{
		super();
		
		try
		{
			clientSocket = new Socket("localhost", 6112);
				
			
			fenetre = new JFrame();
			fenetre.setLayout(null);
			fenetre.setResizable(false);
			fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			PersoWindowListener listener = new PersoWindowListener(this);
			fenetre.addWindowListener(listener);
			
			initComponent();
			
			// Message field
			message_field = new JTextField();
			message_field.setPreferredSize(new Dimension(300, 300));
			message_field.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					try
					{
						String message = message_field.getText();

						if (!message.equals(""))
						{
							OutputStreamWriter  out = new OutputStreamWriter (clientSocket.getOutputStream());
							out.write(message + "\n");
							out.flush();

							message_field.setText("");
						}

					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			
			fenetre.pack();	
			fenetre.setVisible(true);
			
			// Chat area
			chat_area = new JTextArea();
			chat_area.setPreferredSize(new Dimension(300, 300));
			
			
			// Bouton envoyer
			bouton_env = new JButton("Envoyer");
			bouton_env.setPreferredSize(new Dimension(200, 100));
			bouton_env.addActionListener(this);
			

			
			// On ajoute tout dans le JFrame fenetre
			fenetre.add(message_field);
			fenetre.add(chat_area);
			fenetre.add(bouton_env);
			
			fenetre.pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void initComponent() throws ParseException
	{
		draw_area = new PanelDessin(this);
		draw_area.setBounds(0, 0, 600, 300);
		
		players_area = new JTextArea();
		players_area.setEditable(false);
		players_area.setBounds(0, 300, 200, 200);
		
		chat_area = new JTextArea();
		chat_area.setEditable(false);
		
		chat_pane = new JScrollPane(chat_area);
		chat_pane.setBounds(200, 300, 400, 180);
		
		message_field = new JTextField();
		message_field.setBounds(200, 480, 400, 20);
		message_field.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String message = message_field.getText();

				if (!message.equals(""))
				{
					sendToServeur(1, message);
					message_field.setText("");
				}

			}
		});
	
		fenetre.add(draw_area);
		fenetre.add(players_area);
		fenetre.add(chat_pane);
		fenetre.add(message_field);
		
		fenetre.getContentPane().setPreferredSize(new Dimension(600, 500));
	}
	
	public void sendToServeur (int type, String message)
	{
		try
		{
			OutputStreamWriter out = new OutputStreamWriter (clientSocket.getOutputStream());
			out.write(type + ";" + message + "\n");
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void addMessage (String m)
	{
		String chat;
		if (!chat_area.getText().equals(""))
			chat = chat_area.getText() + "\n" + m;
		else
			chat = m;
		
		chat_area.setText(chat);
		chat_area.setCaretPosition(chat_area.getDocument().getLength());
	}
	
	public Socket getSocket ()
	{
		return clientSocket;
	}
	
	public PanelDessin getDessinPanel ()
	{
		return draw_area;
	}
	
	public void coupeThread ()
	{
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//////////
	// Main //
	//////////
	public static void main(String argv[]) throws Exception 
	{
		DSClient client = new DSClient();
		new ServeurThread(client.getSocket(), client);
	}
	
	

	// M�me action qu'appuyer sur le bouton entr�e. comment r�utiliser la m�thode d�ja �crite plutot que de la recopier??
	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			String message = message_field.getText();

			if (!message.equals(""))
			{
				OutputStreamWriter  out = new OutputStreamWriter (clientSocket.getOutputStream());
				out.write(message + "\n");
				out.flush();

				message_field.setText("");
			}

		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

}
