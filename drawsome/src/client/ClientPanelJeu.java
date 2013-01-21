package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import core.Message;

public class ClientPanelJeu extends JPanel
{
	private ClientMain prgm_client;
	
	private ClientPanelDessin draw_area;
	private JTextArea players_area;
	private JScrollPane chat_pane;
	private JTextArea chat_area;
	private JTextField message_field;
	
	//////////////////
	// Constructeur //
	//////////////////
	public ClientPanelJeu (ClientMain prgm_client)
	{
		super ();
		
		this.prgm_client = prgm_client;

		setPreferredSize(new Dimension(800, 600));
		setBackground(new Color(0x7ebcbe));
		setLayout(null);

		initComponent();
	}

	///////////////////
	// InitComponent //
	///////////////////
	public void initComponent ()
	{
		draw_area = new ClientPanelDessin(prgm_client);
		draw_area.setBounds(5, 5, 790, 390);
		add(draw_area);
		
		players_area = new JTextArea();
		players_area.setEditable(false);
		players_area.setBounds(5, 400, 190, 195);
		add(players_area);
		
		chat_area = new JTextArea();
		chat_area.setEditable(false);
		
		chat_pane = new JScrollPane(chat_area)
		{
			protected void paintComponent (Graphics g)
			{
				super.paintComponent(g);
				
				setBorder(null);
			}
		};
		chat_pane.setBounds(200, 400, 595, 175);
		add(chat_pane);

		message_field = new JTextField();
		message_field.setBounds(200, 575, 595, 20);
		message_field.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String proposition = message_field.getText();

				if (!proposition.equals(""))
				{
					Message message = new Message(1, proposition);
					prgm_client.sendMessage(message);
					
					message_field.setText("");
				}
			}
		});
		add(message_field);
	}
	
	// on ajoute un message au jtextarea
	public void addProposition (String proposition)
	{
		String chat;
		
		if (!chat_area.getText().equals(""))
			chat = chat_area.getText() + "\n" + proposition;
		else
			chat = proposition;
		
		chat_area.setText(chat);
		chat_area.setCaretPosition(chat_area.getDocument().getLength());
	
	}

	////////////
	// Getter //
	////////////
	public ClientPanelDessin getClientPanelDessin ()
	{
		return draw_area;
	}
}
