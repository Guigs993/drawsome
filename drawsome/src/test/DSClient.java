package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class DSClient extends JFrame
{
	private Socket clientSocket;
	private JFrame fenetre;
	  
	private JTextField message_field;

	public DSClient ()
	{
		super();

		try
		{
			clientSocket = new Socket("localhost", 6112);
			
			// TODO changer le contenu du programme client
			fenetre = new JFrame();
			fenetre.setSize(300, 300);
			fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fenetre.setVisible(true);
			
			PersoWindowListener listener = new PersoWindowListener(this);
			fenetre.addWindowListener(listener);
			
			message_field = new JTextField();
			message_field.setSize(300, 300);
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
			
			fenetre.add(message_field);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
		DSClient prg_client = new DSClient();
	}

}
