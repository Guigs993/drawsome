package test;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DSClient extends JFrame
{
	private Socket clientSocket;
	private JFrame fenetre;
	  
	private JTextField message_field;
	private JTextArea chat_area;

	public DSClient ()
	{
		super();

		try
		{
			clientSocket = new Socket("localhost", 6112);
			
			// TODO changer le contenu du programme client
			fenetre = new JFrame();
			fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fenetre.setVisible(true);
			fenetre.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			
			PersoWindowListener listener = new PersoWindowListener(this);
			fenetre.addWindowListener(listener);
			
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
			
			chat_area = new JTextArea();
			chat_area.setPreferredSize(new Dimension(300, 300));

			fenetre.add(message_field);
			fenetre.add(chat_area);
			
			fenetre.pack();
		}
		catch (Exception e)
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
	}
	
	public Socket getSocket ()
	{
		return clientSocket;
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

	public static void main(String argv[]) throws Exception 
	{
		DSClient client = new DSClient();
		
		new ServeurThread(client.getSocket(), client);
	}

}
