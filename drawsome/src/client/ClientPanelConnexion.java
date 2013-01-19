package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.Message;

public class ClientPanelConnexion extends JPanel
{
	private ClientMain prgm_client;
	
	private JLabel logo_label;
	
	private JLabel rules_label;

	private JLabel nickname_label;
	private JLabel nickname_error;
	private JTextField nickname_field;
	private GoButton login_button;
	
	//////////////////
	// Constructeur //
	//////////////////
	public ClientPanelConnexion (ClientMain prgm_client)
	{
		super();
		
		this.prgm_client = prgm_client; 
		
		setPreferredSize(new Dimension(800, 600));
		setBackground(new Color(0xc2f8fa));
		setLayout(null);
		
		initComponent();
	}

	///////////////////
	// InitComponent //
	///////////////////
	public void initComponent ()
	{
		logo_label = new JLabel(new ImageIcon("images/logo.png"));
		logo_label.setBounds(0, 0, 800, 200);
		add(logo_label);

		rules_label = new JLabel(new ImageIcon("images/rules.png"));
		rules_label.setBounds(50, 250, 450, 300);
		add(rules_label);
		
		nickname_label = new JLabel(new ImageIcon("images/nickname.png"));
		nickname_label.setBounds(550, 350, 200, 20);
		add(nickname_label);
		
		nickname_error = new JLabel();
		nickname_error.setBounds(632, 352, 118, 20);
		nickname_error.setForeground(Color.RED);
		nickname_error.setAlignmentY(BOTTOM_ALIGNMENT);
		add(nickname_error);
		
		nickname_field = new JTextField();
		nickname_field.setBounds(550, 400, 200, 20);
		add(nickname_field);
		
		login_button = new GoButton();
		login_button.setBounds(600, 450, 100, 100);
		login_button.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ae)
			{	
				String nickname = nickname_field.getText();
				if (nickname.equals(""))
					showError(0);
				else
				{
					Message message = new Message(0, nickname);
					prgm_client.sendMessage(message);
				}
			}
		});
		add(login_button);
	}
	
	//////////////
	// Methodes //
	//////////////
	public void showError (int type)
	{
		if (type == 0)
			nickname_error.setText("Pseudo vide");
		else
			nickname_error.setText("Pseudo indisponible");
			
	}
}
